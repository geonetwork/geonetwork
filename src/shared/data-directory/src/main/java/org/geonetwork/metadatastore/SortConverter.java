/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
