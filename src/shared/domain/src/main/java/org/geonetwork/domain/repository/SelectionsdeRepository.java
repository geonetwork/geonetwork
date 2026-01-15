/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import org.geonetwork.domain.Selectionsde;
import org.geonetwork.domain.SelectionsdeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectionsdeRepository extends JpaRepository<Selectionsde, SelectionsdeId> {}
