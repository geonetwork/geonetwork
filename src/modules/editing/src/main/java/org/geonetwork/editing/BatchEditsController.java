/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing;

import static org.geonetwork.constants.ApiParams.API_CLASS_RECORD_TAG;
import static org.geonetwork.constants.ApiParams.API_CLASS_RECORD_TAG_DESCRIPTION;
import static org.geonetwork.constants.ApiParams.API_PARAM_BUCKET_NAME;
import static org.geonetwork.constants.ApiParams.API_PARAM_RECORD_UUIDS_OR_SELECTION;
import static org.geonetwork.constants.ApiParams.API_PARAM_UPDATE_DATESTAMP;
import static org.geonetwork.constants.ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT;
import static org.geonetwork.editing.ApiParams.API_OP_BATCH_BODY;
import static org.geonetwork.editing.ApiParams.API_OP_BATCH_DESCRIPTION;
import static org.geonetwork.editing.ApiParams.API_OP_BATCH_PREVIEW_RESPONSE;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.geonetwork.editing.model.BatchEditParameter;
import org.geonetwork.utility.legacy.xml.Xml;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = {"/api/records"})
@Tag(name = API_CLASS_RECORD_TAG, description = API_CLASS_RECORD_TAG_DESCRIPTION)
@RestController
@RequiredArgsConstructor
public class BatchEditsController {
    private final BatchEditsService batchEditService;

    @io.swagger.v3.oas.annotations.Operation(
            summary = "Preview batch editing results.",
            description = API_OP_BATCH_DESCRIPTION)
    @PostMapping(
            value = "/batchediting/preview",
            produces = {MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = API_OP_BATCH_PREVIEW_RESPONSE),
                @ApiResponse(responseCode = "403", description = API_RESPONSE_NOT_ALLOWED_CAN_EDIT)
            })
    @PreAuthorize("@securityService.hasMetadataBatchEditingAccessLevel()")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String previewBatchEdit(
            @Parameter(description = API_PARAM_RECORD_UUIDS_OR_SELECTION, required = false)
                    @RequestParam(required = false)
                    String[] uuids,
            @Parameter(description = API_PARAM_BUCKET_NAME, required = false) @RequestParam(required = false)
                    String bucket,
            //  TODO       @Parameter(
            //            description = "Return differences with diff, diffhtml or patch",
            //            required = false
            //        )
            //        @RequestParam(
            //            required = false
            //        )
            //            DiffType diffType,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = API_OP_BATCH_BODY) @RequestBody
                    BatchEditParameter[] edits,
            HttpServletRequest request)
            throws Exception {
        return Xml.getString(batchEditService
                .applyBatchEdits(uuids, bucket, false, edits, request, BatchEditMode.PREVIEW)
                .two());
    }

    @io.swagger.v3.oas.annotations.Operation(
            summary = "Batch edit one or more records",
            description = API_OP_BATCH_DESCRIPTION)
    @PutMapping(
            value = "/batchediting",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Return a report of what has been done."),
                @ApiResponse(responseCode = "403", description = API_RESPONSE_NOT_ALLOWED_CAN_EDIT)
            })
    @PreAuthorize("@securityService.hasMetadataBatchEditingAccessLevel()")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Object batchEdit(
            @Parameter(description = API_PARAM_RECORD_UUIDS_OR_SELECTION, required = false)
                    @RequestParam(required = false)
                    String[] uuids,
            @Parameter(description = API_PARAM_BUCKET_NAME, required = false) @RequestParam(required = false)
                    String bucket,
            @Parameter(description = API_PARAM_UPDATE_DATESTAMP, required = false)
                    @RequestParam(required = false, defaultValue = "false")
                    boolean updateDateStamp,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = API_OP_BATCH_BODY,
                            content =
                                    @Content(
                                            examples = {
                                                @ExampleObject(
                                                        name =
                                                                "ISO19139 / Remove all online resources having a protocol 'OGC:WMS'.",
                                                        value =
                                                                """
[{"xpath":".//gmd:onLine[*/gmd:protocol/*/text() = 'OGC:WMS']","value":"<gn_delete></gn_delete>"}]
"""),
                                                @ExampleObject(
                                                        name = "ISO19139 / Add a new keyword section.",
                                                        value =
                                                                """
[{
"xpath": "/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:descriptiveKeywords",
"value": "<gn_add><gmd:descriptiveKeywords xmlns:gmd=\\"http://www.isotc211.org/2005/gmd\\" xmlns:gco=\\"http://www.isotc211.org/2005/gco\\"><gmd:MD_Keywords><gmd:keyword><gco:CharacterString>Waste water</gco:CharacterString></gmd:keyword><gmd:type><gmd:MD_KeywordTypeCode codeList=\\"./resources/codeList.xml#MD_KeywordTypeCode\\" codeListValue=\\"theme\\"/></gmd:type></gmd:MD_Keywords></gmd:descriptiveKeywords></gn_add>"
}]
                                     """),
                                                @ExampleObject(
                                                        name = "ISO19139 / Replace a keyword.",
                                                        value =
                                                                """
                                                                 [{
                                                                 "xpath":".//gmd:keyword/gco:CharacterString[text() = 'wastewater']",
                                                                 "value":"<gn_replace>Waste water</gn_replace>"
                                                                }]
                                                                """)
                                            }))
                    @RequestBody
                    BatchEditParameter[] edits,
            HttpServletRequest request)
            throws Exception {

        // TODO: Return a report
        // TODO: Make it asynch
        return batchEditService
                .applyBatchEdits(uuids, bucket, updateDateStamp, edits, request, BatchEditMode.SAVE)
                .one();
    }
}
