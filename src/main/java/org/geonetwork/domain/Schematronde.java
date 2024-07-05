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
@Table(name = "schematrondes")
public class Schematronde {
    @SequenceGenerator(name = "schematrondes_id_gen", sequenceName = "operation_id_seq", allocationSize = 1)
    @EmbeddedId
    private SchematrondeId id;

    @MapsId("iddes")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iddes", nullable = false)
    private Schematron iddes;

    @Size(max = 96)
    @NotNull
    @Column(name = "label", nullable = false, length = 96)
    private String label;

}
