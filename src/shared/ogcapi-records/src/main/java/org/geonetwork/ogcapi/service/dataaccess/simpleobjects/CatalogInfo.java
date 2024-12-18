/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.dataaccess.simpleobjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geonetwork.domain.Source;
import org.geonetwork.index.model.record.IndexRecord;

/** Information about a catalog - the underlying DB Source, and the linked Elastic Index Record (if available). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogInfo {

    private Source source;
    private IndexRecord linkedIndexRecord;
}
