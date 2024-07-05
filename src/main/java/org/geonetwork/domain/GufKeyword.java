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
@Table(name = "guf_keywords")
public class GufKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guf_keywords_id_gen")
    @SequenceGenerator(name = "guf_keywords_id_gen", sequenceName = "gufkey_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name="\"value\"")
    private String value;

}
