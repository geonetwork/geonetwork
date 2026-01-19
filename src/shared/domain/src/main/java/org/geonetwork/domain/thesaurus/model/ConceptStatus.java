/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "concept_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConceptStatus {

  // Database initialization SQL:
  /*INSERT INTO concept_status (name) VALUES
      ('active'),
      ('deprecated'),
      ('retired'),
      ('obsolete'); */

  @Getter
  public enum ConceptStatusName {
    ACTIVE(1L, "active"),
    DEPRECATED(2L, "deprecated"),
    RETIRED(3L, "retired"),
    OBSOLETE(4L, "obsolete");


    private final Long id;
    private final String name;

    ConceptStatusName(Long id, String name) {
      this.id = id;
      this.name = name;
    }

  }

  public static final ConceptStatus STATUS_ACTIVE = new ConceptStatus(ConceptStatusName.ACTIVE.getId(), ConceptStatusName.ACTIVE.getName());
  public static final ConceptStatus STATUS_DEPRECATED = new ConceptStatus(ConceptStatusName.DEPRECATED.getId(), ConceptStatusName.DEPRECATED.getName());
  public static final ConceptStatus STATUS_RETIRED = new ConceptStatus(ConceptStatusName.RETIRED.getId(), ConceptStatusName.RETIRED.getName());
  public static final ConceptStatus STATUS_OBSOLETE = new ConceptStatus(ConceptStatusName.OBSOLETE.getId(), ConceptStatusName.OBSOLETE.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;
}
