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
@Table(name = "schematroncriteria")
public class Schematroncriterion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schematroncriteria_id_gen")
    @SequenceGenerator(name = "schematroncriteria_id_gen", sequenceName = "schematron_criteria_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @Size(max = 255)
    @Column(name = "uitype")
    private String uitype;

    @Size(max = 255)
    @Column(name = "uivalue")
    private String uivalue;

    @Size(max = 255)
    @NotNull
    @Column(name = "\"value\"", nullable = false)
    private String value;

}
