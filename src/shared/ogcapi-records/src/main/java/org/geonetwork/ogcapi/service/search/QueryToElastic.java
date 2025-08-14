/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.search;

import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.And;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.google.common.base.Splitter;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.geometry.Rectangle;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGnElasticDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsJsonPropertyDto;
import org.geonetwork.ogcapi.service.queryables.QueryablesService;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This helps to build the "extra" (`"&amp;property=value"`) queryables in the OGCAPI search to an Elastic Index query.
 *
 * <p>see the documentation on "queryables.json" with goes into more depth.
 *
 * <p>This class uses the metadata in the "queryables.json" to determine how to make the elastic query.
 */
@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
@SuppressWarnings("unused")
public class QueryToElastic {

    @Autowired
    public QueryablesService queryablesService;

    /**
     * Given an already setup SearchSourceBuilder, add more queries to it for any of the request &amp;param=search-Text.
     * <br>
     * WHERE: param is a queryable (cf queryables.json).
     *
     * @param ogcApiQuery information from the search request.
     */
    public Query getQueryablesQuery(OgcApiQuery ogcApiQuery) {
        if (ogcApiQuery.getPropValues() == null || ogcApiQuery.getPropValues().isEmpty()) {
            return null;
        }

        List<Query> queries = new ArrayList<>();

        var jsonSchema = queryablesService.getFullQueryables(ogcApiQuery.getCollectionId());

        for (var prop : ogcApiQuery.getPropValues().entrySet()) {
            var jsonProp = jsonSchema.getProperties().get(prop.getKey());
            var searchVal = prop.getValue();
            var q = create(jsonProp, searchVal, "*");
            if (q != null) {
                queries.add(q);
            }
        }

        return BoolQuery.of(bq -> bq.must(queries))._toQuery();
    }

    /**
     * Given a JsonProperty, construct the appropriate Elastic Query. Handles Geo, Nested, Time, and text searches.
     *
     * @param jsonProperty property to process.
     * @param userSearchTerm what user is searching for
     * @param lang3iso what language?
     * @return QueryBuilder for this particular queryable.
     */
    public Query create(OgcApiRecordsJsonPropertyDto jsonProperty, String userSearchTerm, String lang3iso) {

        var isGeo = jsonProperty.getxGnElastic().stream()
                .anyMatch(x -> x.getElasticColumnType() == OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.GEO);

        var isNested = jsonProperty.getxGnElastic().stream()
                .anyMatch(x -> x.getElasticQueryType() == OgcApiRecordsGnElasticDto.ElasticQueryTypeEnum.NESTED);

        var isTime = (jsonProperty.getxGnElastic().getFirst().getElasticColumnType()
                        == OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.DATE)
                || (jsonProperty.getxGnElastic().getFirst().getElasticColumnType()
                        == OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.DATERANGE);

        if (isTime) {
            return createVsDate(jsonProperty.getxGnElastic().getFirst(), userSearchTerm, lang3iso);
        }

        if (isGeo) {
            return createGeo(jsonProperty.getxGnElastic().getFirst(), userSearchTerm, lang3iso);
        }

        if (isNested) {
            return createNested(jsonProperty.getxGnElastic(), userSearchTerm, lang3iso);
        }

        return createMulti(jsonProperty.getxGnElastic(), userSearchTerm, lang3iso);
    }

    /**
     * given an elastic config, the user search term, and the language, create a query. This function determines what
     * type of query to create, and then hands off to the create*() methods.
     *
     * @param gnElasticPath info about the how to query elastic
     * @param userSearchTerm what the user is looking for
     * @param lang3iso what language (iso 3 letter)
     * @return Query for the parameter
     */
    private Query create(OgcApiRecordsGnElasticDto gnElasticPath, String userSearchTerm, String lang3iso) {
        if (gnElasticPath.getElasticColumnType() == OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.TEXT
                && StringUtils.countMatches(gnElasticPath.getElasticPath(), ".") > 2) {
            return createNested(gnElasticPath, userSearchTerm, lang3iso);
        }
        if (gnElasticPath.getElasticColumnType() == OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.TEXT
                && StringUtils.countMatches(gnElasticPath.getElasticPath(), ".") <= 2) {
            return createMatch(gnElasticPath, userSearchTerm, lang3iso);
        }
        if (gnElasticPath.getElasticColumnType() == OgcApiRecordsGnElasticDto.ElasticColumnTypeEnum.KEYWORD) {
            return createTerm(gnElasticPath, userSearchTerm, lang3iso);
        }
        return null;
    }

