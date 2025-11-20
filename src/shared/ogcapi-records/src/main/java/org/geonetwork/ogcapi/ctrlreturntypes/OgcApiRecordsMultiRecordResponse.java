/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geonetwork.application.ctrlreturntypes.IControllerResponseObject;
import org.geonetwork.application.ctrlreturntypes.IMediaTypeAndProfile;
import org.geonetwork.application.ctrlreturntypes.RequestMediaTypeAndProfile;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetSummaryDto;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;

@Getter
@Setter
@NoArgsConstructor
public class OgcApiRecordsMultiRecordResponse implements IControllerResponseObject, IMediaTypeAndProfile {

    /** query for the records sent by the user* */
    OgcApiQuery userQuery;

    /** Id of the catalog/collection (ogcapi records) */
    String catalogId;

    /** single record - one for each record in the response */
    List<OgcApiRecordsSingleRecordResponse> records;

    /** total number of records that match the query (TOTAL for paging) */
    long totalHits;

    /** records.size() */
    long recordsCount;

    /** computed from elastic* */
    Map<String, OgcApiRecordsFacetSummaryDto> facetInfo;

    RequestMediaTypeAndProfile requestMediaTypeAndProfile;
}
