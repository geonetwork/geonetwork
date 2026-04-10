/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import java.util.List;
import org.geonetwork.domain.Usersavedselection;
import org.geonetwork.domain.UsersavedselectionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersavedselectionRepository extends JpaRepository<Usersavedselection, UsersavedselectionId> {
    Integer countByIdMetadatauuidAndIdSelectionid(String metadataUuid, int selectionId);

    List<String> findMetadatauuidByIdSelectionidAndIdUserid(int selectionid, int userid);

    void deleteAllByIdSelectionid(int selectionid);

    void deleteAllByIdSelectionidAndIdUserid(int selectionid, int userid);
}
