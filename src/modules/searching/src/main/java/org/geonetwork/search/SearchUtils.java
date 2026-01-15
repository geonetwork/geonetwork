/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geonetwork.index.client.ElasticWithUserPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Search utils. */
@Component
public class SearchUtils {
    public static SearchRequest buildDefaultSearch() {
        return null;
    }

    ElasticWithUserPermissions elasticWithUserPermissions;

    @Autowired
    public SearchUtils(ElasticWithUserPermissions elasticWithUserPermissions) {
        this.elasticWithUserPermissions = elasticWithUserPermissions;
    }

    /** Add permission filter to search request. */
    public SearchRequest buildRequestWithPermissionFilter(final SearchRequest request) {
        Query existingQuery = request.query();
        // TODO: Discuss if we want to filter out templates by default?
        // Query filterQuery = QueryBuilders.term(t -> t.field(IS_TEMPLATE).value("n"));
        Query permissionQuery = elasticWithUserPermissions.createPermissionQuery();

        Stream<Query> filterStream = Stream.of(permissionQuery).filter(Objects::nonNull);

        if (existingQuery != null
                && existingQuery.isBool()
                && existingQuery.bool().filter() != null) {
            filterStream = Stream.concat(existingQuery.bool().filter().stream(), filterStream);
        }

        List<Query> filterQueries = filterStream.collect(Collectors.toUnmodifiableList());

        Query queryWithPermission;
        if (StringUtils.isNotEmpty(request.q())) {
            queryWithPermission = Query.of(
                    q -> q.bool(b -> b.must(m -> m.queryString(QueryStringQuery.of(qsq -> qsq.query(request.q()))))
                            .filter(filterQueries)));
        } else if (existingQuery == null) {
            queryWithPermission = Query.of(q -> q.bool(b -> b.must(filterQueries)));
        } else {
            queryWithPermission =
                    Query.of(q -> q.bool(b -> b.must(existingQuery).filter(filterQueries)));
        }
        return SearchRequest.of(s -> {
            s.index(request.index());
            s.aggregations(request.aggregations());
            s.query(queryWithPermission);
            s.trackTotalHits(request.trackTotalHits());
            s.sort(request.sort());
            s.size(request.size());
            s.from(request.from());
            s.source(request.source());
            s.fields(request.fields());
            s.scriptFields(request.scriptFields());
            return s;
        });
    }
}
