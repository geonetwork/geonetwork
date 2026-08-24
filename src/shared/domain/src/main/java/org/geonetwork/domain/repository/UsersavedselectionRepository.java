/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.repository;

import org.geonetwork.domain.Usersavedselection;
import org.geonetwork.domain.UsersavedselectionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersavedselectionRepository extends JpaRepository<Usersavedselection, UsersavedselectionId> {
    Integer countByIdMetadatauuidAndIdSelectionid(String metadataUuid, int selectionId);
}
