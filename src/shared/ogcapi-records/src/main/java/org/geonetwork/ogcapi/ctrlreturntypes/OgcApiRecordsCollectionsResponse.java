/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.geonetwork.application.ctrlreturntypes.IControllerResponseObject;
import org.geonetwork.application.ctrlreturntypes.IMediaTypeAndProfile;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;

/**
 * There currently aren't any "special" formats for the OgcApiRecordsCollectionsResponse - only the OGCAPI-Records spec
 * defined json response.
 *
 * <p>Because of this, we don't have anything in this object (except the user's requested profile, if present).
 */
@Getter
@Setter
@AllArgsConstructor
public class OgcApiRecordsCollectionsResponse implements IControllerResponseObject, IMediaTypeAndProfile {

    RequestMediaTypeAndProfile requestMediaTypeAndProfile;
    URI jsonLink;
}
