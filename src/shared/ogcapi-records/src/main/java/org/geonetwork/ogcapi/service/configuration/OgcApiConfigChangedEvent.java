/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Fired when the OGC API property-mapping configuration is updated via the admin REST API. */
@Getter
public class OgcApiConfigChangedEvent extends ApplicationEvent {

    private final long updateSequence;

    public OgcApiConfigChangedEvent(Object source, long updateSequence) {
        super(source);
        this.updateSequence = updateSequence;
    }
}
