/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.cql;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;
import org.geotools.api.filter.Filter;
import org.geotools.data.DataUtilities;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CqlToElasticSearch {

    @Autowired
    OgcElasticFieldMapper ogcElasticFieldMapper;
    /**
     * creates a query based on the CQL expression in the request.
     *
     * @param requestQuery - query from user request
     * @return null or the converted CQL expression
     */
    public Query create(OgcApiQuery requestQuery) throws Exception {

        if (requestQuery == null
                || StringUtils.isAllBlank(requestQuery.getFilter())
                || !Objects.equals(requestQuery.getFilterLang(), "cql2-text")) {
            return null;
        }

        var filter = parseCql(requestQuery.getFilter());
        filter = DataUtilities.simplifyFilter(new org.geotools.api.data.Query("gn", filter))
                .getFilter();
        var validator = new IsSimpleFilterVisitor();
        filter.accept(validator, new HashSet<>());

        var query = ImprovedCqlFilter2Elastic.translate(filter, ogcElasticFieldMapper);
        return query;
    }

    public Query create(String cql) throws Exception {
        if (cql == null || StringUtils.isAllBlank(cql)) {
            return null;
        }
        var filter = parseCql(cql);
        filter = DataUtilities.simplifyFilter(new org.geotools.api.data.Query("gn", filter))
                .getFilter();
        var validator = new IsSimpleFilterVisitor();
        filter.accept(validator, new HashSet<>());

        var query = ImprovedCqlFilter2Elastic.translate(filter, ogcElasticFieldMapper);
        return query;
    }

    /**
     * Parses CQL text, tolerating callers that percent-encode the {@code filter} value twice (the servlet container
     * only undoes one layer, so e.g. a literal {@code %} in a {@code LIKE} pattern survives as {@code %xx} text). We
     * don't control every client of this API, so rather than guessing up-front, we parse as-is first and only attempt
     * one extra decode pass if that fails - this can't corrupt a value that was already correctly single-encoded, since
     * that case always parses successfully on the first attempt.
     */
    Filter parseCql(String cql) throws CQLException {
        try {
            return CQL.toFilter(cql);
        } catch (CQLException firstAttemptFailure) {
            String decodedAgain;
            try {
                decodedAgain = URLDecoder.decode(cql, StandardCharsets.UTF_8);
            } catch (IllegalArgumentException notPercentEncoded) {
                throw firstAttemptFailure;
            }
            if (decodedAgain.equals(cql)) {
                throw firstAttemptFailure;
            }
            try {
                return CQL.toFilter(decodedAgain);
            } catch (CQLException secondAttemptFailure) {
                throw firstAttemptFailure;
            }
        }
    }
}
