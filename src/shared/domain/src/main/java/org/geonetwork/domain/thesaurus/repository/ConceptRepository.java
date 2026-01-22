/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.domain.thesaurus.repository;

import org.geonetwork.domain.thesaurus.model.Concept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConceptRepository extends JpaRepository<Concept, Long> {

    @Query(
            value =
                    """
        SELECT json_agg(
            json_build_object(
                'values', values_map,
                'definitions', definitions_map,
                'coordEast', cc.coord_east,
                'coordWest', cc.coord_west,
                'coordSouth', cc.coord_south,
                'coordNorth', cc.coord_north,
                'thesaurusKey',
                    cs.origin_type || '.' || cs.thesaurus_type || '.' || cs.internal_identifier,
                'value', preferred_label,
                'definition', preferred_definition,
                'uri', c.uri
            )
        )::text
        FROM (
            SELECT
                c.id AS concept_id,
                c.uri,

                -- preferred value (first language match)
                (
                    SELECT cl.text
                    FROM concept_label cl
                    JOIN label_type lt ON lt.id = cl.type_id
                    WHERE cl.concept_id = c.id
                      AND lt.name = 'prefLabel'
                      AND cl.language = :uiLang
                ) AS preferred_label,

                -- preferred definition (first language match)
                (
                    SELECT cn.text
                    FROM concept_note cn
                    WHERE cn.concept_id = c.id
                      AND cn.type = 'definition'
                      AND cn.language = :uiLang
                ) AS preferred_definition,

                -- multilingual values map
                (
                    SELECT jsonb_object_agg(cl.language, cl.text)
                    FROM concept_label cl
                    JOIN label_type lt ON lt.id = cl.type_id
                    WHERE cl.concept_id = c.id
                      AND lt.name = 'prefLabel'
                ) AS values_map,

                -- multilingual definitions map
                (
                    SELECT jsonb_object_agg(cn.language, cn.text)
                    FROM concept_note cn
                    WHERE cn.concept_id = c.id
                      AND cn.type = 'definition'
                ) AS definitions_map,

                cc.coord_east,
                cc.coord_west,
                cc.coord_south,
                cc.coord_north,

                cs.origin_type,
                cs.thesaurus_type,
                cs.internal_identifier

            FROM concept c
            JOIN concept_scheme cs ON cs.id = c.concept_scheme_id
            LEFT JOIN concept_coordinates cc ON cc.concept_id = c.id
            WHERE cs.internal_identifier = :internalIdentifier
        ) sub
        """,
            nativeQuery = true)
    String getKeywords(@Param("internalIdentifier") String internalIdentifier, @Param("uiLang") String uiLang);
}
