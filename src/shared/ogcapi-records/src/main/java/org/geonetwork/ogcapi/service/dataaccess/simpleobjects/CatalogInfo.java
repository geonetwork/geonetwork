/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.dataaccess.simpleobjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.geonetwork.domain.Source;
import org.geonetwork.index.model.record.IndexRecord;

/** Information about a catalog - the underlying DB Source, and the linked Elastic Index Record (if available). */
@Getter
@AllArgsConstructor
// NOTE: see the spotbugs excludes for this class (EI_EXPOSE_REP2 and EI_EXPOSE_REP)
public class CatalogInfo {

    private Source source;
    private IndexRecord linkedIndexRecord;
}
