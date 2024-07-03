/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */


package org.geonetwork.index.model.record;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AttributeTable {
    String name;
    String definition;
    String code;
    String link;
    String type;
    String cardinality;
    @Singular
    List<CodeListValue> values;
}
