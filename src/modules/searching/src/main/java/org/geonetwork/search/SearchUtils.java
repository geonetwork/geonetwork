/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import io.micrometer.common.util.StringUtils;
import java.util.List;

/** Search utils. */
public class SearchUtils {
    public static SearchRequest buildDefaultSearch() {
        return null;
    }

    /** Add permission filter to search request. */
    public static SearchRequest buildRequestWithPermissionFilter(final SearchRequest request) {
        final Query existingQuery = request.query();
        Query filterQuery = QueryBuilders.term(t -> t.field("isTemplate").value("n"));
        // TODO: Add user permissions to filterQuery
        //    Object principal =
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Query queryWithPermission;
        if (StringUtils.isNotEmpty(request.q())) {
            queryWithPermission = Query.of(
                    q -> q.bool(b -> b.must(m -> m.queryString(QueryStringQuery.of(qsq -> qsq.query(request.q()))))));
        } else if (existingQuery == null) {
            queryWithPermission = Query.of(q -> q.bool(b -> b.must(filterQuery)));
        } else {
            queryWithPermission = Query.of(q -> q.bool(b -> b.must(existingQuery)
                    .filter(
                            (existingQuery.isBool() && existingQuery.bool().filter() != null)
                                    ? existingQuery.bool().filter()
                                    : List.of(filterQuery))));
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
            // TODO: Other props
            return s;
        });
    }
}
