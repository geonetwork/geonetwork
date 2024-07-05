package org.geonetwork.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "metadatarating")
public class Metadatarating {
    @SequenceGenerator(name = "metadatarating_id_gen", sequenceName = "mapserver_id_seq", allocationSize = 1)
    @EmbeddedId
    private MetadataratingId id;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Integer rating;

}