    /**
     * create a date range query.
     *
     * @param gnElasticInfo column (singular) you are querying
     * @param userSearchTerm what searching for? (this will be parsed to an interval)
     * @param lang3iso language
     * @return QueryBuilder with a data-based range query.
     */
    public Query createVsDate(@Valid OgcApiRecordsGnElasticDto gnElasticInfo, String userSearchTerm, String lang3iso) {

        return RangeQuery.of(rq -> {
                    rq.field(gnElasticInfo.getElasticPath());
                    processDateRequest(rq, userSearchTerm);
                    return rq;
                })
                ._toQuery();
    }

    /**
     * Format.
     *
     * <p>interval-bounded = date-time "/" date-time interval-half-bounded-start = [".."] "/" date-time
     * interval-half-bounded-end = date-time "/" [".."] interval = interval-closed / interval-half-bounded-start /
     * interval-half-bounded-end datetime = date-time / interval
     *
     * <p>The syntax of date-time is specified by RFC 3339, 5.6. https://www.rfc-editor.org/rfc/rfc3339.html#section-5.6
     *
     * @param result RangeQueryBuilder to update with start/end (might only have start or end if "..")
     * @param userSearchTerm date or interval to parse.
     */
    private void processDateRequest(RangeQuery.Builder result, String userSearchTerm) {
        if (!userSearchTerm.contains("/")) {
            // its a single date (request) vs a date (elastic index)
            result.relation(RangeRelation.Intersects);
            result.gte(JsonData.of(userSearchTerm));
            result.lte(JsonData.of(userSearchTerm));
            return;
        }
        // interval
        var dateParts = Splitter.on('/').splitToList(userSearchTerm);
        if (dateParts.size() != 2) {
            return; // error!
        }

        result.relation(RangeRelation.Intersects);
        if (!dateParts.get(0).equals("..")) {
            result.gte(JsonData.of(dateParts.get(0)));
        }
        if (!dateParts.get(1).equals("..")) {
            result.lte(JsonData.of(dateParts.get(1)));
        }
    }

    /**
     * create a geo query (GeoBoundingBoxQuery).
     *
     * @param gnElasticInfo info about the index
     * @param userSearchTerm bbox from user (comma separated list from user)
     * @param lang3iso language (From request) - 3 letter iso lang value (i.e. 'eng') (not needed)
     * @return GeoBoundingBoxQuery for the requested bbox
     */
    private Query createGeo(OgcApiRecordsGnElasticDto gnElasticInfo, String userSearchTerm, String lang3iso) {

        var nums = Splitter.on(',').splitToList(userSearchTerm);
        if (nums.size() != 4) {
            return null;
        }

        Rectangle rectangle = new Rectangle(
                Double.parseDouble(nums.get(0)),
                Double.parseDouble(nums.get(2)),
                Double.parseDouble(nums.get(3)),
                Double.parseDouble(nums.get(1)));

        var geoQuery = GeoBoundingBoxQuery.of(q -> q.field(gnElasticInfo.getElasticPath())
                        .boundingBox(b -> b.trbl(trbl -> trbl.bottomLeft(
                                        x -> x.coords(Arrays.asList(rectangle.getMinX(), rectangle.getMinY())))
                                .topRight(x -> x.coords(Arrays.asList(rectangle.getMaxX(), rectangle.getMaxY()))))))
                ._toQuery();
        return geoQuery;
    }

    /**
     * convert a JsonProperty to an elastic QUERY.
     *
     * @param jsonProperty property
     * @return String representing the needed elastic query (or null if not possible)
     */
    public Query convert(OgcApiRecordsJsonPropertyDto jsonProperty, String userSearchTerm, String lang3iso) {
        var gnElasticPath = jsonProperty.getxGnElastic();
        if (gnElasticPath == null || gnElasticPath.isEmpty()) {
            return null;
        }
        if (gnElasticPath.size() > 1) {
            return createOR(gnElasticPath, userSearchTerm, lang3iso);
        }
        return create(gnElasticPath.getFirst(), userSearchTerm, lang3iso);
    }

    /**
     * create an elastic nested query. The main path will be the 1st path part of the full path. i.e. "contacts.phone"
     * -> main path will be "contacts".
     *
     * @param columns what columns are we searching in.
     * @param userSearchTerm what is the userer searching for
     * @param lang3iso language (From request) - 3 letter iso lang value (i.e. 'eng')
     * @return QueryBuilder (could be a nested, or an OR ("bool" "should" query) with multiple nested)
     */
    public Query createNested(List<OgcApiRecordsGnElasticDto> columns, String userSearchTerm, String lang3iso) {

        if (columns.size() == 1) {
            return createNested(columns.get(0), userSearchTerm, lang3iso);
        }

        var queryList = new ArrayList<Query>();

        for (var gnElasticInfo : columns) {
            var subQ = createNested(gnElasticInfo, userSearchTerm, lang3iso);
            queryList.add(subQ);
        }

        var result =
                BoolQuery.of(bq -> bq.minimumShouldMatch("1").should(queryList))._toQuery();

        return result;
    }

