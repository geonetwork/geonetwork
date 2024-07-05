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
@Table(name = "guf_ratingcriteriades")
public class GufRatingcriteriade {
    @SequenceGenerator(name = "guf_ratingcriteriades_id_gen", sequenceName = "csw_server_capabilities_info_id_seq", allocationSize = 1)
    @EmbeddedId
    private GufRatingcriteriadeId id;

    @MapsId("iddes")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iddes", nullable = false)
    private GufRatingcriterion iddes;

    @Size(max = 2000)
    @NotNull
    @Column(name = "label", nullable = false, length = 2000)
    private String label;

}
