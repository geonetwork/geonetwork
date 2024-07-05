package org.geonetwork.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "guf_rating")
public class GufRating {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guf_rating_id_gen")
    @SequenceGenerator(name = "guf_rating_id_gen", sequenceName = "gufrat_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "rating")
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criteria_id")
    private GufRatingcriterion criteria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userfeedback_id")
    private GufUserfeedback userfeedback;

    @OneToOne(mappedBy = "detailedratinglist")
    private GufUserfeedbacksGufRating gufUserfeedbacksGufRating;

}