    /**
     * create a nested query with an internal match query.
     *
     * @param gnElasticPath info about elastic info
     * @param userSearchTerm what user is searching for
     * @param lang3iso language (From request) - 3 letter iso lang value (i.e. 'eng')
     * @return nested query
     */
    private Query createNested(OgcApiRecordsGnElasticDto gnElasticPath, String userSearchTerm, String lang3iso) {
        var userSearchTerm2 = userSearchTerm.replaceAll("\"", "");
        var path = gnElasticPath.getElasticPath().replace("${lang3iso}", lang3iso);
        var firstPartPath = path.substring(0, path.indexOf("."));

        var matchQuery = MatchQuery.of(
                        mq -> mq.field(path).query(userSearchTerm2).operator(And))
                ._toQuery();

        var nestedQuery = NestedQuery.of(nq -> nq.path(firstPartPath)
                        .query(matchQuery)
                        .scoreMode(ChildScoreMode.Max)
                        .ignoreUnmapped(true))
                ._toQuery();

        return nestedQuery;
    }

    /**
     * Create a match_multi elastic query for the given columns.
     *
     * @param columns which columns? (can contain "*")
     * @param userSearchTerm what text searching for?
     * @param lang3iso language to inject as "$P{iso3lang}
     * @return QueryBuilder with a match_multi
     */
    public Query createMulti(List<OgcApiRecordsGnElasticDto> columns, String userSearchTerm, String lang3iso) {
        var userSearchTerm2 = userSearchTerm.replaceAll("\"", "");

        var paths = columns.stream()
                .map(x -> x.getElasticPath())
                .map(x -> x.replace("${lang3iso}", lang3iso))
                .toList();

        var multiMatchQuery = MultiMatchQuery.of(mmq -> mmq.fields(paths)
                        .query(userSearchTerm2)
                        .fuzzyTranspositions(true)
                        .lenient(true)
                        .minimumShouldMatch("1")
                        .operator(And)
                        .fuzziness("AUTO"))
                ._toQuery();
        return multiMatchQuery;
    }

    /**
     * creates a MultiMatchQuery or a MatchQuery depending on if the path has a wildcard (*).
     *
     * @param gnElasticPath info about the elastic index
     * @param userSearchTerm what they are searching for
     * @param lang3iso language (From request) - 3 letter iso lang value (i.e. 'eng')
     * @return MultiMatchQuery or MatchQuery
     */
    private Query createMatch(OgcApiRecordsGnElasticDto gnElasticPath, String userSearchTerm, String lang3iso) {
        var userSearchTerm2 = userSearchTerm.replaceAll("\"", "");
        var path = gnElasticPath.getElasticPath().replace("${lang3iso}", lang3iso);

        if (path.contains("*")) {
            return MultiMatchQuery.of(
                            mmq -> mmq.query(userSearchTerm2).fields(path).operator(And))
                    ._toQuery();
        }

        return MatchQuery.of(mq -> mq.field(path).query(userSearchTerm2).operator(And))
                ._toQuery();
    }

    /**
     * creates a terms query.
     *
     * @param gnElasticPath info about the index
     * @param userSearchTerm what user is searching for
     * @param lang3iso language (From request) - 3 letter iso lang value (i.e. 'eng')
     * @return TermsQuery
     */
    private Query createTerm(OgcApiRecordsGnElasticDto gnElasticPath, String userSearchTerm, String lang3iso) {
        var userSearchTerm2 = userSearchTerm.replaceAll("\"", "");
        var path = gnElasticPath.getElasticPath().replace("${lang3iso}", lang3iso);

        return TermsQuery.of(tq -> tq.field(path).terms(t -> t.value(List.of(FieldValue.of(userSearchTerm2)))))
                ._toQuery();
    }

    /**
     * elastic "SHOULD" is an "OR".
     *
     * @param gnElasticPaths set of queries to OR ("should") together
     * @param userSearchTerm what user is searching for
     * @param lang3iso language (From request) - 3 letter iso lang value (i.e. 'eng')
     * @return String representing the needed elastic query (or null if not possible)
     */
    private Query createOR(List<OgcApiRecordsGnElasticDto> gnElasticPaths, String userSearchTerm, String lang3iso) {

        List<Query> queries = new ArrayList<>();
        for (var gnElasticInfo : gnElasticPaths) {
            var orItem = create(gnElasticInfo, userSearchTerm, lang3iso);
            if (orItem != null) {
                queries.add(orItem);
            }
        }

        return BoolQuery.of(bq -> bq.minimumShouldMatch("1").should(queries))._toQuery();
    }
}
