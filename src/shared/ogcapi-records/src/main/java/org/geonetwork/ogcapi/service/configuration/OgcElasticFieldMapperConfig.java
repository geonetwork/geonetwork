/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.configuration;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OgcElasticFieldMapperConfig {

    public String ogcProperty;
    public String elasticProperty;
    public String indexRecordProperty;

    public OverrideType typeOverride;

    /** add to the results (ie. already a pre-defined property that you want to use in facets) */
    public Boolean addProperty = true;

    public List<OgcFacetConfig> facetsConfig = new ArrayList<>();
}
