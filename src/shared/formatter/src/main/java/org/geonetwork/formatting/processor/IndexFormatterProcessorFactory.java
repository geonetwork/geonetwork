/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting.processor;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IndexFormatterProcessorFactory {
    private final List<IndexFormatterProcessor> processors;

    public IndexFormatterProcessor getFormatterProcessor(String id) {
        return processors.stream()
                .filter(processor -> processor.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<String> getAvailableFormatterProcessors() {
        return processors.stream().map(IndexFormatterProcessor::getId).toList();
    }
}
