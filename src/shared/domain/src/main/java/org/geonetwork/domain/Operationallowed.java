/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "operationallowed")
public class Operationallowed {
    @EmbeddedId
    private OperationallowedId id;

    @Override
    public String toString() {
        return "Operationallowed [metadataId=" + id.getMetadataid() + ", groupid=" + id.getGroupid() + ", permission="
                + ReservedOperation.lookup(id.getOperationid()) + "]";
    }
}
