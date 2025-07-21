package org.geonetwork.ogcapi.service.cql;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;
import org.geotools.data.DataUtilities;
import org.geotools.filter.text.cql2.CQL;
import org.springframework.stereotype.Component;

@Component
public class CqlToElasticSearch {

    /**
     * creates a query based on the CQL expression in the request.
     *
     * @param requestQuery - query from user request
     * @return null or the converted CQL expression
     */
    public Query create(OgcApiQuery requestQuery) throws Exception {

        if (requestQuery == null
                || StringUtils.isAllBlank(requestQuery.getFilter())
                || !Objects.equals(requestQuery.getFilterLang(), "cql-text")) {
            return null;
        }

        var fieldMapper = new OgcElasticFieldMapper();

        var filter = CQL.toFilter(requestQuery.getFilter());
        filter = DataUtilities.simplifyFilter(new org.geotools.api.data.Query("gn", filter))
                .getFilter();
        var validator = new IsSimpleFilterVisitor();
        filter.accept(validator, new HashSet<>());
        var strResult = CswFilter2Es.translate(filter, fieldMapper);

        try (var inputStream = new ByteArrayInputStream(strResult.getBytes(StandardCharsets.UTF_8))) {
            var result = new Query.Builder().withJson(inputStream);
            return result.build();
        }
    }
}
