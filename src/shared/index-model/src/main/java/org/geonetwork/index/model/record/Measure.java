/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 *
 *
 * <pre>
 *     "measure": [
 *     {
 *       "name": "Exactitude globale",
 *       "value": "83.80 %",
 *       "type": "DQ_QuantitativeResult"
 *     }
 *   ],
 * </pre>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Measure {
    private String name;
    private String description;
    private String date;
    private String value;
    private String type;
    private String unit;
}
