/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.dataaccess;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import java.io.IOException;
import java.text.MessageFormat;
import org.geonetwork.index.client.IndexClient;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.ogcapi.controllerexceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleElastic {

    @Autowired
    private IndexClient client;

    public IndexRecord getOne(String uuid) throws NotFoundException, IOException {
        SearchResponse<IndexRecord> searchResponse = client.getEsClient()
                .search(
                        s -> {
                            s.index("gn-records");
                            s.size(1);
                            s.q("_id:" + uuid);

                            return s;
                        },
                        IndexRecord.class);

        if (searchResponse.hits().hits().isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Metadata record ''{0}'', which was not found.", uuid));
        }

        var indexRecord = searchResponse.hits().hits().getFirst().source();
        return indexRecord;
    }
}
