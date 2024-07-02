/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Value("${geonetwork.openapi.url}")
    private String serverUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server server = new Server();
        server.setUrl(serverUrl);

        Contact contact = new Contact();
        contact.setEmail("geonetwork-users@lists.sourceforge.net");
        contact.setName("GeoNetwork opensource");
        contact.setUrl("https://geonetwork-opensource.org/");

        License license = new License().name("GPL 2.0").url("https://www.gnu.org/licenses/old-licenses/lgpl-2.0.html");

        Info info = new Info()
                .title("GeoNetwork API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to GeoNetwork API.")
                .license(license);

        return new OpenAPI().info(info).servers(List.of(server));
    }
}
