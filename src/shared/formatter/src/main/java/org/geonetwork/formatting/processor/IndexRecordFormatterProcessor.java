/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.formatting.processor;

import static java.nio.charset.StandardCharsets.UTF_8;

import co.elastic.clients.json.JsonpUtils;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.geonetwork.index.model.record.IndexRecord;
import org.springframework.stereotype.Component;

@Component
public class IndexRecordFormatterProcessor implements IndexFormatterProcessor {
    @Override
    public String getName() {
        return "elastic-json-index";
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public String getTitle() {
        return "GeoNetwork Elastic JSON Index for a Record";
    }

    @Override
    public String getOfficialProfileName() {
        return "http://geonetwork.net/def/profile/elastic-json-index";
    }

    @Override
    public String getProfileName() {
        return "elastic-json-index";
    }

    @Override
    public void process(IndexRecord indexRecord, OutputStream out, Map<String, Object> config) throws IOException {
        out.write(JsonpUtils.toJsonString(indexRecord, new JacksonJsonpMapper()).getBytes(UTF_8));
    }
}
