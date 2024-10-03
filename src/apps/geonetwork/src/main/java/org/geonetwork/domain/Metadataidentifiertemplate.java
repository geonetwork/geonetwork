/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "metadataidentifiertemplate")
public class Metadataidentifiertemplate {
  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "metadataidentifiertemplate_id_gen")
  @SequenceGenerator(
      name = "metadataidentifiertemplate_id_gen",
      sequenceName = "metadata_identifier_template_id_seq",
      allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 32)
  @NotNull
  @Column(name = "name", nullable = false, length = 32)
  private String name;

  @NotNull
  @Column(name = "isprovided", nullable = false, length = Integer.MAX_VALUE)
  private String isprovided;

  @Size(max = 255)
  @NotNull
  @Column(name = "template", nullable = false)
  private String template;
}
