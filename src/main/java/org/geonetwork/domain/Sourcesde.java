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
@Table(name = "sourcesdes")
public class Sourcesde {
    @EmbeddedId
    private SourcesdeId id;

    @MapsId("iddes")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iddes", nullable = false)
    private Source iddes;

    @Size(max = 96)
    @NotNull
    @Column(name = "label", nullable = false, length = 96)
    private String label;

}
