/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.geonetwork.application.ctrlreturntypes.IControllerResponseObject;
import org.geonetwork.application.ctrlreturntypes.IMediaTypeAndProfile;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;

@Getter
@Setter
@AllArgsConstructor
public class OgcApiLandingPageResponse implements IControllerResponseObject, IMediaTypeAndProfile {

    RequestMediaTypeAndProfile requestMediaTypeAndProfile;
    URI jsonLink;
}
