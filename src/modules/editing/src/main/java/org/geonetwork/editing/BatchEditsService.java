/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.constants.Geonet;
import org.geonetwork.domain.Metadata;
import org.geonetwork.editing.model.AddElemValue;
import org.geonetwork.editing.model.BatchEditParameter;
import org.geonetwork.metadata.MetadataAccessManager;
import org.geonetwork.metadata.MetadataManager;
import org.geonetwork.schemas.MetadataSchema;
import org.geonetwork.schemas.SchemaManager;
import org.geonetwork.utility.legacy.Pair;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchEditsService {

    public static final String PREVIEW_RESPONSE_ROOT_ELEMENT_NAME = "preview";

    private final SchemaManager schemaManager;

    private final MetadataManager metadataManager;

    private final MetadataAccessManager metadataAccessManager;

    public Pair<Object, Element> applyBatchEdits(
            //    private Pair<SimpleMetadataProcessingReport, Element> applyBatchEdits(
            String[] uuids,
            String bucket,
            boolean updateDateStamp,
            BatchEditParameter[] edits,
            HttpServletRequest request,
            BatchEditMode batchEditMode)
            throws Exception {
        List<BatchEditParameter> listOfUpdates = Arrays.asList(edits);
        if (listOfUpdates.size() == 0) {
            throw new IllegalArgumentException("At least one edit must be defined.");
        }

        final Set<String> setOfUuidsToEdit = new HashSet<>();
        if (uuids == null) {
            throw new NotImplementedException("GN5 / SelectionManager is not implemented.");
            //    TODO:        SelectionManager selectionManager =
            //                SelectionManager.getManager(serviceContext.getUserSession());
            //
            //            synchronized (
            //                selectionManager.getSelection(bucket)) {
            //                final Set<String> selection = selectionManager.getSelection(bucket);
            //                setOfUuidsToEdit = Sets.newHashSet(selection);
            //            }
            //            setOfUuidsToEdit = new HashSet<>();
        } else {
            for (String uuid : uuids) {
                Metadata metadata = metadataManager.findMetadataByUuidOrId(uuid, true);
                if (metadata != null && metadataAccessManager.canEdit(metadata.getId())) {
                    setOfUuidsToEdit.add(metadata.getUuid());
                }
            }
        }

        if (setOfUuidsToEdit.isEmpty()) {
            throw new IllegalArgumentException("At least one record should be defined or selected for updates.");
        }

        //    ConfigurableApplicationContext appContext = ApplicationContextHolder.get();
        //    DataManager dataMan = appContext.getBean(DataManager.class);
        //    SchemaManager _schemaManager = context.getBean(SchemaManager.class);
        //    AccessManager accessMan = context.getBean(AccessManager.class);
        //    final String settingId =
        // Settings.SYSTEM_CSW_TRANSACTION_XPATH_UPDATE_CREATE_NEW_ELEMENTS;
        boolean createXpathNodeIfNotExists = false;

        //    SimpleMetadataProcessingReport report = new SimpleMetadataProcessingReport();
        //    report.setTotalRecords(setOfUuidsToEdit.size());
        //    UserSession userSession = ApiUtils.getUserSession(request.getSession());

        @SuppressWarnings("unused")
        String changeDate = null;
        Element preview = new Element(PREVIEW_RESPONSE_ROOT_ELEMENT_NAME);

        //    final IMetadataUtils metadataRepository = context.getBean(IMetadataUtils.class);
        for (String recordUuid : setOfUuidsToEdit) {
            Metadata record = metadataManager.findMetadataByUuid(recordUuid, false);
            if (record == null) {
                //        report.incrementNullRecords();
                //      } else if (!accessMan.isOwner(serviceContext,
                // String.valueOf(record.getId()))) {
                //        report.addNotEditableMetadataId(record.getId());
            } else {
                // Processing
                try {
                    EditLib editLib = new EditLib(schemaManager);
                    MetadataSchema metadataSchema = schemaManager.getSchema(record.getSchemaid());
                    Element metadata = Xml.loadString(record.getData(), false);
                    @SuppressWarnings("unused")
                    String original = Xml.getString(metadata);
                    boolean metadataChanged = false;

                    Iterator<BatchEditParameter> listOfUpdatesIterator = listOfUpdates.iterator();
                    while (listOfUpdatesIterator.hasNext()) {
                        BatchEditParameter batchEditParameter = listOfUpdatesIterator.next();

                        AddElemValue propertyValue = new AddElemValue(batchEditParameter.getValue());

                        boolean applyEdit = true;
                        if (StringUtils.isNotEmpty(batchEditParameter.getCondition())) {
                            applyEdit = false;
                            final Object node = Xml.selectSingle(
                                    metadata, batchEditParameter.getCondition(), metadataSchema.getNamespaces());
                            if (node instanceof Boolean && Boolean.TRUE.equals(node)) {
                                applyEdit = true;
                            }
                        }
                        if (applyEdit) {
                            metadataChanged = editLib.addElementOrFragmentFromXpath(
                                            metadata,
                                            metadataSchema,
                                            getXpath(batchEditParameter, metadataSchema),
                                            propertyValue,
                                            createXpathNodeIfNotExists)
                                    || metadataChanged;
                        }
                    }
                    if (batchEditMode == BatchEditMode.PREVIEW) {
                        //            if (diffType == null) {
                        preview.addContent(metadata);
                        //            } else {
                        //              preview.addContent(Diff.diff(original,
                        // Xml.getString(metadata),
                        // diffType));
                        //            }
                    } else if (metadataChanged) {
                        //            Element beforeMetadata =
                        //                dataMan.getMetadata(
                        //                    serviceContext, String.valueOf(record.getId()), false,
                        // false,
                        // false);

                        metadataManager.update(record.getId(), metadata, false, true, changeDate, false);
                        //            dataMan.updateMetadata(
                        //                serviceContext,
                        //                record.getId() + "",
                        //                metadata,
                        //                validate,
                        //                ufo,
                        //                "eng", // Not used when validate is false
                        //                changeDate,
                        //                uds,
                        //                IndexingMode.full);
                        //            report.addMetadataInfos(record, "Metadata updated.");

                        //            Element afterMetadata =
                        //                dataMan.getMetadata(
                        //                    serviceContext, String.valueOf(record.getId()), false,
                        // false,
                        // false);
                        //            XMLOutputter outp = new XMLOutputter();
                        //            String xmlBefore = outp.outputString(beforeMetadata);
                        //            String xmlAfter = outp.outputString(afterMetadata);
                        //            new RecordUpdatedEvent(
                        //                    record.getId(), userSession.getUserIdAsInt(),
                        // xmlBefore, xmlAfter)
                        //                .publish(appContext);
                    } else {
                        //            report.incrementUnchangedRecords();
                    }
                } catch (Exception e) {
                    log.error("Error processing record: " + recordUuid + e.getMessage());
                    //          report.addMetadataError(record, e);
                }
                //        report.incrementProcessedRecords();
            }
        }
        //    report.close();
        return Pair.write(new HashMap<>(), preview);
    }

    private String getXpath(BatchEditParameter batchEditParameter, MetadataSchema metadataSchema) {
        if (StringUtils.isNotEmpty(batchEditParameter.getXpath())) {
            return batchEditParameter.getXpath();
        } else if (StringUtils.isNotEmpty(batchEditParameter.getProperty())) {
            return getXpathForProperty(metadataSchema.getName(), batchEditParameter.getProperty());
        } else {
            throw new IllegalArgumentException("Either xpath or property must be defined.");
        }
    }

    // TODO - this is a temporary solution until we have a better way to map properties to xpaths
    private String getXpathForProperty(String schema, String property) {
        switch (property) {
            case Geonet.IndexFieldNames.RESOURCETITLE + "Object":
                switch (schema) {
                    case "iso19115-3.2018":
                        return "mdb:identificationInfo/mri:MD_DataIdentification/mri:citation/cit:CI_Citation/cit:title/gco:CharacterString";
                    case "iso19139":
                        return "gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString";
                }
                break;
            case Geonet.IndexFieldNames.RESOURCEABSTRACT + "Object":
                switch (schema) {
                    case "iso19115-3.2018":
                        return "mdb:identificationInfo/mri:MD_DataIdentification/mri:abstract/gco:CharacterString";
                    case "iso19139":
                        return "gmd:identificationInfo/gmd:MD_DataIdentification/gmd:abstract/gco:CharacterString";
                }
                break;
        }
        return "";
    }
}
