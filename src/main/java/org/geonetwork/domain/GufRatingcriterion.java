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
@Table(name = "guf_ratingcriteria")
public class GufRatingcriterion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guf_ratingcriteria_id_gen")
    @SequenceGenerator(name = "guf_ratingcriteria_id_gen", sequenceName = "rating_criteria_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "isinternal", nullable = false, length = Integer.MAX_VALUE)
    private String isinternal;

    @Size(max = 32)
    @NotNull
    @Column(name = "name", nullable = false, length = 32)
    private String name;

}
