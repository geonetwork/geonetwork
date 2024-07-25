/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ValidationId implements Serializable {
  private static final long serialVersionUID = 1162216089847311602L;

  @NotNull
  @Column(name = "metadataid", nullable = false)
  private Integer metadataid;

  @Size(max = 128)
  @NotNull
  @Column(name = "valtype", nullable = false, length = 128)
  private String valtype;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    ValidationId entity = (ValidationId) o;
    return Objects.equals(this.metadataid, entity.metadataid)
        && Objects.equals(this.valtype, entity.valtype);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metadataid, valtype);
  }
}
