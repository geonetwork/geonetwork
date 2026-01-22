/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import org.geonetwork.domain.Metadatarating;
import org.geonetwork.domain.MetadataratingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataratingRepository extends JpaRepository<Metadatarating, MetadataratingId> {}
