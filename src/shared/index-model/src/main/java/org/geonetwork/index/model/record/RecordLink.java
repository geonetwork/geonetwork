/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Record online resource link.
 *
 * <pre>
 *     "recordLink": [
 *     {
 *       "type": "sources",
 *       "to": "7369222c-5241-452a-af07-4929506212f9",
 *       "url": "https://metawal.wallonie.be/geonetwork/srv/api/records/7369222c-5241-452a-af07-4929506212f9",
 *       "title": "Orthophotos 2020",
 *       "origin": "remote"
 *     },
 * </pre>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RecordLink {

    private String type;
    private Origin origin;
    private String to;
    // TODO: Multilingual
    private String title;
    private String url;
    private String associationType;
    private String initiativeType;

    /** Origin of the link. */
    public enum Origin {
        remote,
        catalog
    }
}
