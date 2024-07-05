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
@Table(name = "selections")
public class Selection {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "selections_id_gen")
    @SequenceGenerator(name = "selections_id_gen", sequenceName = "selection_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "iswatchable", nullable = false, length = Integer.MAX_VALUE)
    private String iswatchable;

    @OneToMany(mappedBy = "iddes")
    private Set<Selectionsde> selectionsdes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "selectionid")
    private Set<Usersavedselection> usersavedselections = new LinkedHashSet<>();

}
