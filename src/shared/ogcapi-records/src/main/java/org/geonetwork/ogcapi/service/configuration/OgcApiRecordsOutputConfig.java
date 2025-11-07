/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** configure the output for the ogcapi-records json output (defined in the ogcapi-records spec). */
@Configuration
@ConfigurationProperties(prefix = "geonetwork.openapi-records.ogcapi-records-json")
@Getter
@Setter
public class OgcApiRecordsOutputConfig {

    /** included the underlying (raw) XML (i.e. iso19139) in the output. */
    public boolean includeRawXML;

    /** include the elastic index json records (`IndexRecord` class) in the output. */
    public boolean includeElasticJson;
}
