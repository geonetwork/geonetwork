/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application.formatters;

import java.util.List;
import org.geonetwork.application.ctrlreturntypes.IControllerResultFormatter;

public interface IContentNegotiationInitializable {

    public List<IControllerResultFormatter> initialize() throws Exception;
}
