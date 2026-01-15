/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.metadata;

import java.util.Collection;
import java.util.Set;
import org.geonetwork.domain.Operation;

public interface IMetadataAccessManager {
    boolean canEdit(final int metadataId) throws Exception;

    boolean isOwner(final int metadataId) throws Exception;

    boolean hasEditPermission(final int metadataId) throws Exception;

    boolean canDownload(final int id) throws Exception;

    boolean canView(final int metadataId) throws Exception;

    Set<Operation> getOperations(int mdId, String ip) throws Exception;

    Set<Operation> getOperations(int mdId, String ip, Collection<Operation> operations) throws Exception;

    Set<Operation> getAllOperations(int mdId, String ip) throws Exception;
}
