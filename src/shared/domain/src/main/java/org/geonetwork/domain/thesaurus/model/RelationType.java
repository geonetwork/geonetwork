/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "relation_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelationType {

    // Database initialization SQL:
    /*INSERT INTO relation_type (name) VALUES
    (1, 'broader'),
    (2, 'narrower'),
    (3, 'related'),
    (4, 'topConceptOf');*/

    @Getter
    public enum RelationTypeName {
        BROADER(1L, "broader"),
        NARROWER(2L, "narrower"),
        RELATED(3L, "related"),
        TOP_CONCEPT_OF(4L, "topConceptOf");

        private final Long id;
        private final String name;

        RelationTypeName(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static final RelationType BROADER =
            new RelationType(RelationTypeName.BROADER.getId(), RelationTypeName.BROADER.getName());
    public static final RelationType NARROWER =
            new RelationType(RelationTypeName.NARROWER.getId(), RelationTypeName.NARROWER.getName());
    public static final RelationType RELATED =
            new RelationType(RelationTypeName.RELATED.getId(), RelationTypeName.RELATED.getName());
    public static final RelationType TOP_CONCEPT_OF =
            new RelationType(RelationTypeName.TOP_CONCEPT_OF.getId(), RelationTypeName.TOP_CONCEPT_OF.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;
}
