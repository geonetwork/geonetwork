/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.index.model.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/** Specification conformance. */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SpecificationConformance {

    private String link;
    private String title;
    private String explanation;
    private String date;
    private String pass;
}
