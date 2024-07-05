package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class GroupsdeId implements Serializable {
    private static final long serialVersionUID = 7806926519951609363L;
    @NotNull
    @Column(name = "iddes", nullable = false)
    private Integer iddes;

    @Size(max = 5)
    @NotNull
    @Column(name = "langid", nullable = false, length = 5)
    private String langid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GroupsdeId entity = (GroupsdeId) o;
        return Objects.equals(this.iddes, entity.iddes) &&
            Objects.equals(this.langid, entity.langid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iddes, langid);
    }

}
