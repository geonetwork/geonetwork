/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.cql;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * CQL "BETWEEN" is inclusive, so its hard to create queries that are like `A >= 1 AND A <2`
 *
 * <p>This will create 2 elastic queries; a) range gte 1 b) range lt 2
 *
 * <p>Unfortunately, if they elastic item being queried is an array, then this will return TRUE if there's a value >=1
 * and a value <2 NOTE: [100, -100] would work because one is >=1 and one is <2
 *
 * <p>If we notice 2 range queries in an `AND` for the same property, then we combine them
 *
 * <p>{ gte: "1", lt: "2" }
 *
 * <p>this works as expected - [100, -100] will not work, but [1, ...] or [1.5, ....] will.
 */
@Component
public class ElasticBetweenSimplifier {

    /**
     * This takes a list of queries that will be AND-ed together. Looks inside for multiple RangeQueries against the
     * same field. If so, they are merged together.
     *
     * @param qs set of queries to be AND-ed together
     * @return same queries, with RangeQueries (same field) merged
     */
    public List<Query> simplify(List<Query> qs) {
        if (qs == null || qs.isEmpty()) {
            return qs;
        }
        if (qs.stream().filter(x -> x._kind().name().equals("Range")).count() < 2) {
            return qs; // not enough to simplify
        }

        var range_property = qs.stream()
                .filter(x -> x._kind().name().equals("Range"))
                .map(x -> (RangeQuery) x._get())
                .collect(Collectors.groupingBy(x -> x.field()))
                .entrySet()
                .stream()
                .filter(x -> x.getValue().size() == 2)
                .toList();

        var newQs = new ArrayList<Query>();

        for (var q : range_property) {
            var rq1 = q.getValue().get(0);
            var rq2 = q.getValue().get(1);
            var newQ = combineRange(rq1, rq2);
            newQs.add(newQ);
            qs = remove(qs, rq1);
            qs = remove(qs, rq2);
        }

        qs.addAll(newQs);
        return qs;
    }

    /**
     * given 2 range queries (with the same field), merge them together.
     *
     * @param rangeQuery0 first to merge
     * @param rangeQuery1 2nd to merge
     * @return merged query
     */
    public Query combineRange(RangeQuery rangeQuery0, RangeQuery rangeQuery1) {
        var query = Query.of(q -> q.range(x -> {
            x.field(rangeQuery0.field());
            x.gt(rangeQuery0.gt() == null ? rangeQuery1.gt() : rangeQuery0.gt());
            x.lt(rangeQuery0.lt() == null ? rangeQuery1.lt() : rangeQuery0.lt());
            x.gte(rangeQuery0.gte() == null ? rangeQuery1.gte() : rangeQuery0.gte());
            x.lte(rangeQuery0.lte() == null ? rangeQuery1.lte() : rangeQuery0.lte());
            return x;
        }));
        return query;
    }

    /**
     * given a list of queries, remove one from it.
     *
     * <p>NOTE: this uses `==` (reference) equals. NOTE: this looks inside each query (Query#_get()) to determine `==`
     *
     * @param qs list of queries
     * @param removeMe which one to remove
     * @return qs, but with removeMe removed
     */
    public List<Query> remove(List<Query> qs, RangeQuery removeMe) {
        return qs.stream().filter(x -> x._get() != removeMe).collect(Collectors.toCollection(ArrayList::new));
    }
}
