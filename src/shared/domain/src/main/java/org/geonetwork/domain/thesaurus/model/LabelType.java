/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain.thesaurus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "label_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelType {

  // Database initialization SQL:
  // INSERT INTO label_type (id, name) VALUES
  //     (1, 'prefLabel'),
  //     (2, 'altLabel'),
  //     (3, 'hiddenLabel');

  @Getter
  public enum LabelTypeName {
    PREF_LABEL(1L, "prefLabel"),
    ALT_LABEL(2L, "altLabel"),
    HIDDEN_LABEL(3L, "hiddenLabel");

    private final Long id;
    private final String name;

    LabelTypeName(Long id, String name) {
      this.id = id;
      this.name = name;
    }

  }

  public static final LabelType PREF_LABEL = new LabelType(1L, "prefLabel");
  public static final LabelType ALT_LABEL = new LabelType(2L, "altLabel");
  public static final LabelType HIDDEN_LABEL = new LabelType(3L, "hiddenLabel");

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50, nullable = false, unique = true)
  private String name;

}
