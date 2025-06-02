/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.geonetwork.domain.Metadata;
import org.geonetwork.formatting.processor.IndexFormatterProcessorFactory;
import org.geonetwork.schemas.MetadataSchema;
import org.geonetwork.schemas.SchemaManager;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FormatterFactory {
    private final XsltFormatter xsltFormatter;
    private final IndexFormatter indexFormatter;
    private final IdentityFormatter identityFormatter;
    private final IndexFormatterProcessorFactory indexFormatterProcessorFactory;
    private final SchemaManager schemaManager;

    /**
     * Returns a Formatter based on the provided metadata and formatterId. If formatterId is null or "xml", returns the
     * identityFormatter. If the formatterId is available in indexFormatter, returns it. If the formatterId is available
     * in xsltFormatter, returns it. Otherwise, returns the identityFormatter.
     *
     * @param metadata Metadata object to determine available formatters
     * @param formatterId ID of the requested formatter
     * @return Appropriate Formatter instance
     */
    public Formatter getFormatter(Metadata metadata, String formatterId) {
        if (formatterId == null || formatterId.isEmpty() || "xml".equals(formatterId)) {
            return identityFormatter;
        } else {
            if (indexFormatter.isFormatterAvailable(metadata, formatterId)) {
                return indexFormatter;
            } else if (xsltFormatter.isFormatterAvailable(metadata, formatterId)) {
                return xsltFormatter;
            } else {
                return identityFormatter;
            }
        }
    }

    /**
     * Returns a list of available formatters based on the metadata. Always includes "xml" and all available index and
     * xslt formatters.
     *
     * @param metadata Metadata object to determine available formatters.
     * @return List of available formatter IDs.
     */
    public List<String> getAvailableFormatters(Metadata metadata) {
        List<String> formatters = new ArrayList<>();
        formatters.add("xml"); // Always available
        formatters.addAll(indexFormatterProcessorFactory.getAvailableFormatterProcessors());

        MetadataSchema metadataSchema = schemaManager.getSchema(metadata.getSchemaid());
        formatters.addAll(metadataSchema.getFormatters());

        return formatters;
    }
}
