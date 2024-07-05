package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "thesaurus")
public class Thesaurus {
  @Id
  @Size(max = 255)
  @Column(name = "id", nullable = false)
  private String id;

  @NotNull
  @Column(name = "activated", nullable = false, length = Integer.MAX_VALUE)
  private String activated;
}
