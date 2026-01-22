/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadatastore;

import java.beans.PropertyEditorSupport;

/** Convert {@link Sort} enumeration from request parameter Created by francois on 31/12/15. */
public class SortConverter extends PropertyEditorSupport {
    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(Sort.valueOf(text.trim()));
    }
}
