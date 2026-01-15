/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
