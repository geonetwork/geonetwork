/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.metadata;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.constants.Edit;
import org.geonetwork.constants.Geonet;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.Operation;
import org.geonetwork.domain.Operationallowed;
import org.geonetwork.domain.ReservedOperation;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.domain.repository.OperationRepository;
import org.geonetwork.domain.repository.OperationallowedRepository;
import org.geonetwork.metadata.editing.EditLib;
import org.geonetwork.schemas.SchemaManager;
import org.geonetwork.utility.date.ISODate;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@AllArgsConstructor
public class MetadataManager implements IMetadataManager {
    private final MetadataRepository metadataRepository;
    private final OperationRepository operationRepository;
    private final OperationallowedRepository operationallowedRepository;
    private final SchemaManager schemaManager;
    private final EditLib editLib;

    @Override
    public Metadata findMetadataById(int metadataId) throws MetadataNotFoundException {
        Optional<Metadata> metadataOpt = metadataRepository.findById(metadataId);

        if (metadataOpt.isPresent()) {
            return metadataOpt.get();
        } else {
            throw new MetadataNotFoundException(String.format("Metadata with identifier '%d' not found", metadataId));
        }
    }

    @Override
    public Metadata findMetadataByUuid(String uuid, boolean approved) throws MetadataNotFoundException {
        Optional<Metadata> metadataOpt = metadataRepository.findByUuid(uuid);

        if (metadataOpt.isPresent()) {
            return metadataOpt.get();
        } else {
            throw new MetadataNotFoundException(String.format("Metadata with uuid '%s' not found", uuid));
        }
    }

    @Override
    public Metadata update(
            int metadataId, Element xml, boolean validate, boolean ufo, String changeDate, boolean updateDateStamp)
            throws MetadataNotFoundException {
        Metadata metadataEntity = findMetadataById(metadataId);
        String schema = metadataEntity.getSchemaid();

        // TODO: Extract UUID from XML
        //  uuidBeforeUfo = findUuid(metadataXml, schema, metadata);
        // TODO: Check if already used?
        //  metadataUtils.checkMetadataWithSameUuidExist(uuid, metadataEntity.getId());
        // In this case, UFO is mandatory. Should we always use UFO?

        if (updateDateStamp) {
            if (StringUtils.isEmpty(changeDate)) {
                metadataEntity.setChangedate(new ISODate().getDateAndTimeUtc());
            } else {
                metadataEntity.setChangedate(new ISODate(changeDate).getDateAndTimeUtc());
            }
        }

        String uuidBeforeUfo = null;
        if (ufo) {
            // TODO: uuidBeforeUfo = findUuid(metadataXml, schema, metadata);
            uuidBeforeUfo = metadataEntity.getUuid();

            xml = updateFixedInfo(schema, Optional.of(metadataEntity.getId()), uuidBeforeUfo, xml, updateDateStamp);
        }

        // --- force namespace prefix for iso19139 metadata
        // TODO setNamespacePrefixUsingSchemas(schema, metadataXml);

        // --- write metadata to dbms
        metadataEntity.setData(Xml.getString(xml));

        //        TODO if (newUuid != null) {
        //            metadataEntity.setUuid(newUuid);
        //        }

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
        metadataRepository.save(metadataEntity);
        return metadataEntity;
    }

    public Element getMetadataDocumentForEditing(String id, boolean withValidationErrors, boolean keepXlinkAttributes)
            throws Exception {
        //    Element metadataXml = getXmlSerializer().selectNoXLinkResolver(id, false, applyOperationsFilters);
        Element metadataXml =
                Xml.loadString(this.findMetadataById(Integer.parseInt(id)).getData(), false);

        String version = null;

        //  TODO?  if (doXLinks)
        //      Processor.processXLink(metadataXml, srvContext);

        Metadata metadataEntity =
                metadataRepository.findById(Integer.parseInt(id)).get();
        String schema = metadataEntity.getSchemaid();

        // Inflate metadata
        Path inflateStyleSheet = schemaManager
                .getSchema(schema)
                .getMetadataSchema()
                .getSchemaDir()
                .resolve(Geonet.File.INFLATE_METADATA);
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
            String schema, Optional<Integer> integer, String uuid, Element md, boolean updateDatestamp) {
        // TODO
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

    @Override
    public List<Operation> getAvailableMetadataOperations() {
        return this.operationRepository.findAll();
    }

    @Override
    public List<Operationallowed> getMetadataOperations(int metadataId) {
        return operationallowedRepository.findAllByIdMetadataid(metadataId);
    }

    @Override
    public Path getMetadataDir(Path metadataDataDirectory, int metadataId) {
        String group = pad(metadataId / 100, 3);
        String groupDir = group + "00-" + group + "99";
        return metadataDataDirectory.resolve(groupDir).resolve(String.valueOf(metadataId));
    }

    @Override
    public Path getMetadataDir(Path metadataDataDirectory, String access, int metadataId) {
        Path metadataDir = getMetadataDir(metadataDataDirectory, metadataId);

        // TODO: Create Enum
        String subDir = "public".equals(access) ? "public" : "private";
        return metadataDir.resolve(subDir);
    }

    @Override
    public List<Integer> getEditableGroups(int metadataId) {
        List<Operationallowed> operationalloweds = getMetadataOperations(metadataId);

        return operationalloweds.stream()
                .filter(operationallowed ->
                        operationallowed.getId().getOperationid().equals(ReservedOperation.editing.getId()))
                .map(operationallowed -> operationallowed.getId().getGroupid())
                .collect(Collectors.toList());
    }

    private String pad(int group, int length) {
        String text = Integer.toString(group);

        while (text.length() < length) text = "0" + text;

        return text;
    }
}
