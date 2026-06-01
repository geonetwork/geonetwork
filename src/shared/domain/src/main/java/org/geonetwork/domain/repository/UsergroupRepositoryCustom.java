/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import java.util.List;
import org.geonetwork.domain.Usergroup;
import org.springframework.data.jpa.domain.Specification;

public interface UsergroupRepositoryCustom {
    /**
     * Find all the groupIds that match the specification provided.
     *
     * @param spec a UserGroup selector specification
     */
    List<Integer> findGroupIds(Specification<Usergroup> spec);
}
