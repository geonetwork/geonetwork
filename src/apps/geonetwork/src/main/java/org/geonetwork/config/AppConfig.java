/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.config;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@Configuration
public class AppConfig {
    @Bean
    public MessageSource messageSource() throws IOException {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources("classpath*:messages.properties");

        Path classpath = Path.of(
                org.apache.commons.lang3.StringUtils.substringBefore(System.getProperty("java.class.path"), ":"));

        var basenames = new HashSet<String>();

        for (var resource : resources) {
            String basename;
            if (resource instanceof FileSystemResource) { // The executable is class file (target/classes etc).
                // Extract class path to find resource name.
                basename = classpath.relativize(resource.getFile().toPath()).toString();
            } else if (resource
                    instanceof ClassPathResource classPathResource) { // The process is jar-like single file.
                basename = classPathResource.getPath();
            } else if (resource instanceof UrlResource urlResource) {
                basename = urlResource.getURL().toString();
            } else {
                throw new IllegalStateException(String.format(
                        "Resource must be either FileSystemResource or ClassPathResource. %s [Class: %s]",
                        resource.getURL().toString(), resource.getClass()));
            }

            // Remove file extension and language code from file name.
            String resourceName = basename.replaceAll("(|_[a-z]{2}_[A-Z]{2}).properties$", "");

            basenames.add("classpath:" + resourceName);
        }

        // Load collected basenames.
        messageSource.setBasenames(basenames.toArray(String[]::new));
        // Configure message source...
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }
}
