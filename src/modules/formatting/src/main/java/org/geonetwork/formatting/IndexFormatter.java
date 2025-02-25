/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import static java.nio.charset.StandardCharsets.UTF_8;

import co.elastic.clients.json.JsonpUtils;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import org.geonetwork.domain.Metadata;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.search.SearchController;
import org.springframework.stereotype.Component;

/** Index formatter that writes the index document as JSON to the output stream. */
@Component
@AllArgsConstructor
public class IndexFormatter implements Formatter {
    SearchController searchController;

    @Override
    public void format(Metadata metadata, String formatterId, OutputStream outputStream) {
        try {
            IndexRecord indexDocument = searchController.getIndexDocument(metadata.getUuid());
            outputStream.write(JsonpUtils.toJsonString(indexDocument, new JacksonJsonpMapper())
                    .getBytes(UTF_8));
        } catch (Exception e) {
            throw new FormatterException(
                    String.format("Error occur while formatting record %s", metadata.getUuid()), e);
        }
    }
}
