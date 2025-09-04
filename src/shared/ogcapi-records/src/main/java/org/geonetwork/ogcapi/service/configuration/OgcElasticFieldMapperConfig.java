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

/** This is user-contributed configuration for "extra" properties. */
@Getter
@Setter
public class OgcElasticFieldMapperConfig {

    /** what ogc property (in the record's Properties/Additional-Properties) is this */
    public String ogcProperty;

    /** Location inside elastic json index that this property is from - use dot notation */
    public String elasticProperty;

    /** Location inside the `IndexRecord` object that this property is located - use dot notation */
    public String indexRecordProperty;

    /**
     * user-overrided type. Typically, to return either a list of (type) (`List<String>`) or a single-value type
     * (`String`). Differentiating between them from the elastic index definition isn't really possible.
     */
    public OverrideType typeOverride;

    /** Should this field be in the sortables? */
    public Boolean isSortable = false;

    /** should this field be in the queryables? */
    public Boolean isQueryable = false;

    /** info about the field */
    public String description;

    /** info about the field */
    public String title;

    /**
     * add to the JSON ogc output? usually, you do. However, you might not want to if you already have a pre-defined
     * property that you want to use in facets.
     */
    public Boolean addPropertyToOutput = true;

    /** config for facets */
    public List<OgcFacetConfig> facetsConfig = new ArrayList<>();
}
