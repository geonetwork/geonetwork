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
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.Operation;
import org.geonetwork.domain.Operationallowed;
import org.geonetwork.domain.ReservedOperation;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.domain.repository.OperationRepository;
import org.geonetwork.domain.repository.OperationallowedRepository;
import org.springframework.stereotype.Service;

@Service
public class MetadataManager implements IMetadataManager {
    private final MetadataRepository metadataRepository;
    private final OperationRepository operationRepository;
    private final OperationallowedRepository operationallowedRepository;

    MetadataManager(
            final MetadataRepository metadataRepository,
            final OperationRepository operationRepository,
            final OperationallowedRepository operationallowedRepository) {
        this.metadataRepository = metadataRepository;
        this.operationRepository = operationRepository;
        this.operationallowedRepository = operationallowedRepository;
    }

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
