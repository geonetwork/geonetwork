/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
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
public class UseraddressId implements Serializable {
    private static final long serialVersionUID = -1334938307141570863L;

    @NotNull
    @Column(name = "userid", nullable = false)
    private Integer userid;

    @NotNull
    @Column(name = "addressid", nullable = false)
    private Integer addressid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        if (!UseraddressId.class.isInstance(o)) return false;
        UseraddressId entity = (UseraddressId) o;
        return Objects.equals(this.userid, entity.userid) && Objects.equals(this.addressid, entity.addressid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, addressid);
    }
}
