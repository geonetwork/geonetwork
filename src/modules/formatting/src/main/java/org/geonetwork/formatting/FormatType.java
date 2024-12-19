/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

/**
 * Enumerates the support output types.
 *
 * @author Jesse on 10/26/2014.
 */
public enum FormatType {
    txt("text/plain"),
    html("text/html"),
    xml("application/xml"),
    json("application/json"),
    jsonld("application/vnd.schemaorg.ld+json"),
    pdf("application/pdf"),
    testpdf("application/test-pdf");
    public final String contentType;

    FormatType(String contentType) {
        this.contentType = contentType;
    }

    /** Find the best format matching the list of Accept header. If not found, return null */
    public static FormatType find(String acceptHeader) {
        if (acceptHeader != null) {
            String[] accept = acceptHeader.toLowerCase().split(",");
            for (String h : accept) {
                for (FormatType c : values()) {
                    if (h.startsWith(c.contentType)) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    public static FormatType findByFormatterKey(String formatterId) {
        if (formatterId == null) {
            return null;
        }

        if (formatterId.contains("dcat")) {
            return FormatType.xml;
        }
        for (FormatType c : FormatType.values()) {
            if (formatterId.contains(c.name())) {
                return c;
            }
        }

        return null;
    }
}
