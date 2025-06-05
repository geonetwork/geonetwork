/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting.test;

import java.util.HashMap;
import java.util.Map;
import org.geonetwork.domain.Metadata;

/** Helper class for creating test data for formatter tests. */
public class FormatterTestHelper {

    /** Creates a test metadata object with default values. */
    public static Metadata createTestMetadata(String uuid, String schemaId, int id) {
        Metadata metadata = new Metadata();
        metadata.setId(id);
        metadata.setUuid(uuid);
        metadata.setSchemaid(schemaId);
        metadata.setData("<metadata><title>Test Metadata</title></metadata>");
        metadata.setChangedate("2024-01-01");
        metadata.setOwner(1);
        metadata.setGroupowner(1);
        return metadata;
    }

    /** Creates a map of standard formatters. */
    public static Map<String, String> createStandardFormatters() {
        Map<String, String> formatters = new HashMap<>();
        formatters.put("json", "application/json");
        formatters.put("xml", "application/xml");
        formatters.put("html", "text/html");
        formatters.put("pdf", "application/pdf");
        formatters.put("jsonld", "application/vnd.schemaorg.ld+json");
        return formatters;
    }

    /** Creates sample XML metadata content. */
    public static String createSampleXmlContent(String title, String abstract_) {
        return String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<gmd:MD_Metadata xmlns:gmd=\"http://www.isotc211.org/2005/gmd\">\n"
                        + "  <gmd:identificationInfo>\n"
                        + "    <gmd:MD_DataIdentification>\n"
                        + "      <gmd:citation>\n"
                        + "        <gmd:CI_Citation>\n"
                        + "          <gmd:title>\n"
                        + "            <gco:CharacterString>%s</gco:CharacterString>\n"
                        + "          </gmd:title>\n"
                        + "        </gmd:CI_Citation>\n"
                        + "      </gmd:citation>\n"
                        + "      <gmd:abstract>\n"
                        + "        <gco:CharacterString>%s</gco:CharacterString>\n"
                        + "      </gmd:abstract>\n"
                        + "    </gmd:MD_DataIdentification>\n"
                        + "  </gmd:identificationInfo>\n"
                        + "</gmd:MD_Metadata>",
                title, abstract_);
    }
}
