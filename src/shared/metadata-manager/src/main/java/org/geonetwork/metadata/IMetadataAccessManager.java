/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
