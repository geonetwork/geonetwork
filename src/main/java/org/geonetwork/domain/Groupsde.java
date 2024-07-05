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
@Table(name = "groupsdes")
public class Groupsde {
    @SequenceGenerator(name = "groupsdes_id_gen", sequenceName = "address_id_seq", allocationSize = 1)
    @EmbeddedId
    private GroupsdeId id;

    @MapsId("iddes")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iddes", nullable = false)
    private Group iddes;

    @Size(max = 96)
    @NotNull
    @Column(name = "label", nullable = false, length = 96)
    private String label;

}
