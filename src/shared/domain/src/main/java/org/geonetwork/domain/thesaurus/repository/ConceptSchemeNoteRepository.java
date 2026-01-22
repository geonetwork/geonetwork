/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */


package org.geonetwork.domain.thesaurus.repository;

import org.geonetwork.domain.thesaurus.model.ConceptSchemeNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConceptSchemeNoteRepository extends JpaRepository<ConceptSchemeNote, Long> {}
