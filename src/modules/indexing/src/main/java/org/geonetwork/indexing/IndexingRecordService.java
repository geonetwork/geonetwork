/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.indexing;

import static java.util.stream.Collectors.groupingBy;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.OP_PREFIX;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.VALID;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.Group;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.MetadataValidationStatus;
import org.geonetwork.domain.Metadatastatus;
import org.geonetwork.domain.Operationallowed;
import org.geonetwork.domain.ReservedGroup;
import org.geonetwork.domain.ReservedOperation;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Validation;
import org.geonetwork.domain.repository.GroupRepository;
import org.geonetwork.domain.repository.GufUserfeedbackRepository;
import org.geonetwork.domain.repository.MetadataDraftRepository;
import org.geonetwork.domain.repository.MetadatacategRepository;
import org.geonetwork.domain.repository.MetadatastatusRepository;
import org.geonetwork.domain.repository.OperationallowedRepository;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsersavedselectionRepository;
import org.geonetwork.domain.repository.ValidationRepository;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.index.model.record.IndexRecordFieldNames;
import org.geonetwork.index.model.record.IndexRecords;
import org.geonetwork.utility.xml.XsltUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Indexing service.
 *
 * <p>Create index document from the database entity. The index document is created by collecting all properties from
 * the database entity. Then XSLT transformation is applied to the index document (grouped by schema). If the schema
 * starts with iso19 then a default iso.xsl conversion is applied.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class IndexingRecordService {

    private final MetadataDraftRepository metadataDraftRepository;
    private final GufUserfeedbackRepository userfeedbackRepository;
    private final UserRepository userRepository;
    private final MetadatastatusRepository metadatastatusRepository;
    private final GroupRepository groupRepository;
    private final OperationallowedRepository operationallowedRepository;
    private final MetadatacategRepository metadatacategRepository;
    private final ValidationRepository validationRepository;
    private final UsersavedselectionRepository usersavedselectionRepository;

    /** Build index documents from the database metadata. */
    public IndexRecords buildIndexDocuments(List<Metadata> databaseMetadata) {
        IndexRecords indexRecords = new IndexRecords();
        databaseMetadata.stream().collect(groupingBy(Metadata::getSchemaid)).forEach((schema, records) -> indexRecords
                .getIndexRecord()
                .addAll(collectProperties(schema, records).getIndexRecord()));
        return indexRecords;
    }

    /** Collect properties from the database metadata and apply XSLT to extract fields from XML document.. */
    public IndexRecords collectProperties(String schema, List<Metadata> schemaRecords) {
        String indexingXsltFileName = String.format("indexing/xslt/%s.xsl", schema);
        URL indexingXsltFile = null;
        try {
            indexingXsltFile = new ClassPathResource(indexingXsltFileName).getURL();
        } catch (IOException ioSchemaFileException) {
            if (schema.startsWith("iso19")) {
                try {
                    indexingXsltFile = new ClassPathResource("xslt/indexing/iso.xsl").getURL();
                } catch (IOException isoFileException) {
                    // Should not happen
                }
            }
            //      report.setNumberOfRecordsWithUnsupportedSchema(schemaRecords.size());
            log.atError()
                    .log(
                            "Schema {} used by records {} does not exist or does not provide indexing file {}",
                            schema,
                            schemaRecords.stream()
                                    .map(Metadata::getUuid)
                                    .map(Objects::toString)
                                    .collect(Collectors.joining(",")),
                            indexingXsltFileName);

            //      if (!schemaManager.existsSchema(schema)) {
            //        fields.put(IndexFields.DRAFT, "n");
            //        fields.put(IndexFields.INDEXING_ERROR_FIELD, true);
            //        fields.put(IndexFields.INDEXING_ERROR_MSG,
            //
            // searchManager.createIndexingErrorMsgObject("indexingErrorMsg-schemaNotRegistered",
            //            "error",
            //            Map.of("record", metadataId, "schema", schema)));
            //        searchManager.index(null, md, indexKey, fields, metadataType,
            //          forceRefreshReaders, indexingMode);
            //        Log.error(Geonet.DATA_MANAGER, String.format(
            //          "Record %s / Schema '%s' is not registered in this catalog. Install it or
            // remove
            // those records. Record is indexed indexing error flag.",
            //          metadataId, schema));
            //      }
        }

        if (indexingXsltFile != null) {
            IndexRecords.IndexRecordsBuilder indexRecordsBuilder = IndexRecords.builder();

            schemaRecords.stream().map(this::collectDbProperties).forEach(indexRecordsBuilder::indexRecord);
            IndexRecords records = indexRecordsBuilder.build();

            log.atTrace()
                    .log(XsltUtil.transformObjectToString(
                            records, indexingXsltFile, IndexRecords.class, new HashMap<>()));

            return XsltUtil.transformObjectToObject(records, indexingXsltFile, IndexRecords.class, new HashMap<>());
        }
        return null;
    }

    /**
     * Initialize an {@link IndexRecord} with all properties from the database of an {@link Metadata} and return its XML
     * representation as string.
     */
    protected IndexRecord collectDbProperties(Metadata r) {
        IndexRecord.IndexRecordBuilder indexRecord = IndexRecord.builder()
                .id(r.getId())
                .uuid(r.getUuid())
                .documentStandard(r.getSchemaid())
                .isTemplate(r.getIstemplate().charAt(0))
                .harvested(r.getIsharvested().equals("y"))
                .popularity(r.getPopularity())
                .rating(r.getRating())
                .changeDate(r.getChangedate())
                .createDate(r.getCreatedate())
                .groupOwner(r.getGroupowner())
                .sourceCatalogue(r.getSource())
                .displayOrder(r.getDisplayorder())
                .document(r.getData())
                .draft("n");

        boolean hasDraft = metadataDraftRepository.existsByApprovedversion_Id(r.getId());
        if (hasDraft) {
            // TODO:
            //  indexKey += "-draft";
            //   draft field e exists / y is a draft
            log.atInfo().log(String.format("Record %s has a draft. TODO", r.getId()));
        }

        setOwnerFields(r, indexRecord);
        setCategoryFields(r, indexRecord);
        setPrivilegesFields(r, indexRecord);
        setStatusFields(r, indexRecord);
        setPopularityFields(r, indexRecord);
        processValidationInfo(validationRepository.findAllById_metadataid(r.getId()), indexRecord);

        return indexRecord.build();
    }

    private void setPopularityFields(Metadata r, IndexRecord.IndexRecordBuilder indexRecord) {
        indexRecord.feedbackCount(this.userfeedbackRepository.countByMetadataUuid_Uuid(r.getUuid()));
        int preferredRecordSelectionId = 0;
        indexRecord.userSavedCount(this.usersavedselectionRepository.countByIdMetadatauuidAndIdSelectionid(
                r.getUuid(), preferredRecordSelectionId));
    }

    private void setStatusFields(Metadata r, IndexRecord.IndexRecordBuilder indexRecord) {
        Optional<Metadatastatus> lastWorkflowStatus =
                this.metadatastatusRepository.findFirstByMetadataidAndStatusidTypeOrderByChangedateDesc(
                        r.getId(), "workflow");
        lastWorkflowStatus.ifPresent(metadatastatus -> indexRecord.lastWorkflowStatus(
                String.valueOf(metadatastatus.getStatusid().getId())));
    }

    /** Validation status -1 : not evaluated 0 : invalid 1 : valid. */
    private void processValidationInfo(List<Validation> validationInfo, IndexRecord.IndexRecordBuilder indexRecord) {
        if (validationInfo.isEmpty()) {
            indexRecord.valid(-1);
            return;
        }

        int isValid = determineValidationStatus(validationInfo);
        indexRecord.valid(isValid);

        Map<String, String> validationStatus = new HashMap<>();
        validationInfo.stream()
                .filter(v -> !v.getId().getValtype().equalsIgnoreCase(IndexRecordFieldNames.INSPIRE))
                .forEach(vi ->
                        validationStatus.put(VALID + "_" + vi.getId().getValtype(), String.valueOf(vi.getStatus())));
        indexRecord.validationByType(validationStatus);

        boolean hasInspireValidation = processInspireValidation(validationInfo, indexRecord);
        if (!hasInspireValidation) {
            indexRecord.inspireValid(-1);
        }
    }

    private int determineValidationStatus(List<Validation> validationInfo) {
        int isValid = 1;
        for (Validation vi : validationInfo) {
            if (!vi.getId().getValtype().equalsIgnoreCase(IndexRecordFieldNames.INSPIRE)) {
                if (vi.getStatus() == MetadataValidationStatus.NEVER_CALCULATED.getCode() && vi.getRequired()) {
                    return -1;
                }
                if ((vi.getStatus() == MetadataValidationStatus.INVALID.getCode()) && vi.getRequired()) {
                    isValid = 0;
                }
            }
        }
        return isValid;
    }

    private boolean processInspireValidation(
            List<Validation> validationInfo, IndexRecord.IndexRecordBuilder indexRecord) {
        for (Validation vi : validationInfo) {
            if (vi.getId().getValtype().equalsIgnoreCase(IndexRecordFieldNames.INSPIRE)) {
                indexRecord.inspireReportUrl(vi.getReporturl());
                indexRecord.inspireValidationDate(vi.getValdate());
                indexRecord.validation(VALID + "_" + vi.getId().getValtype(), String.valueOf(vi.getStatus()));
                return true;
            }
        }
        return false;
    }

    private void setPrivilegesFields(Metadata r, IndexRecord.IndexRecordBuilder indexRecord) {
        List<Operationallowed> operationsAllowed = operationallowedRepository.findAllByIdMetadataid(r.getId());
        boolean isPublishedToAll = false;
        boolean isPublishedToIntranet = false;
        boolean isPublishedToGuest = false;

        Map<String, ArrayList<Integer>> privileges = new HashMap<>();

        for (Operationallowed operationAllowed : operationsAllowed) {
            int groupId = operationAllowed.getId().getGroupid();
            int operationId = operationAllowed.getId().getOperationid();
            String fieldName = OP_PREFIX + operationId;

            ArrayList<Integer> operationField = privileges.computeIfAbsent(fieldName, k -> new ArrayList<>());
            operationField.add(groupId);

            if (operationId == ReservedOperation.view.getId()) {
                Optional<Group> g = groupRepository.findById(groupId);
                if (g.isPresent()) {
                    indexRecord.groupPublished(g.get().getName());
                    indexRecord.groupPublishedId(g.get().getId());

                    if (g.get().getId() == ReservedGroup.all.getId()) {
                        isPublishedToAll = true;
                    } else if (g.get().getId() == ReservedGroup.intranet.getId()) {
                        isPublishedToIntranet = true;
                    } else if (g.get().getId() == ReservedGroup.guest.getId()) {
                        isPublishedToGuest = true;
                    }
                }
            }
        }
        indexRecord.operations(privileges);
        indexRecord.publishedToAll(isPublishedToAll);
        indexRecord.publishedToIntranet(isPublishedToIntranet);
        indexRecord.publishedToGuest(isPublishedToGuest);
    }

    private void setCategoryFields(Metadata r, IndexRecord.IndexRecordBuilder indexRecord) {
        this.metadatacategRepository.findAllByIdMetadataid(r.getId()).forEach(c -> {
            indexRecord.category(c.getCategoryid().getName());
        });
    }

    private void setOwnerFields(Metadata r, IndexRecord.IndexRecordBuilder indexRecord) {
        if (r.getOwner() != null) {
            Optional<User> userOpt = userRepository.findById(r.getOwner());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                indexRecord.userinfo(
                        user.getUsername() + "|" + user.getSurname() + "|" + user.getName() + "|" + user.getProfile());
                indexRecord.recordOwner(user.getName() + " " + user.getSurname());
            }
        }
    }
}
