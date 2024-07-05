package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
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
public class MetadatacategId implements Serializable {
  private static final long serialVersionUID = -3508240483658841218L;

  @NotNull
  @Column(name = "metadataid", nullable = false)
  private Integer metadataid;

  @NotNull
  @Column(name = "categoryid", nullable = false)
  private Integer categoryid;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    MetadatacategId entity = (MetadatacategId) o;
    return Objects.equals(this.metadataid, entity.metadataid)
        && Objects.equals(this.categoryid, entity.categoryid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metadataid, categoryid);
  }
}
