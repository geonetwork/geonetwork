/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.domain;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geonetwork.domain.converter.BooleanToYN;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Cacheable
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_gen")
    @SequenceGenerator(name = "user_id_gen", sequenceName = "user_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "isenabled", nullable = false)
    @Convert(converter = BooleanToYN.class)
    private Boolean isenabled;

    @Size(max = 16)
    @Column(name = "kind", length = 16)
    private String kind;

    @Size(max = 255)
    @Column(name = "lastlogindate")
    private String lastlogindate;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "organisation")
    private String organisation;

    @NotNull
    @Column(name = "profile", nullable = false)
    @Builder.Default
    private Profile profile = Profile.RegisteredUser;

    @Size(max = 32)
    @Column(name = "authtype", length = 32)
    private String authtype;

    @Size(max = 255)
    @Column(name = "nodeid")
    private String nodeid;

    @Size(max = 120)
    @NotNull
    @Column(name = "password", nullable = false, length = 120)
    private String password;

    @Size(max = 128)
    @Column(name = "security", length = 128)
    private String security;

    @Size(max = 255)
    @Column(name = "surname")
    private String surname;

    @Size(max = 255)
    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @OneToMany(mappedBy = "userid")
    @Builder.Default
    private Set<Usergroup> usergroups = new LinkedHashSet<>();

    @ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    @CollectionTable(name = "email")
    @Column(name = "email")
    @Builder.Default
    private Set<String> email = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "UserAddress",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = {@JoinColumn(name = "addressid", referencedColumnName = "ID", unique = true)})
    @Builder.Default
    private Set<Address> addresses = new LinkedHashSet<>();
}
