/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.metadata.datadir;

import java.nio.file.Path;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.metadata.MetadataNotFoundException;
import org.springframework.util.StringUtils;

public class UuidMetadataDirPathProcessor implements IMetadataDirProcessor {
    private final MetadataRepository metadataRepository;
    private final MetadataDirPrivileges metadataDirectoryPrivileges;

    public UuidMetadataDirPathProcessor(
            MetadataDirPrivileges metadataDirectoryPrivileges, MetadataRepository metadataRepository) {
        this.metadataDirectoryPrivileges = metadataDirectoryPrivileges;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public Path calculatePath(Path dataDir, int metadataId) throws MetadataNotFoundException {
        String uuid = metadataRepository.findUuidById(metadataId);
        if (!StringUtils.hasLength(uuid)) {
            throw new MetadataNotFoundException("Metadata with id " + metadataId + " not found");
        }

        return dataDir.resolve(uuid);
    }

    @Override
    public Path calculatePathWithAccess(Path dataDir, String access, int metadataId) throws MetadataNotFoundException {
        if (metadataDirectoryPrivileges.equals(MetadataDirPrivileges.DEFAULT)) {
            return calculatePath(dataDir, metadataId).resolve(access);
        } else {
            return calculatePath(dataDir, metadataId);
        }
    }
}
