/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.index.model.record;

import lombok.Data;

/** Indexing report. */
@Data
public class IndexingReport {

    Integer numberOfRecordsWithUnsupportedSchema = 0;
    Integer numberOfRecordsWithXsltErrors = 0;
    Integer numberOfRecordsWithIndexingErrors = 0;
    Integer numberOfGhostRecords = 0;
}
