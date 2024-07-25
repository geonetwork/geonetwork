/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Table(name = "usersearch_group")
public class UsersearchGroup {
  @SequenceGenerator(
      name = "usersearch_group_id_gen",
      sequenceName = "user_search_id_seq",
      allocationSize = 1)
  @EmbeddedId
  private UsersearchGroupId id;

  @MapsId("usersearchId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usersearch_id", nullable = false)
  private Usersearch usersearch;

  @MapsId("groupId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "group_id", nullable = false)
  private Group group;
}
