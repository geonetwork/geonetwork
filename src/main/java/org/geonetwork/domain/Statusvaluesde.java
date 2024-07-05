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
@Table(name = "statusvaluesdes")
public class Statusvaluesde {
    @SequenceGenerator(name = "statusvaluesdes_id_gen", sequenceName = "mapserver_id_seq", allocationSize = 1)
    @EmbeddedId
    private StatusvaluesdeId id;

    @MapsId("iddes")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iddes", nullable = false)
    private Statusvalue iddes;

    @Size(max = 255)
    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

}
