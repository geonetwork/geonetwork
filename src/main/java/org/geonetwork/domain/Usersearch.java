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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.Instant;
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
@Table(name = "usersearch")
public class Usersearch {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersearch_id_gen")
  @SequenceGenerator(
      name = "usersearch_id_gen",
      sequenceName = "user_search_id_seq",
      allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "creationdate")
  private Instant creationdate;

  @Column(name = "featuredtype", length = Integer.MAX_VALUE)
  private String featuredtype;

  @Size(max = 255)
  @Column(name = "logo")
  private String logo;

  @Column(name = "url", length = Integer.MAX_VALUE)
  private String url;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id")
  private User creator;

  @ManyToMany
  @JoinTable(
      name = "usersearch_group",
      joinColumns = @JoinColumn(name = "usersearch_id"),
      inverseJoinColumns = @JoinColumn(name = "group_id"))
  private Set<Group> groups = new LinkedHashSet<>();

  @OneToMany(mappedBy = "iddes")
  private Set<Usersearchde> usersearchdes = new LinkedHashSet<>();
}
