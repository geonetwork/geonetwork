/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import java.util.List;
import java.util.Optional;
import org.geonetwork.domain.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SourceRepository extends JpaRepository<Source, String> {
    Optional<Source> findByType(String type);

    @Query(
            "select s from Source s where s.type = 'portal' or (s.type = 'subportal' and s.islistableinheaderselector = 'y')")
    List<Source> findAllOgcApiCollections();
}
