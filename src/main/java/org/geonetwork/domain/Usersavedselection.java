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
@Table(name = "usersavedselections")
public class Usersavedselection {
  @SequenceGenerator(
      name = "usersavedselections_id_gen",
      sequenceName = "operation_id_seq",
      allocationSize = 1)
  @EmbeddedId
  private UsersavedselectionId id;

  @MapsId("selectionid")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "selectionid", nullable = false)
  private Selection selectionid;

  @MapsId("userid")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "userid", nullable = false)
  private User userid;
}
