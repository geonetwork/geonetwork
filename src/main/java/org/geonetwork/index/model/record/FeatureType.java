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
public class FeatureType {
    String typeName;
    String definition;
    String code;
    String isAbstract;
    String aliases;
    @Singular("attributeTable")
    List<AttributeTable> attributeTable;
}
