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
public class UsergroupId implements Serializable {
    private static final long serialVersionUID = 539309035788142295L;
    @NotNull
    @Column(name = "groupid", nullable = false)
    private Integer groupid;

    @NotNull
    @Column(name = "profile", nullable = false)
    private Integer profile;

    @NotNull
    @Column(name = "userid", nullable = false)
    private Integer userid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UsergroupId entity = (UsergroupId) o;
        return Objects.equals(this.groupid, entity.groupid) &&
            Objects.equals(this.profile, entity.profile) &&
            Objects.equals(this.userid, entity.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupid, profile, userid);
    }

}
