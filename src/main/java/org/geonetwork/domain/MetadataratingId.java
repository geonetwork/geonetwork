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
public class MetadataratingId implements Serializable {
  private static final long serialVersionUID = -1106630645028630289L;

  @Size(max = 45)
  @NotNull
  @Column(name = "ipaddress", nullable = false, length = 45)
  private String ipaddress;

  @NotNull
  @Column(name = "metadataid", nullable = false)
  private Integer metadataid;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    MetadataratingId entity = (MetadataratingId) o;
    return Objects.equals(this.ipaddress, entity.ipaddress)
        && Objects.equals(this.metadataid, entity.metadataid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ipaddress, metadataid);
  }
}
