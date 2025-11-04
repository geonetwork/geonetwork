package org.geonetwork.ogcapi.ctrlreturntypes;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geonetwork.application.ctrlreturntypes.IControllerResponseObject;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetSummaryDto;
import org.geonetwork.ogcapi.service.querybuilder.OgcApiQuery;

@Getter
@Setter
@NoArgsConstructor
public class OgcApiRecordsMultiRecordResponse implements IControllerResponseObject {

    /** query for the records sent by the user* */
    OgcApiQuery userQuery;

    /** Id of the catalog/collection (ogcapi records) */
    String catalogId;

    /** from the request, set of profile names that the user wants */
    List<String> userRequestedProfiles;

    /** single record - one for each record in the response */
    List<OgcApiRecordsSingleRecordResponse> records;

    /** total number of records that match the query (TOTAL for paging) */
    long totalHits;

    /** records.size() */
    long recordsCount;

     Map<String, OgcApiRecordsFacetSummaryDto> facetInfo;
}
