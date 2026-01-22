/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadata;

import java.nio.file.Path;
import java.util.List;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.Operation;
import org.geonetwork.domain.Operationallowed;
import org.jdom.Element;

public interface IMetadataManager {
    /**
     * Searches a metadata by its internal identifier.
     *
     * @param metadataId the metadata internal identifier.
     * @return the metadata record.
     * @throws MetadataNotFoundException if there is no metadata with the provided identifier.
     */
    Metadata findMetadataById(int metadataId) throws MetadataNotFoundException;

    /**
     * Searches a metadata by the UUID identifier.
     *
     * @param uuid the metadata UUI identifier.
     * @param approved flag to retrieve the approved or working copy (
     * @return the metadata record.
     * @throws MetadataNotFoundException if there is no metadata with the provided identifier.
     */
    Metadata findMetadataByUuid(String uuid, boolean approved) throws MetadataNotFoundException;

    Metadata update(
            int metadataId, Element xml, boolean validate, boolean ufo, String changeDate, boolean updateDateStamp)
            throws MetadataNotFoundException;

    List<Operation> getAvailableMetadataOperations();

    List<Operationallowed> getMetadataOperations(int metadataId);

    List<Integer> getEditableGroups(int metadataId);

    Path getMetadataDir(Path metadataDataDirectory, int metadataId);

    Path getMetadataDir(Path metadataDataDirectory, String access, int metadataId);
}
