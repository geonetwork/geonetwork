/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting.processor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.geonetwork.schemas.model.schemaident.Formatter;
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

    public List<Formatter> getAvailableFormatterProcessors() {
        return processors.stream()
                .map(p -> {
                  Formatter f = new Formatter();
                  f.setName(p.getId());
                  f.setProfile(p.getId());
                  f.setContentType(p.getContentType());
                  return f;
                }).collect(Collectors.toList());
    }
}
