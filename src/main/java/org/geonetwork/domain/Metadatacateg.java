package org.geonetwork.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "metadatacateg")
public class Metadatacateg {
    @SequenceGenerator(name = "metadatacateg_id_gen", sequenceName = "mapserver_id_seq", allocationSize = 1)
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
