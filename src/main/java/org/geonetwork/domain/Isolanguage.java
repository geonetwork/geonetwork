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
@Table(name = "isolanguages")
public class Isolanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "isolanguages_id_gen")
    @SequenceGenerator(name = "isolanguages_id_gen", sequenceName = "iso_language_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 3)
    @NotNull
    @Column(name = "code", nullable = false, length = 3)
    private String code;

    @Size(max = 2)
    @Column(name = "shortcode", length = 2)
    private String shortcode;

}
