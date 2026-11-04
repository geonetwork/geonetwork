/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.dataaccess;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.util.ObjectBuilder;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.function.Function;
import org.geonetwork.index.client.IndexClient;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.controllerexceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Very simple tools for elastic. */
@Component
public class SimpleElastic {

    @Autowired
    private IndexClient client;

    @Autowired
    private ElasticWithUserPermissions elasticWithUserPermissions;

    public String getRecordIndexName() {
        return client.getIndexRecordName();
    }

    /**
     * given a records UUID, get it from the index. Only returns records the user is allowed to view.
     *
     * @param uuid record `_id`
     * @param filters extra filters (e.g. collection), can be empty
     * @return parsed elastic JSON index record
     * @throws NotFoundException record doesn't exist or user can't view it
     * @throws IOException bad connection to elastic
     */
    public IndexRecord getOne(String uuid, List<Query> filters) throws NotFoundException, IOException {
        var recordQuery = BoolQuery.of(b ->
                        b.filter(IdsQuery.of(i -> i.values(uuid))._toQuery()).filter(filters))
                ._toQuery();
        var query = elasticWithUserPermissions.createPermissionQuery(recordQuery);

        SearchResponse<IndexRecord> searchResponse = client.getEsClient()
                .search(s -> s.index(getRecordIndexName()).size(1).query(query), IndexRecord.class);

        if (searchResponse.hits().hits().isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Metadata record ''{0}'', which was not found.", uuid));
        }

        return searchResponse.hits().hits().getFirst().source();
    }

    /**
     * perform search on the Elastic Client. This is just a simple wrapper around client.getEsClient().search
     *
     * @param fn builder
     * @param tDocumentClass parse results into
     * @return elastic search results
     * @param <TDocument> parse results into
     * @throws IOException elastic connection error
     * @throws ElasticsearchException something wrong with the request
     */
    public final <TDocument> SearchResponse<TDocument> search(
            Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>> fn, Class<TDocument> tDocumentClass)
            throws IOException, ElasticsearchException {
        return client.getEsClient().search(fn, tDocumentClass);
    }
}
