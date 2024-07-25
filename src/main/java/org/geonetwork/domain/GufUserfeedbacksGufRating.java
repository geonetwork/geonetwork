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
import jakarta.persistence.OneToOne;
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
@Table(name = "guf_userfeedbacks_guf_rating")
public class GufUserfeedbacksGufRating {
  @Id
  @NotNull
  @SequenceGenerator(
      name = "guf_userfeedbacks_guf_rating_id_gen",
      sequenceName = "csw_server_capabilities_info_id_seq",
      allocationSize = 1)
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  private GufRating detailedratinglist;

  @Size(max = 255)
  @Column(name = "guf_userfeedback_uuid")
  private String gufUserfeedbackUuid;
}
