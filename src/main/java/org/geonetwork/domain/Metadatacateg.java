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
@Table(name = "metadatacateg")
public class Metadatacateg {
  @SequenceGenerator(
      name = "metadatacateg_id_gen",
      sequenceName = "mapserver_id_seq",
      allocationSize = 1)
  @EmbeddedId
  private MetadatacategId id;

  @MapsId("metadataid")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "metadataid", nullable = false)
  private Metadata metadataid;

  @MapsId("categoryid")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "categoryid", nullable = false)
  private Category categoryid;
}
