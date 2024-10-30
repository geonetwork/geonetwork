/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/** Code list value. */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CodeListValue {
    String label;
    String definition;
    String code;
}
