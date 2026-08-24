/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.thesaurus.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DublinCoreMultilingual {

    private String lang;
    private String tag;
    private String value;
}
