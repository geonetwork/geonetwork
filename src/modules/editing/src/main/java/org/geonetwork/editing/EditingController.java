/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing;

import static org.geonetwork.constants.ApiParams.API_PARAM_RECORD_UUID;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.constants.ApiParams;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.Statusvalue;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.domain.repository.StatusvalueRepository;
import org.geonetwork.indexing.IndexingMode;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = {"/api/records"})
@Tag(name = "API_CLASS_RECORD_TAG", description = "API_CLASS_RECORD_OPS")
@RestController
@Slf4j
@RequiredArgsConstructor
public class EditingController {
    private final MetadataRepository metadataRepository;

    private final StatusvalueRepository statusvalueRepository;

    private final AccessManager accessManager;

    private final EditUtils editUtils;

    public static final String ID = "id";
    public static final String MINOREDIT = "minor";
    public static final String VERSION = "version";
    private final MetadataManager metadataManager;

    @io.swagger.v3.oas.annotations.Operation(summary = "Edit a record", description = "Return HTML form for editing.")
    @RequestMapping(
            value = "/{metadataUuid}/editor",
            method = RequestMethod.GET,
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE})
    //    @PreAuthorize("hasAuthority('Editor')")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "The editor form."),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT)
            })
    public String startEditing(
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String metadataUuid,
            @Parameter(description = "Tab") @RequestParam(defaultValue = "") String currTab,
            @RequestParam(defaultValue = "false") boolean withAttributes,
            @Parameter(hidden = true) HttpSession session,
            @Parameter(hidden = true) @RequestParam Map<String, String> allRequestParams,
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
        Metadata metadata = accessManager.canEditRecord(metadataUuid, request);

        // TODO: Check permissions
        // Import ApiUtils.canEditRecord( ? AccessManager

        // TODO: Workflow

        // TODO: Start edit session (save record before any changes for rollback on cancel)

        boolean showValidationErrors = false;
        Element elMd = editUtils.getMetadataEmbedded(String.valueOf(metadata.getId()), showValidationErrors);
        if ("meta".equals(currTab)) {
            return Xml.getString(elMd);
        } else {
            // TODO: Editor configuration transformation
            return Xml.getString(elMd);
        }
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Save edits", description = "Save the HTML form content.")
    @PostMapping(
            value = "/{metadataUuid}/editor",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE})
    //  TODO @PreAuthorize("hasAuthority('Editor')")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "The editor form."),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT)
            })
    @ResponseBody
    public void saveEdits(
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String metadataUuid,
            @Parameter(description = "Tab") @RequestParam(defaultValue = "simple") String tab,
            @RequestParam(defaultValue = "false") boolean withAttributes,
            @RequestParam(defaultValue = "false") boolean withValidationErrors,
            @RequestParam(defaultValue = "false") boolean minor,
            @Parameter(description = "Submit for review directly after save.")
                    @RequestParam(defaultValue = Statusvalue.Status.DRAFT)
                    String status,
            @Parameter(description = "Save current edits.") @RequestParam(defaultValue = "false") boolean commit,
            @Parameter(description = "Save and terminate session.") @RequestParam(defaultValue = "false")
                    boolean terminate,
            @Parameter(description = "Record as XML. TODO: rename xml") @RequestParam(defaultValue = "") String data,
            @Parameter(hidden = true) @RequestParam Map<String, String> allRequestParams,
            HttpServletRequest request,
            HttpServletResponse response,
            @Parameter(hidden = true) HttpSession httpSession)
            throws Exception {
        log.trace("Saving metadata editing with UUID {}", metadataUuid);
        Metadata metadata = accessManager.canEditRecord(metadataUuid, request);

        String id = String.valueOf(metadata.getId());
        String isTemplate = allRequestParams.get(ApiParams.TEMPLATE);

        // TODO
        //    IMetadataValidator validator = applicationContext.getBean(IMetadataValidator.class);
        //    BaseMetadataStatus statusRepository = ApplicationContextHolder.get().getBean(BaseMetadataStatus.class);
        //    SettingManager sm = context.getBean(SettingManager.class);
        //    boolean isEditor = session.getProfile().equals(Profile.Editor);
        //    boolean isReviewer = session.getProfile().equals(Profile.Reviewer);
        //    boolean isAdmin = session.getProfile().equals(Profile.Administrator);
        //
        //    // Checks when workflow enabled if the user is allowed
        //    boolean isEnabledWorkflow = sm.getValueAsBool(Settings.METADATA_WORKFLOW_ENABLE);
        //    if (isEnabledWorkflow && isEditor && !statusRepository.canEditorEdit(metadata.getId())) {
        //      throw new NotAllowedException("Editing is allowed only in Draft state for the current profile.");
        //    }

        // TODO remove? Element params = new Element("request");
        Map<String, String> forwardedParams = allRequestParams.entrySet().stream()
                .filter(e -> !e.getKey().startsWith("_"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // int iLocalId = Integer.parseInt(id);
        // TODO dataMan.setTemplateExt(iLocalId, MetadataType.lookup(isTemplate));
        metadata.setIstemplate(isTemplate);

        // --- use StatusActionsFactory and StatusActions class to possibly
        // --- change status as a result of this edit (use onEdit method)
        // TODO:   log.trace(" > Trigger status actions based on this edit");
        //    StatusActionsFactory saf = context.getBean(StatusActionsFactory.class);
        //    StatusActions sa = saf.createStatusActions(context);
        //    sa.onEdit(iLocalId, minor);
        //    Element beforeMetadata = dataMan.getMetadata(context, String.valueOf(metadata.getId()), false, false,
        // false);

        IndexingMode indexingMode = terminate ? IndexingMode.full : IndexingMode.core;

        if (StringUtils.isNotEmpty(data)) {
            log.trace(" > Updating metadata using XML document provided");
            Element md = Xml.loadString(data, false);
            String changeDate = null;
            boolean updateDateStamp = !minor;
            boolean ufo = true;

            metadataManager.update(id, md, withValidationErrors, ufo, changeDate, updateDateStamp, indexingMode);

            //      TODO
            //      if (terminate) {
            //        XMLOutputter outp = new XMLOutputter();
            //        String xmlBefore = outp.outputString(beforeMetadata);
            //        String xmlAfter = outp.outputString(md);
            //        new RecordUpdatedEvent(Long.parseLong(id), session.getUserIdAsInt(), xmlBefore, xmlAfter)
            //          .publish(applicationContext);
            //      }
        } else {
            log.trace(" > Updating content with form parameters");
            editUtils.updateContent(allRequestParams, false, true, indexingMode);

            //      TODO Element afterMetadata = dataMan.getMetadata(context, String.valueOf(metadata.getId()), false,
            // false, false);
            //
            //      if (terminate) {
            //        XMLOutputter outp = new XMLOutputter();
            //        String xmlBefore = outp.outputString(beforeMetadata);
            //        String xmlAfter = outp.outputString(afterMetadata);
            //        new RecordUpdatedEvent(Long.parseLong(id), session.getUserIdAsInt(), xmlBefore, xmlAfter)
            //          .publish(applicationContext);
            //      }
        }

        // --- if finished then remove the XML from the session
        if ((commit) && (!terminate)) {
            return;
        }
        if (terminate) {
            log.trace(" > Closing editing session");

            // TODO      boolean forceValidationOnMdSave =
            // sm.getValueAsBool(METADATA_WORKFLOW_FORCE_VALIDATION_ON_MD_SAVE);
            //
            //      boolean reindex = false;
            //
            //      String lang = String.valueOf(languageUtils.parseAcceptLanguage(request.getLocales()));
            //      ResourceBundle messages = ResourceBundle.getBundle("org.fao.geonet.api.Messages",
            //        new Locale(lang));
            //
            //      // Save validation if the forceValidationOnMdSave is enabled
            //      if (forceValidationOnMdSave) {
            //        validator.doValidate(metadata, context.getLanguage());
            //        reindex = true;
            //      }
            //
            //      // Automatically change the workflow state after save
            //      if (isEnabledWorkflow) {
            //        boolean isAllowedSubmitApproveInvalidMd =
            // sm.getValueAsBool(Settings.METADATA_WORKFLOW_ALLOW_SUBMIT_APPROVE_INVALID_MD);
            //        if (((status.equals(StatusValue.Status.SUBMITTED))
            //          || (status.equals(StatusValue.Status.APPROVED)))
            //          && !isAllowedSubmitApproveInvalidMd) {
            //
            //          if (!forceValidationOnMdSave) {
            //            validator.doValidate(metadata, context.getLanguage());
            //          }
            //          boolean isInvalid = MetadataUtils.retrieveMetadataValidationStatus(metadata, context);
            //
            //          if (isInvalid) {
            //            throw new NotAllowedException("Metadata is invalid: can't be submitted or approved")
            //              .withMessageKey("exception.resourceInvalid.metadata")
            //              .withDescriptionKey("exception.resourceInvalid.metadata.description");
            //          }
            //        }
            //
            //
            //        if (status.equals(StatusValue.Status.SUBMITTED)) {
            //          // Only editors can submit a record
            //          if (isEditor || isAdmin) {
            //            Integer changeToStatus = Integer.parseInt(StatusValue.Status.SUBMITTED);
            //            StatusValue statusValue = statusValueRepository.findById(changeToStatus).get();
            //
            //            MetadataStatus metadataStatus = new MetadataStatus();
            //
            //            metadataStatus.setMetadataId(metadata.getId());
            //            metadataStatus.setUuid(metadata.getUuid());
            //            metadataStatus.setChangeDate(new ISODate());
            //            metadataStatus.setUserId(session.getUserIdAsInt());
            //            metadataStatus.setStatusValue(statusValue);
            //            metadataStatus.setChangeMessage(messages.getString("metadata_save_submit_text"));
            //
            //            List<MetadataStatus> listOfStatusChange = new ArrayList<>(1);
            //            listOfStatusChange.add(metadataStatus);
            //            sa.onStatusChange(listOfStatusChange, true);
            //          } else {
            //            throw new SecurityException(String.format("Only users with editor profile can submit."));
            //          }
            //        }
            //        if (status.equals(StatusValue.Status.APPROVED)) {
            //          // Only reviewers can approve
            //          if (isReviewer || isAdmin) {
            //            Integer changeToStatus = Integer.parseInt(StatusValue.Status.APPROVED);
            //            StatusValue statusValue = statusValueRepository.findById(changeToStatus).get();
            //
            //            MetadataStatus metadataStatus = new MetadataStatus();
            //
            //            metadataStatus.setMetadataId(metadata.getId());
            //            metadataStatus.setUuid(metadata.getUuid());
            //            metadataStatus.setChangeDate(new ISODate());
            //            metadataStatus.setUserId(session.getUserIdAsInt());
            //            metadataStatus.setStatusValue(statusValue);
            //            metadataStatus.setChangeMessage(messages.getString("metadata_save_approve_text"));
            //
            //            List<MetadataStatus> listOfStatusChange = new ArrayList<>(1);
            //            listOfStatusChange.add(metadataStatus);
            //            sa.onStatusChange(listOfStatusChange, true);
            //          } else {
            //            throw new SecurityException(String.format("Only users with review profile can approve."));
            //          }
            //        }
            //        reindex = true;
            //      }
            //
            //      boolean automaticUnpublishInvalidMd =
            // sm.getValueAsBool(METADATA_WORKFLOW_AUTOMATIC_UNPUBLISH_INVALID_MD);
            //      boolean isUnpublished = false;
            //
            //      // Unpublish the metadata automatically if the setting
            //      // automaticUnpublishInvalidMd is enabled and
            //      // the metadata becomes invalid
            //      if (automaticUnpublishInvalidMd) {
            //        final OperationAllowedRepository operationAllowedRepo = context
            //          .getBean(OperationAllowedRepository.class);
            //
            //        boolean isPublic = (operationAllowedRepo.count(where(hasMetadataId(id))
            //          .and(hasOperation(ReservedOperation.view)).and(hasGroupId(ReservedGroup.all.getId()))) > 0);
            //
            //        if (isPublic) {
            //          final MetadataValidationRepository metadataValidationRepository = context
            //            .getBean(MetadataValidationRepository.class);
            //
            //          boolean isInvalid = (metadataValidationRepository
            //            .count(MetadataValidationSpecs.isInvalidAndRequiredForMetadata(Integer.parseInt(id))) > 0);
            //
            //          if (isInvalid) {
            //            isUnpublished = true;
            //            operationAllowedRepo
            //              .deleteAll(where(hasMetadataId(id)).and(hasGroupId(ReservedGroup.all.getId())));
            //          }
            //
            //          reindex = true;
            //        }
            //
            //      }
            //
            //      if (reindex) {
            //        Log.trace(Geonet.DATA_MANAGER, " > Reindexing record");
            //        metadataIndexer.indexMetadata(id, true, IndexingMode.full);
            //      }
            //
            //      // Reindex the metadata table record to update the field _statusWorkflow that contains the composite
            //      // status of the published and draft versions
            //      if (metadata instanceof MetadataDraft) {
            //        Metadata metadataApproved = metadataRepository.findOneByUuid(metadata.getUuid());
            //
            //        if (metadataApproved != null) {
            //          metadataIndexer.indexMetadata(String.valueOf(metadataApproved.getId()), true,
            // IndexingMode.full);
            //        }
            //      }

            editUtils.removeMetadataEmbedded(id);
            // Removed - done in removeMetadataEmbedded metadataManager.endEditingSession(id);

            // TODO     if (isEnabledWorkflow) {
            //        // After saving & close remove the information to remove the draft copy if the user cancels the
            // editor
            //        context.getUserSession().removeProperty(Geonet.Session.METADATA_EDITING_CREATED_DRAFT);
            //      }

            //  TODO    if (isUnpublished) {
            //        throw new IllegalStateException(String.format("Record saved but as it was invalid at the end of "
            //          + "the editing session. The public record '%s' was unpublished.", metadata.getUuid()));
            //      } else {
            //        return;
            //      }
        }

        Element elMd = editUtils.getMetadataEmbedded(id, withValidationErrors);
        response.getOutputStream().write(Xml.getString(elMd).getBytes(StandardCharsets.UTF_8));
        //    TODO buildEditorForm(tab, httpSession, forwardedParams, request, elMd,
        // metadata.getDataInfo().getSchemaId(), context,
        //      applicationContext, false, false, response);

        //   TODO  if (isEnabledWorkflow) {
        //      // After saving the form remove the information to remove the draft copy if the user cancels the editor
        //      context.getUserSession().removeProperty(Geonet.Session.METADATA_EDITING_CREATED_DRAFT);
        //    }
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Cancel edits", description = "Cancel current editing session.")
    @DeleteMapping(
            value = "/{metadataUuid}/editor",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE})
    //  TODO @PreAuthorize("hasAuthority('Editor')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Editing session cancelled.",
                        content = {@Content(schema = @Schema(hidden = true))}),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT)
            })
    @ResponseBody
    public void cancelEdits(
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String metadataUuid,
            @Parameter(hidden = true) @RequestParam Map<String, String> allRequestParams,
            HttpServletRequest request,
            @Parameter(hidden = true) HttpSession httpSession)
            throws Exception {
        Metadata metadata = accessManager.canEditRecord(metadataUuid, request);

        // TODO: dataMan.cancelEditingSession(context, String.valueOf(metadata.getId()));
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Add element", description = "")
    @PutMapping(
            value = "/{metadataUuid}/editor/elements",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasAuthority('Editor')")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Element added."),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT)
            })
    public void addElement(
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String metadataUuid,
            @Parameter(
                            description =
                                    "Reference of the insertion point. ie. `geonet:element/@ref` of the new element parent.",
                            required = true)
                    @RequestParam
                    String ref,
            @Parameter(
                            description =
                                    "Name of the element or attribute to add (with prefix). eg. `mri:alternateTitle`, `gco:nilReason`",
                            required = true)
                    @RequestParam
                    String name,
            @Parameter(
                            description = "Empty for inserting element, `geonet:attribute` for attributes.",
                            required = false)
                    @RequestParam(required = false)
                    String child,
            @Parameter(description = "Should attributes be shown on the editor snippet?", required = false)
                    @RequestParam(defaultValue = "false")
                    boolean displayAttributes,
            @Parameter(hidden = true) @RequestParam Map<String, String> allRequestParams,
            HttpServletRequest request,
            HttpServletResponse response,
            @Parameter(hidden = true) HttpSession httpSession)
            throws Exception {
        Metadata metadata = accessManager.canEditRecord(metadataUuid, request);

        // -- build the element to be added
        // -- Here we do mark the element that is added
        // -- then we traverse up the tree to the root
        // -- clone from the root and return the clone
        // -- this is done so that the style sheets have
        // -- access to important information like the
        // -- document language and other locales
        // -- this is important for multilingual editing
        // --
        // -- Note that the metadata-embedded.xsl stylesheet
        // -- only applies the templating to the added element, not to
        // -- the entire metadata so performance should not be a big issue
        Element elResp = editUtils.addElementEmbedded(String.valueOf(metadata.getId()), ref, name, child);
        EditLib.tagForDisplay(elResp);
        Element md = (Element) Xml.findRoot(elResp).clone();
        EditLib.removeDisplayTag(elResp);

        // TODO buildEditorForm(allRequestParams.get("currTab"), httpSession, allRequestParams, request, md,
        //      metadata.getDataInfo().getSchemaId(), context, applicationContext, true, true, response);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Delete element", description = "")
    @DeleteMapping(
            value = "/{metadataUuid}/editor/elements",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE})
    //  @PreAuthorize("hasAuthority('Editor')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Element removed.",
                        content = {@Content(schema = @Schema(hidden = true))}),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT)
            })
    @ResponseBody
    public void deleteElement(
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String metadataUuid,
            @Parameter(description = "Reference of the element to remove.", required = true) @RequestParam String[] ref,
            @Parameter(description = "Name of the parent.", required = true) @RequestParam String parent,
            @Parameter(description = "Should attributes be shown on the editor snippet?", required = false)
                    @RequestParam(defaultValue = "false")
                    boolean displayAttributes,
            HttpServletRequest request,
            @Parameter(hidden = true) HttpSession httpSession)
            throws Exception {
        Metadata metadata = accessManager.canEditRecord(metadataUuid, request);
        String id = String.valueOf(metadata.getId());

        for (String s : ref) {
            editUtils.deleteElementEmbedded(id, s, parent);
        }
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Delete attribute", description = "")
    @DeleteMapping(
            value = "/{metadataUuid}/editor/attributes",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE})
    //  @PreAuthorize("hasAuthority('Editor')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Attribute removed.",
                        content = {@Content(schema = @Schema(hidden = true))}),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT)
            })
    @ResponseBody
    public void deleteAttribute(
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String metadataUuid,
            @Parameter(description = "Reference of the attribute to remove.", required = true) @RequestParam String ref,
            @Parameter(description = "Should attributes be shown on the editor snippet?", required = false)
                    @RequestParam(defaultValue = "false")
                    boolean displayAttributes,
            HttpServletRequest request,
            @Parameter(hidden = true) HttpSession httpSession)
            throws Exception {
        Metadata metadata = accessManager.canEditRecord(metadataUuid, request);
        editUtils.deleteAttributeEmbedded(String.valueOf(metadata.getId()), ref);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Reorder element", description = "")
    @PutMapping(
            value = "/{metadataUuid}/editor/elements/{direction}",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE})
    //  @PreAuthorize("hasAuthority('Editor')")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Element reordered."),
                @ApiResponse(responseCode = "403", description = ApiParams.API_RESPONSE_NOT_ALLOWED_CAN_EDIT)
            })
    @ResponseBody
    public void reorderElement(
            @Parameter(description = API_PARAM_RECORD_UUID, required = true) @PathVariable String metadataUuid,
            @Parameter(description = "Reference of the element to move.", required = true) @RequestParam String ref,
            @Parameter(description = "Direction", required = true) @PathVariable Direction direction,
            @Parameter(description = "Should attributes be shown on the editor snippet?", required = false)
                    @RequestParam(defaultValue = "false")
                    boolean displayAttributes,
            @Parameter(hidden = true) @RequestParam Map<String, String> allRequestParams,
            HttpServletRequest request,
            @Parameter(hidden = true) HttpSession httpSession)
            throws Exception {

        Metadata metadata = accessManager.canEditRecord(metadataUuid, request);
        editUtils.swapElementEmbedded(String.valueOf(metadata.getId()), ref, direction == Direction.down);
    }
}
