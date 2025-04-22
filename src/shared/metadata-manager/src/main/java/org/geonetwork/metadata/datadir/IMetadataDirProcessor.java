/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.metadata.datadir;

import java.nio.file.Path;
import org.geonetwork.metadata.MetadataNotFoundException;

public interface IMetadataDirProcessor {
    Path calculatePath(Path metadataDataDirectory, int metadataId) throws MetadataNotFoundException;

    Path calculatePathWithAccess(Path metadataDataDirectory, String access, int metadataId)
            throws MetadataNotFoundException;
}
