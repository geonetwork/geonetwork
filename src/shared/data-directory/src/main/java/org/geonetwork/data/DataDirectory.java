/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
    private String dataDir;

    public Path getDataDir() {
        return Paths.get(dataDir);
    }

    public Path getMetadataDir() {
        return Paths.get(dataDir).resolve("metadata_data");
    }

    public DataDirectory(@Value("${geonetwork.directory.data}") String dataDir) {
        this.dataDir = dataDir;
        log.info("DataDirectory initialized");
        log.info(String.format("  Data directory: %s", this.dataDir));
    }
}
