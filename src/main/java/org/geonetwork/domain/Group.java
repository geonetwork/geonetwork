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
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groups_id_gen")
    @SequenceGenerator(name = "groups_id_gen", sequenceName = "group_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 128)
    @Column(name = "email", length = 128)
    private String email;

    @Column(name = "enablecategoriesrestriction", length = Integer.MAX_VALUE)
    private String enablecategoriesrestriction;

    @Size(max = 255)
    @Column(name = "logo")
    private String logo;

    @Size(max = 32)
    @NotNull
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "referrer")
    private Integer referrer;

    @Size(max = 255)
    @Column(name = "website")
    private String website;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaultcategory_id")
    private Category defaultcategory;

    @ManyToMany
    @JoinTable(name = "group_category",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "iddes")
    private Set<Groupsde> groupsdes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "groupid")
    private Set<Usergroup> usergroups = new LinkedHashSet<>();

}
