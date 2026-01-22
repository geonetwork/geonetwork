/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import java.util.Optional;
import org.geonetwork.domain.Metadatastatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadatastatusRepository extends JpaRepository<Metadatastatus, Integer> {

    Optional<Metadatastatus> findFirstByMetadataidAndStatusidTypeOrderByChangedateDesc(Integer metadataId, String type);
}
