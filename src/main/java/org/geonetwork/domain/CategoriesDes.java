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
@Table(name = "categoriesdes")
public class CategoriesDes {
    @SequenceGenerator(name = "categoriesdes_id_gen", sequenceName = "address_id_seq", allocationSize = 1)
    @EmbeddedId
    private CategoriesDesId id;

    @MapsId("iddes")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iddes", nullable = false)
    private Category iddes;

    @Size(max = 255)
    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

}
