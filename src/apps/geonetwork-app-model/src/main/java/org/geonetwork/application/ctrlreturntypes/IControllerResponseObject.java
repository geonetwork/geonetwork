/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application.ctrlreturntypes;

import java.net.URI;

/** Tag for the "little" objects that are returned from a controller */
public interface IControllerResponseObject {

    URI getJsonLink();
}
