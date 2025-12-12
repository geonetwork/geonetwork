/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.repository;

import org.geonetwork.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/** Address repository. */
public interface AddressRepository extends JpaRepository<Address, Integer> {}
