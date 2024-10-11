/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "metadatarating")
public class Metadatarating {
  @SequenceGenerator(
      name = "metadatarating_id_gen",
      sequenceName = "mapserver_id_seq",
      allocationSize = 1)
  @EmbeddedId
  private MetadataratingId id;

  @NotNull
  @Column(name = "rating", nullable = false)
  private Integer rating;
}
