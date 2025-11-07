/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import java.util.List;
import org.geonetwork.domain.Metadatacateg;
import org.geonetwork.domain.MetadatacategId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadatacategRepository extends JpaRepository<Metadatacateg, MetadatacategId> {
    List<Metadatacateg> findAllByIdMetadataid(Integer metadataId);
}
