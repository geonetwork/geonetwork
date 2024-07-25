package org.geonetwork.domain;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "isenabled", nullable = false, length = Integer.MAX_VALUE)
  private String isenabled;

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
  private Set<Usergroup> usergroups = new LinkedHashSet<>();
}
