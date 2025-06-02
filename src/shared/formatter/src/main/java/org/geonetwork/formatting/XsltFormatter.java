/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import java.io.OutputStream;
import java.util.HashMap;
import org.geonetwork.domain.Metadata;
import org.geonetwork.utility.xml.XsltUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Xslt formatter that transforms the metadata XML document using the XSLT stylesheet defined in schemas plugin
 * formatter folder.
 */
@Component
public class XsltFormatter implements Formatter {

    @Override
    public void format(Metadata metadata, String formatterId, OutputStream outputStream) {
        String formatterXslt = getFormatterXslt(metadata, formatterId);
        try {
            XsltUtil.transformXmlAsOutputStream(
                    metadata.getData(), new ClassPathResource(formatterXslt), new HashMap<>(), outputStream);
        } catch (Exception e) {
            throw new FormatterException(
                    String.format(
                            "Formatter %s not found. Hint: Available formatters for %s standard are: %s",
                            formatterId, metadata.getSchemaid(), "..."),
                    e);
        }
    }

    @Override
    public boolean isFormatterAvailable(Metadata metadata, String formatterId) {
        String formatterXslt = String.format("schemas/%s/formatter/%s/view.xsl", metadata.getSchemaid(), formatterId);
        return new ClassPathResource(formatterXslt).exists();
    }

    private String getFormatterXslt(Metadata metadata, String formatterId) {
        return String.format("schemas/%s/formatter/%s/view.xsl", metadata.getSchemaid(), formatterId);
    }
}
