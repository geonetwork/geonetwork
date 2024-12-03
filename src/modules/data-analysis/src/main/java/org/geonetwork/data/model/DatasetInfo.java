/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.data.model;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DatasetInfo extends BaseDataInfo {
    @Builder.Default
    private DataType dataType = DataType.VECTOR;

    private List<DatasetLayer> layers;
    private Map<String, Object> metadata;
}
