/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.index.model.record;

import lombok.Builder;
import lombok.Data;

/** Coordinate reference system. */
@Data
@Builder
public class PartyIdentifier {
    private String code;
    private String codeSpace;
    private String link;
}
