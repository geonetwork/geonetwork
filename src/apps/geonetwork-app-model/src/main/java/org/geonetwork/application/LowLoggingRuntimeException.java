/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.application;

/** Use this instead of a generic RuntimeException for exceptions that you don't want to fully log. */
public class LowLoggingRuntimeException extends RuntimeException {

    public LowLoggingRuntimeException() {
        super();
    }

    public LowLoggingRuntimeException(String message) {
        super(message);
    }

    public LowLoggingRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LowLoggingRuntimeException(Throwable cause) {
        super(cause);
    }
}
