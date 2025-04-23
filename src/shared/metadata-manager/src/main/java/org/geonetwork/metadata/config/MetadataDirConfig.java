/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.metadata.config;

import lombok.Getter;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.metadata.datadir.DefaultMetadataDirPathProcessor;
import org.geonetwork.metadata.datadir.IMetadataDirProcessor;
import org.geonetwork.metadata.datadir.MetadataDirLayout;
import org.geonetwork.metadata.datadir.MetadataDirPrivileges;
import org.geonetwork.metadata.datadir.UuidMetadataDirPathProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MetadataDirConfig {
    private final MetadataDirLayout metadataDirLayout;
    private final MetadataDirPrivileges metadataDirPrivileges;

    public MetadataDirConfig(
            @Value("${geonetwork.directory.metadata.layout: 'DEFAULT'}") MetadataDirLayout metadataDirectoryLayout,
            @Value("${geonetwork.directory.metadata.privileges: 'DEFAULT'}")
                    MetadataDirPrivileges metadataDirPrivileges) {
        this.metadataDirLayout = metadataDirectoryLayout;
        this.metadataDirPrivileges = metadataDirPrivileges;
    }

    @Bean
    public IMetadataDirProcessor metadataDirProcessor(
            @Value("${geonetwork.directory.metadata.layout: 'DEFAULT'}") MetadataDirLayout metadataDirectoryLayout,
            @Value("${geonetwork.directory.metadata.privileges: 'DEFAULT'}")
                    MetadataDirPrivileges metadataDirPrivileges,
            MetadataRepository metadataRepository) {
        if (metadataDirectoryLayout == MetadataDirLayout.DEFAULT) {
            return new DefaultMetadataDirPathProcessor(metadataDirPrivileges);
        } else return new UuidMetadataDirPathProcessor(metadataDirPrivileges, metadataRepository);
    }
}
