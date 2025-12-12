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
        if (!MetadatalinkId.class.isInstance(o)) return false;
        MetadatalinkId entity = (MetadatalinkId) o;
        return Objects.equals(this.metadataid, entity.metadataid) && Objects.equals(this.linkid, entity.linkid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadataid, linkid);
    }
}
