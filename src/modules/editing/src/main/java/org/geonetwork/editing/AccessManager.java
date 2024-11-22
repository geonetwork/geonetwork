/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.utility.legacy.exceptions.ResourceNotFoundException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Component;

/** TODO: Migrate AccessManager and ApiUtils */
@Component
@Slf4j
@RequiredArgsConstructor
public class AccessManager {

    private final MetadataRepository metadataRepository;
    /** Check if the current user can edit this record. */
    public Metadata canEditRecord(String metadataUuid, HttpServletRequest request) throws Exception {
        Metadata metadata = getRecord(metadataUuid);
        //   TODO if (!accessManager.canEdit(createServiceContext(request), String.valueOf(metadata.getId()))) {
        //      throw new SecurityException(String.format(
        //        "You can't edit record with UUID %s", metadataUuid));
        //    }
        return metadata;
    }

    public Metadata getRecord(String uuidOrInternalId) throws ResourceNotFoundException {
        try {
            Optional<Metadata> metadata = metadataRepository.findOneByUuid(uuidOrInternalId);
            if (metadata.isPresent()) {
                return metadata.get();
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            log.warn("More than one record found with UUID '{}'. Error is '{}'.", uuidOrInternalId, e.getMessage());
        }

        try {
            log.trace("{} not recognized as UUID. Trying ID.", uuidOrInternalId);
            Optional<Metadata> metadata = metadataRepository.findById(Integer.parseInt(uuidOrInternalId));
            if (metadata.isPresent()) {
                return metadata.get();
            }
        } catch (NumberFormatException | InvalidDataAccessApiUsageException e) {
            // Ignored
        }

        log.trace("Record identified by {} not found.", uuidOrInternalId);
        throw new ResourceNotFoundException("Record with UUID '" + uuidOrInternalId + "' not found in this catalog");
    }
}
