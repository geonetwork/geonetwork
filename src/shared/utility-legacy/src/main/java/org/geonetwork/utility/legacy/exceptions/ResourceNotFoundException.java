/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.utility.legacy.exceptions;

import java.util.Locale;

public class ResourceNotFoundException extends LocalizedException {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    protected String getResourceBundleBeanQualifier() {
        return "apiMessages";
    }

    @Override
    public ResourceNotFoundException withMessageKey(String messageKey) {
        super.withMessageKey(messageKey);
        return this;
    }

    @Override
    public ResourceNotFoundException withMessageKey(String messageKey, Object[] messageKeyArgs) {
        super.withMessageKey(messageKey, messageKeyArgs);
        return this;
    }

    @Override
    public ResourceNotFoundException withDescriptionKey(String descriptionKey) {
        super.withDescriptionKey(descriptionKey);
        return this;
    }

    @Override
    public ResourceNotFoundException withDescriptionKey(String descriptionKey, Object[] descriptionKeyArgs) {
        super.withDescriptionKey(descriptionKey, descriptionKeyArgs);
        return this;
    }

    @Override
    public ResourceNotFoundException withLocale(Locale locale) {
        super.withLocale(locale);
        return this;
    }
}
