/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.configuration;

import lombok.Getter;
import lombok.Setter;

/** user configuration class for the Filter facet type - defines the name of the filter and its CQL expression. */
@Getter
@Setter
public class FilterFacetInfo {

    /** name of the filter */
    public String filterName;

    /** CQL expression for the filter. */
    public String filterEquationCql;
}
