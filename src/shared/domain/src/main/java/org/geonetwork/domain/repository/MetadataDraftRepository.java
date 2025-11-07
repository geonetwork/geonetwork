/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import org.geonetwork.domain.MetadataDraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataDraftRepository extends JpaRepository<MetadataDraft, Integer> {
    MetadataDraft findByApprovedversion_Id(Integer id);

    boolean existsByApprovedversion_Id(Integer id);
}
