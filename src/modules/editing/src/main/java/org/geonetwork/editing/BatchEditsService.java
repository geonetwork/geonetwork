/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing;

import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.StringSubstitutor;
import org.geonetwork.domain.Metadata;
import org.geonetwork.editing.model.AddElemValue;
import org.geonetwork.editing.model.BatchEditParameter;
import org.geonetwork.metadata.MetadataAccessManager;
import org.geonetwork.metadata.MetadataManager;
import org.geonetwork.schemas.MetadataSchema;
import org.geonetwork.schemas.SchemaManager;
import org.geonetwork.utility.json.JsonUtil;
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

    private final SchemaConfiguration schemaConfiguration;

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
        boolean createXpathNodeIfNotExists = true;

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
                            buildBatchParameterForSchema(batchEditParameter, metadataSchema);

                            AddElemValue propertyValue = new AddElemValue(batchEditParameter.getValue());

                            metadataChanged = editLib.addElementOrFragmentFromXpath(
                                            metadata,
                                            metadataSchema,
                                            batchEditParameter.getXpath(),
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

    /**
     * If batch edit parameter is defined by property, set xpath to the corresponding value in the schema (see
     * application-schemas.yml).
     */
    private void buildBatchParameterForSchema(BatchEditParameter batchEditParameter, MetadataSchema metadataSchema) {
        if (StringUtils.isNotBlank(batchEditParameter.getXpath())) {
            // Xpath defined, nothing to do
        } else if (StringUtils.isNotEmpty(batchEditParameter.getProperty())) {
            SchemaConfiguration.Schema.Property propertyConfig =
                    getPropertyConfig(metadataSchema.getName(), batchEditParameter.getProperty());
            batchEditParameter.setXpath(propertyConfig.getXpath());

            String value = batchEditParameter.getValue();
            Map<String, String> replacements = buildReplacements(value, propertyConfig);
            batchEditParameter.setValue(buildWithPropertySubstitutions(propertyConfig.getValue(), replacements));
        } else {
            throw new IllegalArgumentException("Either xpath or property must be defined.");
        }
    }

    /**
     * Build a map of replacements for the property value.
     *
     * <p>First, extract all keys contains in the XML in between {{key:defaultValue}}, eg.
     *
     * <pre>
     *     <gml:beginPosition>{{start.date:}}</gml:beginPosition>
     * </pre>
     *
     * will extract `start.date` as key.
     *
     * <p>Then, for each key, check if a JSONPath match in the valueOrJson parameter.
     *
     * <p>Then return the list of replacement for each key with the value from the valueOrJson parameter.
     */
    private Map<String, String> buildReplacements(String valueOrJson, SchemaConfiguration.Schema.Property config) {
        Map<String, String> replacements = new HashMap<>();
        Set<String> keys = new HashSet<>();
        String xml = config.getValue();
        String name = config.getName();
        if (JsonUtil.isValidJsonObject(valueOrJson)) {
            // Extract all keys contains in the XML in between {{key:-defaultValue}}
            String regex = "\\{\\{([^:]+):-?([^}]+)\\}\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(xml);
            while (matcher.find()) {
                String key = matcher.group(1);
                keys.add(key);
            }
            // For each key, check if a JSONPath match in the valueOrJson parameter
            for (String key : keys) {
                Object value = JsonPath.read(valueOrJson, "$." + key);
                if (value != null) {
                    replacements.put(key, String.valueOf(value));
                }
            }
        } else {
            replacements.put(name, valueOrJson);
        }
        return replacements;
    }

    private SchemaConfiguration.Schema.Property getPropertyConfig(String schemaIdentifier, String property) {
        Optional<SchemaConfiguration.Schema> schema = schemaConfiguration.getSchema(schemaIdentifier);
        if (schema.isEmpty()) {
            throw new IllegalArgumentException(String.format("Schema %s not found in configuration", schemaIdentifier));
        }
        Optional<SchemaConfiguration.Schema.Property> field = schema.get().getProperties().stream()
                .filter(p -> p.getName().equals(property))
                .findFirst();
        if (field.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Property %s not found in schema %s", property, schemaIdentifier));
        }
        return field.get();
    }

    public static String buildWithPropertySubstitutions(String template, Map<String, String> replacements) {
        replacements.forEach((key, value) -> replacements.put(key, StringEscapeUtils.escapeXml11(value)));
        StringSubstitutor sub = new StringSubstitutor(replacements, "{{", "}}");
        return sub.replace(template);
    }
}
