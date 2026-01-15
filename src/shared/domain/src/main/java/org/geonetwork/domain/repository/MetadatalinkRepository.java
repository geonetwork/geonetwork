/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import org.geonetwork.domain.Metadatalink;
import org.geonetwork.domain.MetadatalinkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadatalinkRepository extends JpaRepository<Metadatalink, MetadatalinkId> {}
