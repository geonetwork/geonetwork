/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
