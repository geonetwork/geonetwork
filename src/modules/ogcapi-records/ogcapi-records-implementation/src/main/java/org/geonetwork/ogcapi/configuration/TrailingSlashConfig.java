/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.UrlHandlerFilter;

/**
 * Spring 6 no longer matches "/collections/" to "/collections". Strip the trailing slash on OGC API Records requests so
 * both forms reach the same controller.
 */
@Configuration
public class TrailingSlashConfig {

    @Bean
    public FilterRegistrationBean<UrlHandlerFilter> ogcApiRecordsTrailingSlashFilter(
            @Value("${server.servlet.context-path:}") String contextPath,
            @Value("${geonetwork.openapi-records.links.base-path:/ogcapi-records}") String basePath) {
        // UrlHandlerFilter matches against the full request path, including the context path
        var registration =
                new FilterRegistrationBean<>(UrlHandlerFilter.trailingSlashHandler(contextPath + basePath + "/**")
                        .wrapRequest()
                        .build());
        // before Spring Security, so security rules see the normalized path
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
