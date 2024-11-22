/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.constants.Edit;
import org.geonetwork.constants.Geonet;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.indexing.IndexingMode;
import org.geonetwork.schemas.SchemaManager;
import org.geonetwork.utility.date.ISODate;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class MetadataManager {
    SchemaManager schemaManager;

    MetadataXmlSerializer xmlSerializer;

    EditLib editLib;

    MetadataRepository metadataRepository;

    /** Compared to GN4, removed language (used for validation) */
    public Metadata update(
            String metadataId,
            Element metadataXml,
            boolean validate,
            boolean ufo,
            String changeDate,
            boolean updateDateStamp,
            IndexingMode indexingMode) {
        log.trace("Update record with id {}", metadataId);

        // when invoked from harvesters, session is null?
        //     TODO UserSession session = context.getUserSession();
        //      if (session != null) {
        //        session.removeProperty(Geonet.Session.VALIDATION_REPORT + metadataId);
        //      }

        Metadata metadataEntity =
                metadataRepository.findById(Integer.parseInt(metadataId)).get();
        String schema = metadataEntity.getSchemaid();

        if (updateDateStamp) {
            if (StringUtils.isEmpty(changeDate)) {
                changeDate = new ISODate().toString();
                metadataEntity.setChangedate(new ISODate().getDateAndTimeUtc());
            } else {
                metadataEntity.setChangedate(new ISODate(changeDate).getDateAndTimeUtc());
            }
        }

        String uuidBeforeUfo = null;
        if (ufo) {
            // TODO: uuidBeforeUfo = findUuid(metadataXml, schema, metadata);
            uuidBeforeUfo = metadataEntity.getUuid();

            metadataXml = updateFixedInfo(
                    schema,
                    Optional.of(metadataEntity.getId()),
                    uuidBeforeUfo,
                    metadataXml,
                    (updateDateStamp ? UpdateDatestamp.YES : UpdateDatestamp.NO));
        }

        // --- force namespace prefix for iso19139 metadata
        // TODO setNamespacePrefixUsingSchemas(schema, metadataXml);

        // TODO String uuid = findUuid(metadataXml, schema, metadataEntity);
        String uuid = metadataEntity.getUuid();

        // TODO metadataUtils.checkMetadataWithSameUuidExist(uuid, metadataEntity.getId());

        // --- write metadata to dbms
        xmlSerializer.update(metadataId, uuid, metadataXml, changeDate, updateDateStamp);

        //      try {
        // --- do the validation last - it throws exceptions
        // TODO
        //  if (session != null && validate) {
        //          metadataValidator.doValidate(session, schema, metadataId, metadataXml, lang, false);
        //        }
        //      } finally {
        //        TODO if (indexingMode != IndexingMode.none) {
        //          // Delete old record if UUID changed
        //          if (uuidBeforeUfo != null && !uuidBeforeUfo.equals(uuid)) {
        //            getSearchManager().delete(String.format("+uuid:\"%s\"", uuidBeforeUfo));
        //          }
        //          metadataIndexer.indexMetadata(metadataId, true, indexingMode);
        //        }
        //      }

        //		  TODO: TODOES Searhc for related records with an XLink pointing to this subtemplate
        //        if (metadata.getDataInfo().getType() == MetadataType.SUB_TEMPLATE) {
        //            MetaSearcher searcher = searcherForReferencingMetadata(context, metadata);
        //            Map<Integer, AbstractMetadata> result = ((LuceneSearcher) searcher).getAllMdInfo(context, 500);
        //            for (Integer id : result.keySet()) {
        //                IndexingList list = context.getBean(IndexingList.class);
        //                list.add(id);
        //            }
        //        }

        log.trace("Finishing update of record with id {}", metadataId);
        // Return an up to date metadata record
        return metadataEntity;
    }

    public Element getMetadataDocumentForEditing(String id, boolean withValidationErrors, boolean keepXlinkAttributes)
            throws Exception {
        //    Element metadataXml = getXmlSerializer().selectNoXLinkResolver(id, false, applyOperationsFilters);
        Element metadataXml = xmlSerializer.select(id);

        String version = null;

        //  TODO?  if (doXLinks)
        //      Processor.processXLink(metadataXml, srvContext);

        Metadata metadataEntity =
                metadataRepository.findById(Integer.parseInt(id)).get();
        String schema = metadataEntity.getSchemaid();

        // Inflate metadata
        Path inflateStyleSheet = schemaManager.getSchema(schema).getSchemaDir().resolve(Geonet.File.INFLATE_METADATA);
        if (Files.exists(inflateStyleSheet)) {
            // --- setup environment
            Element env = new Element("env");
            // TODO      env.addContent(new Element("lang").setText(srvContext.getLanguage()));

            // add original metadata to result
            Element result = new Element("root");
            result.addContent(metadataXml);
            result.addContent(env);

            metadataXml = Xml.transformJdomElement(result, inflateStyleSheet.toFile(), new HashMap<>());
        }

        //    TODO if (withEditorValidationErrors) {
        //      final Pair<Element, String> versionAndReport = metadataValidator
        //        .doValidate(srvContext.getUserSession(), schema, id, metadataXml, srvContext.getLanguage(),
        // forEditing);
        //      version = versionAndReport.two();
        //      // Add the validation report to the record
        //      // under a geonet:report element. The report
        //      // contains both XSD and schematron errors.
        //      // The report is used when building the editor form
        //      // to display errors related to elements.
        //      metadataXml.addContent(versionAndReport.one());
        //    } else {
        editLib.expandElements(schema, metadataXml);
        version = editLib.getVersionForEditing(schema, id, metadataXml);
        //    }

        metadataXml.addNamespaceDeclaration(Edit.NAMESPACE);
        Element info = buildInfoElem(id, version);
        metadataXml.addContent(info);

        metadataXml.detach();
        return metadataXml;
    }

    public Element updateFixedInfo(
            String schema, Optional<Integer> integer, String uuid, Element md, UpdateDatestamp updateDatestamp) {
        return md;
    }

    /** buildInfoElem contains similar portion of code with indexMetadata */
    private Element buildInfoElem(String id, String version) throws Exception {
        Metadata metadata = metadataRepository.findById(Integer.parseInt(id)).get();
        String schema = metadata.getSchemaid();
        String createDate = metadata.getCreatedate();
        String changeDate = metadata.getChangedate();
        String source = metadata.getSource();
        String isTemplate = metadata.getIstemplate();
        String uuid = metadata.getUuid();
        String isHarvested = metadata.getIsharvested();
        // TODO String harvestUuid = metadata.getHarvestInfo().getUuid();
        String popularity = "" + metadata.getPopularity();
        String rating = "" + metadata.getRating();
        String owner = "" + metadata.getOwner();
        Integer groupOwner = metadata.getGroupowner();
        String displayOrder = "" + metadata.getDisplayorder();

        Element info = new Element(Edit.RootChild.INFO, Edit.NAMESPACE);

        addElement(info, Edit.Info.Elem.ID, id);
        addElement(info, Edit.Info.Elem.SCHEMA, schema);
        addElement(info, Edit.Info.Elem.CREATE_DATE, createDate);
        addElement(info, Edit.Info.Elem.CHANGE_DATE, changeDate);
        addElement(info, Edit.Info.Elem.IS_TEMPLATE, isTemplate);
        addElement(info, Edit.Info.Elem.SOURCE, source);
        addElement(info, Edit.Info.Elem.UUID, uuid);
        addElement(info, Edit.Info.Elem.IS_HARVESTED, isHarvested);
        addElement(info, Edit.Info.Elem.POPULARITY, popularity);
        addElement(info, Edit.Info.Elem.RATING, rating);
        addElement(info, Edit.Info.Elem.DISPLAY_ORDER, displayOrder);

        //    TODO if (metadata.getHarvestInfo().isHarvested()) {
        //      if (harvestInfoProvider != null) {
        //        info.addContent(harvestInfoProvider.getHarvestInfo(harvestUuid, id, uuid));
        //      }
        //    }
        if (version != null) {
            addElement(info, Edit.Info.Elem.VERSION, version);
        }

        //  TODO   Map<String, Element> map = Maps.newHashMap();
        //    map.put(id, info);
        //    buildPrivilegesMetadataInfo(context, map);

        // add owner name
        //    java.util.Optional<User> user = userRepository.findById(Integer.parseInt(owner));
        //    if (user.isPresent()) {
        //      addElement(info, Edit.Info.Elem.OWNERID, user.get().getId());
        //      String ownerName = user.get().getName();
        //      addElement(info, Edit.Info.Elem.OWNERNAME, ownerName);
        //    }
        //
        //    // add groupowner name
        //    if (groupOwner != null) {
        //      java.util.Optional<Group> group = groupRepository.findById(groupOwner);
        //      if (group.isPresent()) {
        //        String groupOwnerName = group.get().getName();
        //        addElement(info, Edit.Info.Elem.GROUPOWNERNAME, groupOwnerName);
        //      }
        //    }
        //
        //    for (MetadataCategory category : metadata.getCategories()) {
        //      addElement(info, Edit.Info.Elem.CATEGORY, category.getName());
        //    }
        // Add validity information
        //    List<MetadataValidation> validationInfo =
        // metadataValidationRepository.findAllById_MetadataId(Integer.parseInt(id));
        //    if (validationInfo == null || validationInfo.size() == 0) {
        //      addElement(info, Edit.Info.Elem.VALID, "-1");
        //    } else {
        //      String isValid = "1";
        //      for (Object elem : validationInfo) {
        //        MetadataValidation vi = (MetadataValidation) elem;
        //        String type = vi.getId().getValidationType();
        //        if (!vi.isValid()) {
        //          isValid = "0";
        //        }
        //
        //        String ratio = "xsd".equals(type) ? "" : vi.getNumFailures() + "/" + vi.getNumTests();
        //
        //        info.addContent(new Element(Edit.Info.Elem.VALID + "_details").addContent(new
        // Element("type").setText(type))
        //          .addContent(new Element("status").setText(vi.isValid() ? "1" : "0").addContent(new
        // Element("ratio").setText(ratio))));
        //      }
        //      addElement(info, Edit.Info.Elem.VALID, isValid);
        //    }
        //
        //    // add baseUrl of this site (from settings)
        //    SettingInfo si = new SettingInfo();
        //    addElement(info, Edit.Info.Elem.BASEURL, si.getSiteUrl() + context.getBaseUrl());
        //    addElement(info, Edit.Info.Elem.LOCSERV, "/srv/en");
        return info;
    }

    protected static void addElement(Element root, String name, Object value) {
        root.addContent(new Element(name).setText(value == null ? "" : value.toString()));
    }
}
