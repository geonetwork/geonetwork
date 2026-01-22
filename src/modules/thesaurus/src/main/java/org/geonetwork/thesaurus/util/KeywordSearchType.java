/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.thesaurus.util;

public enum KeywordSearchType {
    STARTS_WITH("{0}*"),
    CONTAINS("*{0}*"),
    MATCH("{0}");

    KeywordSearchType(String s) {
        // TODO the SQL implementation. or we should only move with contains ?
    }
}
