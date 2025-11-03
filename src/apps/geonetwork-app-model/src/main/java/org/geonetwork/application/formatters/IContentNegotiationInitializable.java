/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application.formatters;

import java.util.List;
import org.geonetwork.application.ctrlreturntypes.IControllerResultFormatter;

/**
 * Allows modules to create MessageConverters (output formatting) to be registered with spring.
 *
 * <p>see its use in MessageWriterUtil as well as WebConfig.java
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
