/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import java.util.List;
import org.geonetwork.domain.Operationallowed;
import org.geonetwork.domain.OperationallowedId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationallowedRepository extends JpaRepository<Operationallowed, OperationallowedId> {
    List<Operationallowed> findAllByIdMetadataid(Integer metadataId);
}
