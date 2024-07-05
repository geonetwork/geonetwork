package org.geonetwork.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "selectionsdes")
public class Selectionsde {
    @SequenceGenerator(name = "selectionsdes_id_gen", sequenceName = "operation_id_seq", allocationSize = 1)
    @EmbeddedId
    private SelectionsdeId id;

    @MapsId("iddes")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iddes", nullable = false)
    private Selection iddes;

    @Size(max = 255)
    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

}
