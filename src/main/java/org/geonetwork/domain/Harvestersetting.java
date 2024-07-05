package org.geonetwork.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "harvestersettings")
public class Harvestersetting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "harvestersettings_id_gen")
    @SequenceGenerator(name = "harvestersettings_id_gen", sequenceName = "harvester_setting_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "\"value\"", length = Integer.MAX_VALUE)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid")
    private Harvestersetting parentid;

    @NotNull
    @Column(name = "encrypted", nullable = false, length = Integer.MAX_VALUE)
    private String encrypted;

    @OneToMany(mappedBy = "parentid")
    private Set<Harvestersetting> harvestersettings = new LinkedHashSet<>();

}
