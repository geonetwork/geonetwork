/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/** Resource identifier. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceIdentifier {

    String code;
    String codeSpace;
    String link;
}
