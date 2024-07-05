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
@Table(name = "harvesthistory")
public class Harvesthistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "harvesthistory_id_gen")
    @SequenceGenerator(name = "harvesthistory_id_gen", sequenceName = "harvest_history_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "deleted", nullable = false, length = Integer.MAX_VALUE)
    private String deleted;

    @Column(name = "elapsedtime")
    private Integer elapsedtime;

    @Size(max = 30)
    @Column(name = "harvestdate", length = 30)
    private String harvestdate;

    @Size(max = 255)
    @Column(name = "harvestername")
    private String harvestername;

    @Size(max = 255)
    @Column(name = "harvestertype")
    private String harvestertype;

    @Size(max = 255)
    @Column(name = "harvesteruuid")
    private String harvesteruuid;

    @Column(name = "info", length = Integer.MAX_VALUE)
    private String info;

    @Column(name = "params", length = Integer.MAX_VALUE)
    private String params;

}
