/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.application.ctrlreturntypes;

import java.util.List;

/**
 * Implemented by generic message converters (i.e. {@code HttpMessageConverter<Object>}) that write several
 * {@link IControllerResponseObject} types, so {@link MimeAndProfilesForResponseType} can discover them.
 */
public interface IMultiResponseTypeWriter {

    /**
     * What types of controller result does this handle?
     *
     * @return classes that this writes
     */
    List<Class<? extends IControllerResponseObject>> getResponseTypes();
}
