/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
