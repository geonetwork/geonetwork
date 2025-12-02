/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geonetwork.application.ctrlreturntypes.IControllerResponseObject;
import org.geonetwork.application.ctrlreturntypes.IMediaTypeAndProfile;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;
import org.geonetwork.index.model.record.IndexRecord;

/**
 * Typically the controller will create one of these and then expect there to be a formatter that will format this to a
 * particular MIME/profile format.
 *
 * <p>Controllers should return an object like this.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OgcApiRecordsSingleRecordResponse implements IMediaTypeAndProfile, IControllerResponseObject {

    /** Id of the catalog/collection (ogcapi records) */
    String catalogId;

    /** Id of the record (ogcapi records) (uuid in GN) */
    String recordId;

    /** Filled in by GN - actual elastic record for the record. Maybe null in some cases. */
    IndexRecord indexRecord;

    RequestMediaTypeAndProfile requestMediaTypeAndProfile;

    URI jsonLink;
}
