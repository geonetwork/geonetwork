/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import java.nio.file.Path;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Index. */
@Getter
@Setter
@ToString
public class Index {

    private String name;
    private IndexType type;

    Path getConfigFile() {
        return null;
    }
}
