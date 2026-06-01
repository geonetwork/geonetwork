/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import java.util.Optional;
import org.geonetwork.domain.Selection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectionRepository extends JpaRepository<Selection, Integer> {
    Optional<Selection> findOneByName(String name);
}
