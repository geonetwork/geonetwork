package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
