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
@Table(name = "metadata")
public class Metadata {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metadata_id_gen")
  @SequenceGenerator(
      name = "metadata_id_gen",
      sequenceName = "metadata_category_id_seq",
      allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "data", nullable = false, length = Integer.MAX_VALUE)
  private String data;

  @Size(max = 30)
  @NotNull
  @Column(name = "changedate", nullable = false, length = 30)
  private String changedate;

  @Size(max = 30)
  @NotNull
  @Column(name = "createdate", nullable = false, length = 30)
  private String createdate;

  @Column(name = "displayorder")
  private Integer displayorder;

  @Size(max = 255)
  @Column(name = "doctype")
  private String doctype;

  @Size(max = 255)
  @Column(name = "extra")
  private String extra;

  @NotNull
  @Column(name = "popularity", nullable = false)
  private Integer popularity;

  @NotNull
  @Column(name = "rating", nullable = false)
  private Integer rating;

  @Size(max = 255)
  @Column(name = "root")
  private String root;

  @Size(max = 32)
  @NotNull
  @Column(name = "schemaid", nullable = false, length = 32)
  private String schemaid;

  @Size(max = 255)
  @Column(name = "title")
  private String title;

  @NotNull
  @Column(name = "istemplate", nullable = false, length = Integer.MAX_VALUE)
  private String istemplate;

  @NotNull
  @Column(name = "isharvested", nullable = false, length = Integer.MAX_VALUE)
  private String isharvested;

  @Size(max = 512)
  @Column(name = "harvesturi", length = 512)
  private String harvesturi;

  @Size(max = 255)
  @Column(name = "harvestuuid")
  private String harvestuuid;

  @Column(name = "groupowner")
  private Integer groupowner;

  @NotNull
  @Column(name = "owner", nullable = false)
  private Integer owner;

  @Size(max = 255)
  @NotNull
  @Column(name = "source", nullable = false)
  private String source;

  @Size(max = 255)
  @NotNull
  @Column(name = "uuid", nullable = false, unique = true)
  private String uuid;
}
