/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
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
@Table(name = "guf_userfeedbacks")
public class GufUserfeedback {
  @Id
  @Size(max = 255)
  @SequenceGenerator(
      name = "guf_userfeedbacks_id_gen",
      sequenceName = "csw_server_capabilities_info_id_seq",
      allocationSize = 1)
  @Column(name = "uuid", nullable = false)
  private String uuid;

  @Size(max = 255)
  @Column(name = "authoremail")
  private String authoremail;

  @Size(max = 255)
  @Column(name = "authorname")
  private String authorname;

  @Size(max = 255)
  @Column(name = "authororganization")
  private String authororganization;

  @Column(name = "authorprivacy")
  private Integer authorprivacy;

  @Size(max = 255)
  @Column(name = "commenttext")
  private String commenttext;

  @Column(name = "creationdate")
  private Instant creationdate;

  @Size(max = 255)
  @NotNull
  @Column(name = "status", nullable = false)
  private String status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "approver_id")
  private User approver;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "metadata_uuid", referencedColumnName = "uuid")
  private org.geonetwork.domain.Metadata metadataUuid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_uuid")
  private GufUserfeedback parentUuid;
}
