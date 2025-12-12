/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "guf_userfeedbacks_guf_rating")
public class GufUserfeedbacksGufRating {
    @Id
    @NotNull
    @SequenceGenerator(
            name = "guf_userfeedbacks_guf_rating_id_gen",
            sequenceName = "csw_server_capabilities_info_id_seq",
            allocationSize = 1)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private GufRating detailedratinglist;

    @Size(max = 255)
    @Column(name = "guf_userfeedback_uuid")
    private String gufUserfeedbackUuid;

    @Override
    @SuppressWarnings("EqualsGetClass")
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        GufUserfeedbacksGufRating that = (GufUserfeedbacksGufRating) o;
        return getDetailedratinglist() != null && Objects.equals(getDetailedratinglist(), that.getDetailedratinglist());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this)
                        .getHibernateLazyInitializer()
                        .getPersistentClass()
                        .hashCode()
                : getClass().hashCode();
    }
}
