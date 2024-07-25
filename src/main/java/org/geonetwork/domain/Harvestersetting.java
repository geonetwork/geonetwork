/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
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
@Table(name = "harvestersettings")
public class Harvestersetting {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "harvestersettings_id_gen")
  @SequenceGenerator(
      name = "harvestersettings_id_gen",
      sequenceName = "harvester_setting_id_seq",
      allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 255)
  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "\"value\"", length = Integer.MAX_VALUE)
  private String value;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parentid")
  private Harvestersetting parentid;

  @NotNull
  @Column(name = "encrypted", nullable = false, length = Integer.MAX_VALUE)
  private String encrypted;

  @OneToMany(mappedBy = "parentid")
  private Set<Harvestersetting> harvestersettings = new LinkedHashSet<>();
}
