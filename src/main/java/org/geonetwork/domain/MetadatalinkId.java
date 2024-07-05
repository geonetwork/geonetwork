package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class MetadatalinkId implements Serializable {
    private static final long serialVersionUID = 2596202470816814267L;
    @NotNull
    @Column(name = "metadataid", nullable = false)
    private Integer metadataid;

    @NotNull
    @Column(name = "linkid", nullable = false)
    private Integer linkid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MetadatalinkId entity = (MetadatalinkId) o;
        return Objects.equals(this.metadataid, entity.metadataid) &&
                Objects.equals(this.linkid, entity.linkid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadataid, linkid);
    }

}
