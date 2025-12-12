/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.cql;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.util.HashSet;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;
import org.geotools.data.DataUtilities;
import org.geotools.filter.text.cql2.CQL;
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

        var filter = CQL.toFilter(requestQuery.getFilter());
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
        var filter = CQL.toFilter(cql);
        filter = DataUtilities.simplifyFilter(new org.geotools.api.data.Query("gn", filter))
                .getFilter();
        var validator = new IsSimpleFilterVisitor();
        filter.accept(validator, new HashSet<>());

        var query = ImprovedCqlFilter2Elastic.translate(filter, ogcElasticFieldMapper);
        return query;
    }
}
