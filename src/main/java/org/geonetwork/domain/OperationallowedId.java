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
public class OperationallowedId implements Serializable {
    private static final long serialVersionUID = -9029370779341452077L;
    @NotNull
    @Column(name = "groupid", nullable = false)
    private Integer groupid;

    @NotNull
    @Column(name = "metadataid", nullable = false)
    private Integer metadataid;

    @NotNull
    @Column(name = "operationid", nullable = false)
    private Integer operationid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OperationallowedId entity = (OperationallowedId) o;
        return Objects.equals(this.metadataid, entity.metadataid) &&
            Objects.equals(this.groupid, entity.groupid) &&
            Objects.equals(this.operationid, entity.operationid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadataid, groupid, operationid);
    }

}
