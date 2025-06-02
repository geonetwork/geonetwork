/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
