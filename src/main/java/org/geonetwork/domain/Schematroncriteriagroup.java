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
@Table(name = "schematroncriteriagroup")
public class Schematroncriteriagroup {
    @SequenceGenerator(name = "schematroncriteriagroup_id_gen", sequenceName = "operation_id_seq", allocationSize = 1)
    @EmbeddedId
    private SchematroncriteriagroupId id;

    @MapsId("schematronid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schematronid", nullable = false)
    private Schematron schematronid;

    @Size(max = 255)
    @NotNull
    @Column(name = "requirement", nullable = false)
    private String requirement;

}
