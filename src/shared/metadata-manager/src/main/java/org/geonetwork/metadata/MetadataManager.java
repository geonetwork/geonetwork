/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.metadata;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.Operation;
import org.geonetwork.domain.Operationallowed;
import org.geonetwork.domain.ReservedOperation;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.domain.repository.OperationRepository;
import org.geonetwork.domain.repository.OperationallowedRepository;
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
            if (StringUtils.hasText(changeDate)) {
                // TODO changeDate = new ISODate().toString();
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

    public Element updateFixedInfo(
            String schema, Optional<Integer> integer, String uuid, Element md, boolean updateDatestamp) {
        // TODO
        return md;
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
