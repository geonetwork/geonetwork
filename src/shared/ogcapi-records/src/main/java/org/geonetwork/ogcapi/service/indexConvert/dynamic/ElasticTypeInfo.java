/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.indexConvert.dynamic;

import io.swagger.v3.oas.models.media.*;
import lombok.*;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldMapperConfig;
import org.geonetwork.ogcapi.service.configuration.SimpleType;

/** Type metadata about a dynamic property. */
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class ElasticTypeInfo {
    /** link to the user-defined field. */
    private OgcElasticFieldMapperConfig config;

    /** Is this property a list or a single-value? */
    private boolean isList;

    /** Base type (i.e. string, date, integer, ...) */
    private SimpleType type;

    /**
     * given an ElasticTypeInfo, return a OpenApi schema for it.
     *
     * @return OpenApi schema for the type
     */
    public Schema getOpenAPIType() {
        if (this.getType().equals(SimpleType.STRING)) {
            var stringSchema = new StringSchema().name(this.getConfig().getOgcProperty());
            return stringSchema;
        }
        if (this.getType().equals(SimpleType.INTEGER)) {
            var intSchema = new IntegerSchema().name(this.getConfig().getOgcProperty());
            return intSchema;
        }
        if (this.getType().equals(SimpleType.DOUBLE)) {
            var numberSchema = new NumberSchema().name(this.getConfig().getOgcProperty());
            return numberSchema;
        }
        if (this.getType().equals(SimpleType.DATE)) {
            var dateSchema = new DateSchema().name(this.getConfig().getOgcProperty());
            return dateSchema;
        }
        if (this.getType().equals(SimpleType.BOOLEAN)) {
            var boolSchema = new BooleanSchema().name(this.getConfig().getOgcProperty());
            return boolSchema;
        }
        throw new RuntimeException("Unsupported ElasticType: " + this.getType());
    }
}
