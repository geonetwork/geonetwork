/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import lombok.Builder;
import lombok.Data;

/** Coordinate reference system. */
@Data
@Builder
public class PartyIdentifier {
    private String code;
    private String codeSpace;
    private String link;
}
