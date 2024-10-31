/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.domain;

import jakarta.annotation.Nullable;

/**
 * The system reserved operations. Ids and names are hardcoded and have special meaning in Geonetwork.
 *
 * @author Jesse Eichar
 */
public enum ReservedOperation {
    /** The operation required to view the metadata. */
    view(0),
    /** The operation required to download the metadata. */
    download(1),
    /** The operation required to edit the metadata. */
    editing(2),
    /** The operation required for listeners to be notified of changes about the metadata. */
    notify(3),
    /** Identifies a metadata as having a "dynamic" component. */
    dynamic(5),
    /** Operation that allows the metadata to be one of the featured metadata. */
    featured(6);

    private final int _id;

    ReservedOperation(int id) {
        this._id = id;
    }

    /**
     * Look up a reserved operation by id. Returns null if not a reserved operation.
     *
     * @param opId the id of the operation to look up.
     * @return null or the reserved operation.
     */
    public static @Nullable ReservedOperation lookup(int opId) {
        for (ReservedOperation op : ReservedOperation.values()) {
            if (op._id == opId) {
                return op;
            }
        }
        return null;
    }

    /**
     * Get the id of the operation.
     *
     * @return the id of the operation.
     */
    public int getId() {
        return _id;
    }

    /** Create a transient operation entity with the data of the ReservedOperation */
    public Operation getOperationEntity() {
        return Operation.builder().id(_id).name(name()).build();
    }
}
