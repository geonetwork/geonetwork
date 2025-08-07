/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** MVC configuration. */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    private String homeUrl;

    public MvcConfiguration(@Value("${geonetwork.home: '/'}") String homeUrl) {
        this.homeUrl = homeUrl;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String viewName = homeUrl.equals("/") ? "home" : "redirect:" + homeUrl;
        registry.addViewController("/").setViewName(viewName);
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/signin").setViewName("signin");
    }
}
