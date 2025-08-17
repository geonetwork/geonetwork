/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting.processor;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.geonetwork.formatting.processor.dcatap.IndexRecordToDcatModelSerializer;
import org.geonetwork.index.model.record.IndexRecord;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DcatApJsonldFormatterProcessor implements IndexFormatterProcessor {

    private final IndexRecordToDcatModelSerializer dcatSerializer;

    @Override
    public String getName() {
        return "json-ld-index";
    }

    @Override
    public String getContentType() {
        return "application/ld+json";
    }

    @Override
    public String getTitle() {
        return "JSON-LD Formatter for Index Records";
    }

    @Override
    public String getOfficialProfileName() {
        return "http://geonetwork.net/def/profile/json-ld-index";
    }

    @Override
    public String getProfileName() {
        return "json-ld-index";
    }

    @Override
    public void process(IndexRecord indexRecord, OutputStream out, Map<String, Object> config) throws IOException {
        String catalogUri = (String) config.get("catalogUri");
        String baseUri = (String) config.get("baseUri");

        // Serialize to JSON-LD DCAT-AP format
        String dcatJsonLd = dcatSerializer.serialize(indexRecord, catalogUri, baseUri);

        out.write(dcatJsonLd.getBytes(UTF_8));
    }
}
