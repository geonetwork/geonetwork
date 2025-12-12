/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
    public List<org.geonetwork.schemas.model.schemaident.Formatter> getAvailableFormatters(Metadata metadata) {
        List<org.geonetwork.schemas.model.schemaident.Formatter> formatters = new ArrayList<>();

        formatters.addAll(indexFormatterProcessorFactory.getAvailableFormatterProcessors());

        MetadataSchema metadataSchema = schemaManager.getSchema(metadata.getSchemaid());
        formatters.addAll(metadataSchema.getFormatters());

        return formatters;
    }

    /**
     * Returns a list of available formatters based on the metadata. Always includes "xml" and all available index and
     * xslt formatters.
     *
     * @param schemaId Metadata schema ID to determine available formatters.
     * @return List of available formatter IDs.
     */
    public List<org.geonetwork.schemas.model.schemaident.Formatter> getAvailableFormattersForSchema(String schemaId) {
        List<org.geonetwork.schemas.model.schemaident.Formatter> formatters = new ArrayList<>();

        formatters.addAll(indexFormatterProcessorFactory.getAvailableFormatterProcessors());

        MetadataSchema metadataSchema = schemaManager.getSchema(schemaId);
        formatters.addAll(metadataSchema.getFormatters());

        return formatters;
    }
}
