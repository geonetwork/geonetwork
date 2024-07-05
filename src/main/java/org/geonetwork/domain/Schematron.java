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
@Table(name = "schematron")
public class Schematron {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schematron_id_gen")
    @SequenceGenerator(name = "schematron_id_gen", sequenceName = "schematron_criteria_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "displaypriority", nullable = false)
    private Integer displaypriority;

    @Size(max = 255)
    @NotNull
    @Column(name = "filename", nullable = false)
    private String filename;

    @Size(max = 255)
    @NotNull
    @Column(name = "schemaname", nullable = false)
    private String schemaname;

}
