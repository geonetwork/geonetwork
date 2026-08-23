/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.application.formatters;

import java.util.List;
import org.geonetwork.application.ctrlreturntypes.IControllerResultFormatter;

/**
 * Allows modules to create MessageConverters (output formatting) to be registered with spring.
 *
 * <p>see its use in MessageWriterUtil as well as WebConfig.java
 *
 * <p>TODO: likely change this to a "factory"
 */
public interface IContentNegotiationInitializable {

    /**
     * Should return a list of formatters to be used by springs content negotiation system.
     *
     * @return list of formatters to be used by springs content negotiation system.
     * @throws Exception config issue?
     */
    List<IControllerResultFormatter> initialize() throws Exception;
}
