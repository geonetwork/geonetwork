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
@Table(name = "metadataidentifiertemplate")
public class Metadataidentifiertemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metadataidentifiertemplate_id_gen")
    @SequenceGenerator(name = "metadataidentifiertemplate_id_gen", sequenceName = "metadata_identifier_template_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 32)
    @NotNull
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @NotNull
    @Column(name = "isprovided", nullable = false, length = Integer.MAX_VALUE)
    private String isprovided;

    @Size(max = 255)
    @NotNull
    @Column(name = "template", nullable = false)
    private String template;

}
