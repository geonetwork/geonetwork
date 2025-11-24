/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** OpenAPI configuration. */
@Slf4j
@Configuration
public class OpenApiConfiguration {

    protected List<OpenApiCustomizer> openApiCustomizers;

    public OpenApiConfiguration(List<OpenApiCustomizer> openApiCustomizers) {
        this.openApiCustomizers = openApiCustomizers;
    }

    /** OpenAPI configuration. */
    @Bean
    public OpenAPI myOpenApi() {
        Contact contact = new Contact();
        contact.setEmail("geonetwork-users@lists.sourceforge.net");
        contact.setName("GeoNetwork opensource");
        contact.setUrl("https://geonetwork-opensource.org/");

        License license = new License().name("GPL 2.0").url("https://www.gnu.org/licenses/old-licenses/lgpl-2.0.html");

        Info info = new Info()
                .title("GeoNetwork API")
                .version("5.0.0")
                .contact(contact)
                .description("This API exposes endpoints to GeoNetwork API.")
                .license(license);

        var openApi = new OpenAPI().info(info);
        return openApi;
    }
}
