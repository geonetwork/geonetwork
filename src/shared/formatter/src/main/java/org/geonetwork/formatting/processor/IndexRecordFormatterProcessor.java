/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting.processor;

import static java.nio.charset.StandardCharsets.UTF_8;

import co.elastic.clients.json.JsonpUtils;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import java.io.IOException;
import java.io.OutputStream;
import org.geonetwork.index.model.record.IndexRecord;
import org.springframework.stereotype.Component;

@Component
public class IndexRecordFormatterProcessor implements IndexFormatterProcessor {
    @Override
    public String getId() {
        return "json";
    }

    @Override
    public void process(IndexRecord indexRecord, OutputStream out) throws IOException {
        out.write(JsonpUtils.toJsonString(indexRecord, new JacksonJsonpMapper()).getBytes(UTF_8));
    }
}
