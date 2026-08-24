/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.thesaurus.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.geonetwork.domain.thesaurus.model.ConceptScheme;
import org.geonetwork.domain.thesaurus.repository.ConceptSchemeRepository;
import org.geonetwork.utility.legacy.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RemoveThesaurusService {
    private final ConceptSchemeRepository conceptSchemeRepository;

    public void delete(String thesaurus) throws ResourceNotFoundException {

        Optional<ConceptScheme> conceptScheme = conceptSchemeRepository.findByUri(thesaurus);

        if (conceptScheme.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Thesaurus with identifier '%s' not found in the database.", thesaurus));
        }
        conceptSchemeRepository.deleteByUri(thesaurus);
    }
}
