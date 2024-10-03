/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Table(name = "groupsdes")
public class Groupsde {
  @SequenceGenerator(name = "groupsdes_id_gen", sequenceName = "address_id_seq", allocationSize = 1)
  @EmbeddedId
  private GroupsdeId id;

  @MapsId("iddes")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "iddes", nullable = false)
  private Group iddes;

  @Size(max = 96)
  @NotNull
  @Column(name = "label", nullable = false, length = 96)
  private String label;
}
