package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
@Table(name = "statusvalues")
public class Statusvalue {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statusvalues_id_gen")
  @SequenceGenerator(
      name = "statusvalues_id_gen",
      sequenceName = "status_value_id_seq",
      allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "displayorder")
  private Integer displayorder;

  @Size(max = 255)
  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "reserved", nullable = false, length = Integer.MAX_VALUE)
  private String reserved;

  @Size(max = 255)
  @Column(name = "notificationlevel")
  private String notificationlevel;

  @Size(max = 255)
  @Column(name = "type")
  private String type;
}
