/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.thesaurus.cli;

import java.io.FileInputStream;
import java.nio.file.Path;
import org.geonetwork.thesaurus.SkosRdfImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("cli")
@ComponentScan(basePackages = "org.geonetwork")
public class SkosRdfImportApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkosRdfImportApplication.class, args);
    }

    @Bean
    CommandLineRunner importRdf(SkosRdfImportService importService) {
        return args -> {
            if (args.length == 0) {
                throw new IllegalArgumentException("Please provide path to RDF file as first argument");
            }

            Path rdfPath = Path.of(args[0]);
            try (var in = new FileInputStream(rdfPath.toFile())) {
                importService.importRdf(in);
            }

            System.out.println("RDF import finished successfully");
        };
    }
}
