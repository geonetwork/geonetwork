/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.search.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;

/** List of relation type between 2 metadata records. */
@Schema(enumAsRef = true)
public enum RelatedItemType {

    /** Children are records having a parentUuid index field pointing to the parent record. */
    children,
    /**
     * Parent are records returned by
     * {@link org.fao.geonet.kernel.schema.AssociatedResourcesSchemaPlugin#getAssociatedParentUUIDs}. At least
     * parentIdentifier in ISO19139 or parentMetadata in ISO19115-3 but also associatedResources having a specific
     * association type (usually partOfSeamlessDatabase)
     */
    parent,
    /**
     * When 2 records share the same parents, they are siblings. Siblings are records having parentUuid index field set
     * to the same parent UUID.
     */
    brothersAndSisters,
    /** Aggregation info in ISO19139, Associated resources in ISO19115-3, isPartOf in Dublin core */
    siblings,
    /**
     * Associated is reverse direction of siblings. Record having agg_associated index field set to the record of
     * interest. The relation does not contains details about association and initiative type.
     */
    associated,
    /** All services having recordOperateOn index field pointing to record of interest. */
    services,
    /** When a service metadata record operatesOn a dataset. */
    datasets,
    /** Feature catalogue citation reference. */
    fcats,
    hasfeaturecats,
    /** Source reference. */
    sources,
    hassources;

    public static RelatedItemType find(String lookupName) {
        for (RelatedItemType value : values()) {
            if (value.name().equals(lookupName)) {
                return value;
            }
        }
        throw new IllegalArgumentException(String.format(
                "Invalid related type value '%s'. Allowed values are: %s", lookupName, Arrays.toString(values())));
    }

    public String value() {
        return name();
    }
}
