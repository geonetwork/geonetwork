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
@Table(name = "operationsdes")
public class Operationsde {
  @SequenceGenerator(
      name = "operationsdes_id_gen",
      sequenceName = "operation_id_seq",
      allocationSize = 1)
  @EmbeddedId
  private OperationsdeId id;

  @MapsId("iddes")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "iddes", nullable = false)
  private Operation iddes;

  @Size(max = 255)
  @NotNull
  @Column(name = "label", nullable = false)
  private String label;
}
