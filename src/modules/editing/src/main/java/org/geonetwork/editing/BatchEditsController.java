/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.editing.model.BatchEditParameter;
import org.geonetwork.utility.legacy.xml.Xml;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = {"/api/editing"})
@Tag(name = "API_CLASS_RECORD_TAG", description = "API_CLASS_RECORD_OPS")
@RestController
@RequiredArgsConstructor
public class BatchEditsController {
  private final BatchEditsService batchEditService;
  private final MetadataRepository metadataRepository;

  @io.swagger.v3.oas.annotations.Operation(summary = "Preview edits made by XPath expressions.")
  @RequestMapping(
      value = "/batchediting/preview",
      method = RequestMethod.POST,
      produces = {MediaType.APPLICATION_XML_VALUE})
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Processed records."),
        @ApiResponse(
            responseCode = "403",
            description = "ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT")
      })
  //  @PreAuthorize("hasAuthority('Editor')")
  @PreAuthorize("hasRole('Administrator')")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String previewBatchEdit(
      @Parameter(description = "ApiParams.API_PARAM_RECORD_UUIDS_OR_SELECTION", required = false)
          @RequestParam(required = false)
          String[] uuids,
      @Parameter(description = "ApiParams.API_PARAM_BUCKET_NAME", required = false)
          @RequestParam(required = false)
          String bucket,
      //        @Parameter(
      //            description = "Return differences with diff, diffhtml or patch",
      //            required = false
      //        )
      //        @RequestParam(
      //            required = false
      //        )
      //            DiffType diffType,
      @RequestBody BatchEditParameter[] edits,
      HttpServletRequest request)
      throws Exception {
    boolean previewOnly = true;
    System.out.println(metadataRepository.hashCode());
    System.out.println(metadataRepository.findAllByUuidIn(List.of("uuid1")).size());

    return Xml.getString(
        batchEditService.applyBatchEdits(uuids, bucket, false, edits, request, previewOnly).two());
  }
}
