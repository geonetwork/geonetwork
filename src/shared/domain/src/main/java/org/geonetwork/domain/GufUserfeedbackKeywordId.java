/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
public class GufUserfeedbackKeywordId implements Serializable {
    private static final long serialVersionUID = -5393683083661953275L;

    @Size(max = 255)
    @NotNull
    @Column(name = "userfeedback_uuid", nullable = false)
    private String userfeedbackUuid;

    @NotNull
    @Column(name = "keyword_id", nullable = false)
    private Long keywordId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        if (!GufUserfeedbackKeywordId.class.isInstance(o)) return false;
        GufUserfeedbackKeywordId entity = (GufUserfeedbackKeywordId) o;
        return Objects.equals(this.keywordId, entity.keywordId)
                && Objects.equals(this.userfeedbackUuid, entity.userfeedbackUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keywordId, userfeedbackUuid);
    }
}
