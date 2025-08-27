/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterFacetInfo {
    public String filterName;
    public String filterEquation;
}
