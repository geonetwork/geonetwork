/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
