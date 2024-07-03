/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.index.model.record;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
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
