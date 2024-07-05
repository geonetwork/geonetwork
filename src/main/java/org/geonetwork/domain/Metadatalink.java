package org.geonetwork.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "metadatalink")
public class Metadatalink {
    @SequenceGenerator(name = "metadatalink_id_gen", sequenceName = "harvester_setting_id_seq", allocationSize = 1)
    @EmbeddedId
    private MetadatalinkId id;

    @MapsId("linkid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "linkid", nullable = false)
    private Link linkid;

    @Size(max = 255)
    @Column(name = "metadatauuid")
    private String metadatauuid;

}
