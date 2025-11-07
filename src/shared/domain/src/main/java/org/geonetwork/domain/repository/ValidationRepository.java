/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import java.util.List;
import org.geonetwork.domain.Validation;
import org.geonetwork.domain.ValidationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationRepository extends JpaRepository<Validation, ValidationId> {
    List<Validation> findAllById_metadataid(Integer metadataId);
}
