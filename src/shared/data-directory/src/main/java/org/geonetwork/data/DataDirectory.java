/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.data;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataDirectory {

    @Value("${geonetwork.directory.data}")
    private String dataDir;

    public Path getDataDir() {
        return Paths.get(dataDir);
    }

    public Path getMetadataDir() {
        return Paths.get(dataDir).resolve("metadata_data");
    }

    public DataDirectory() {
        log.info("DataDirectory initialized");
        log.info(String.format("  Data directory: %s", this.dataDir));
    }
}
