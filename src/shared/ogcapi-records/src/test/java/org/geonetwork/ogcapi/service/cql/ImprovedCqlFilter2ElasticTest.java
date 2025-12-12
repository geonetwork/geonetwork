/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.cql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import java.util.HashSet;
import org.geotools.data.DataUtilities;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.jupiter.api.Test;

public class ImprovedCqlFilter2ElasticTest {

    @Test
    public void t1() throws CQLException {

        Query result;

        result = doIt("dave='hi'", new TrivialFieldMapper());
        assertEquals(Query.Kind.Term, result._kind());
        assertEquals("__dave", ((TermQuery) result._get()).field());
        assertEquals("hi", ((TermQuery) result._get()).value().stringValue());

        result = doIt("dave='hi' or dave='hello'", new TrivialFieldMapper());
        assertEquals(Query.Kind.Bool, result._kind());
        assertEquals(0, ((BoolQuery) result._get()).must().size());
        assertEquals(2, ((BoolQuery) result._get()).should().size());
        assertEquals("1", ((BoolQuery) result._get()).minimumShouldMatch());
        assertEquals(
                Query.Kind.Term, ((BoolQuery) result._get()).should().get(0)._kind());
        assertEquals(
                Query.Kind.Term, ((BoolQuery) result._get()).should().get(1)._kind());

        result = doIt("(dave='hi') or (dave='hello')", new TrivialFieldMapper());
        assertEquals(Query.Kind.Bool, result._kind());
        assertEquals(0, ((BoolQuery) result._get()).must().size());
        assertEquals(2, ((BoolQuery) result._get()).should().size());
        assertEquals("1", ((BoolQuery) result._get()).minimumShouldMatch());
        assertEquals(
                Query.Kind.Term, ((BoolQuery) result._get()).should().get(0)._kind());
        assertEquals(
                Query.Kind.Term, ((BoolQuery) result._get()).should().get(1)._kind());

        result = doIt("((dave='hi') or (dave='hello'))", new TrivialFieldMapper());
        assertEquals(Query.Kind.Bool, result._kind());
        assertEquals(0, ((BoolQuery) result._get()).must().size());
        assertEquals(2, ((BoolQuery) result._get()).should().size());
        assertEquals("1", ((BoolQuery) result._get()).minimumShouldMatch());
        assertEquals(
                Query.Kind.Term, ((BoolQuery) result._get()).should().get(0)._kind());
        assertEquals(
                Query.Kind.Term, ((BoolQuery) result._get()).should().get(1)._kind());

        result = doIt("dave='hi' and dave='hello'", new TrivialFieldMapper());
        assertEquals(Query.Kind.Bool, result._kind());
        assertEquals(0, ((BoolQuery) result._get()).should().size());
        assertEquals(2, ((BoolQuery) result._get()).must().size());
        assertEquals(Query.Kind.Term, ((BoolQuery) result._get()).must().get(0)._kind());
        assertEquals(Query.Kind.Term, ((BoolQuery) result._get()).must().get(1)._kind());

        // normal wildcards
        result = doIt("dave like '%h__%'", new TrivialFieldMapper());
        assertEquals(Query.Kind.SimpleQueryString, result._kind());
        assertEquals("__dave", ((SimpleQueryStringQuery) result._get()).fields().get(0));
        assertEquals("*h??*", ((SimpleQueryStringQuery) result._get()).query());

        // escaped wildcards
        result = doIt("dave like '\\%h\\_\\_\\%'", new TrivialFieldMapper());
        assertEquals(Query.Kind.SimpleQueryString, result._kind());
        assertEquals("__dave", ((SimpleQueryStringQuery) result._get()).fields().get(0));
        assertEquals("%h__%", ((SimpleQueryStringQuery) result._get()).query());

        // escaped elastic chars
        result = doIt("dave like 'h*h'", new TrivialFieldMapper());
        assertEquals(Query.Kind.SimpleQueryString, result._kind());
        assertEquals("__dave", ((SimpleQueryStringQuery) result._get()).fields().get(0));
        assertEquals("h*h", ((SimpleQueryStringQuery) result._get()).query());

        result = doIt("dave < 3", new TrivialFieldMapper());
        assertEquals(Query.Kind.Range, result._kind());
        assertEquals("__dave", ((RangeQuery) result._get()).field());
        assertEquals("3", ((RangeQuery) result._get()).lt().toString());
        assertNull(((RangeQuery) result._get()).gt());
        assertNull(((RangeQuery) result._get()).gte());
        assertNull(((RangeQuery) result._get()).lte());

        result = doIt("dave between 3 and 5", new TrivialFieldMapper());
        assertEquals(Query.Kind.Range, result._kind());
        assertEquals("__dave", ((RangeQuery) result._get()).field());
        assertEquals("3", ((RangeQuery) result._get()).gte().toString());
        assertEquals("5", ((RangeQuery) result._get()).lte().toString());
        assertNull(((RangeQuery) result._get()).gt());
        assertNull(((RangeQuery) result._get()).lt());
    }

    public Query doIt(String cqlText, IFieldMapper fieldMapper) throws CQLException {
        var filter = CQL.toFilter(cqlText);
        filter = DataUtilities.simplifyFilter(new org.geotools.api.data.Query("gn", filter))
                .getFilter();
        var validator = new IsSimpleFilterVisitor();
        filter.accept(validator, new HashSet<>());
        var parsed = ImprovedCqlFilter2Elastic.translate(filter, fieldMapper);
        return parsed;
    }

    public static class TrivialFieldMapper implements IFieldMapper {

        @Override
        public String map(String field) {
            return "__" + field.replaceAll("/", ".");
        }

        @Override
        public String mapSort(String field) {
            return "";
        }
    }
}
