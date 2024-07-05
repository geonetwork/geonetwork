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
@Table(name = "validation")
public class Validation {
    @SequenceGenerator(name = "validation_id_gen", sequenceName = "user_search_id_seq", allocationSize = 1)
    @EmbeddedId
    private ValidationId id;

    @Column(name = "failed")
    private Integer failed;

    @Column(name = "tested")
    private Integer tested;

    @Column(name = "required")
    private Boolean required;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @Size(max = 30)
    @Column(name = "valdate", length = 30)
    private String valdate;

    @Column(name = "reportcontent", length = Integer.MAX_VALUE)
    private String reportcontent;

    @Size(max = 255)
    @Column(name = "reporturl")
    private String reporturl;

}
