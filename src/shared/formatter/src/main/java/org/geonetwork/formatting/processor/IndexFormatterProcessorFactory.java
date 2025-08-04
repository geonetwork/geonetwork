/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting.processor;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.geonetwork.schemas.MetadataSchemaConfiguration;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IndexFormatterProcessorFactory {
    private final List<IndexFormatterProcessor> processors;

    public IndexFormatterProcessor getFormatterProcessor(String id) {
        return processors.stream()
                .filter(processor -> processor.getName().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<MetadataSchemaConfiguration.Formatter> getAvailableFormatterProcessors() {
        return processors.stream()
                .map(p -> MetadataSchemaConfiguration.Formatter.builder()
                        .name(p.getName())
                        .contentType(p.getContentType())
                        .title(p.getTitle())
                        .officialProfileName(p.getOfficialProfileName())
                        .build())
                .collect(Collectors.toList());
    }
}
