/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.application.ctrlreturntypes;

import java.net.URI;

/** Tag for the "little" objects that are returned from a controller */
public interface IControllerResponseObject {

    URI getJsonLink();
}
