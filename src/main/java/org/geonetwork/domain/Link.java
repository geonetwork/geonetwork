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
@Table(name = "links")
public class Link {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "links_id_gen")
  @SequenceGenerator(name = "links_id_gen", sequenceName = "link_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "laststate")
  private Integer laststate;

  @Size(max = 255)
  @NotNull
  @Column(name = "linktype", nullable = false)
  private String linktype;

  @Size(max = 255)
  @Column(name = "protocol")
  private String protocol;

  @Column(name = "url", length = Integer.MAX_VALUE)
  private String url;

  @Size(max = 255)
  @Column(name = "dateandtime")
  private String dateandtime;

  @OneToMany(mappedBy = "link")
  @Builder.Default
  private Set<Linkstatus> linkstatuses = new LinkedHashSet<>();

  @OneToMany(mappedBy = "linkid")
  @Builder.Default
  private Set<Metadatalink> metadatalinks = new LinkedHashSet<>();
}
