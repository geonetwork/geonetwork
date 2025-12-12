/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.formatting;

public class FormatterException extends RuntimeException {
    public FormatterException(String message) {
        super(message);
    }

    public FormatterException(String message, Exception e) {
        super(message, e);
    }
}
