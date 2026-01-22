/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.domain.thesaurus.repository;

import org.geonetwork.domain.thesaurus.model.ConceptScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptSchemeRepository extends JpaRepository<ConceptScheme, Long> {
    @Query(
            value =
                    """
    SELECT json_build_object(
        'key',
            cs.origin_type || '.' || cs.thesaurus_type || '.' || cs.internal_identifier,

        'dname',
            cs.thesaurus_type,

        'description',
            (
                SELECT csn.text
                FROM concept_scheme_note csn
                WHERE csn.concept_scheme_id = cs.id
                  AND csn.type = 'definition'
                  AND csn.language IN (:uiLang, 'en')
                ORDER BY CASE csn.language
                    WHEN :uiLang THEN 0
                    WHEN 'en' THEN 1
                    ELSE 2
                END
                LIMIT 1
            ),

        'filename',
            cs.internal_identifier || '.rdf',

        'title',
            (
                SELECT csl.text
                FROM concept_scheme_label csl
                JOIN label_type lt ON lt.id = csl.type_id
                WHERE csl.concept_scheme_id = cs.id
                  AND lt.name = 'prefLabel'
                  AND csl.language IN (:uiLang, 'en')
                ORDER BY CASE csl.language
                    WHEN :uiLang THEN 0
                    WHEN 'en' THEN 1
                    ELSE 2
                END
                LIMIT 1
            ),

        'multilingualTitles',
            COALESCE(
                (
                    SELECT json_agg(
                        json_build_object(
                            'lang', csl.language,
                            'title', csl.text
                        )
                        ORDER BY csl.language
                    )
                    FROM concept_scheme_label csl
                    JOIN label_type lt ON lt.id = csl.type_id
                    WHERE csl.concept_scheme_id = cs.id
                      AND lt.name = 'prefLabel'
                ),
                '[]'::json
            ),

        'multilingualDescriptions',
            COALESCE(
                (
                    SELECT json_agg(
                        json_build_object(
                            'lang', csn.language,
                            'description', csn.text
                        )
                        ORDER BY csn.language
                    )
                    FROM concept_scheme_note csn
                    WHERE csn.concept_scheme_id = cs.id
                      AND csn.type = 'definition'
                ),
                '[]'::json
            ),

        'url',
            cs.uri,

        'defaultNamespace',
            cs.namespace_uri,

        'type',
            cs.origin_type
    )
    FROM concept_scheme cs

    """,
            nativeQuery = true)
    String findSchemeResponse(@Param("uiLang") String uiLang);
}
