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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** OpenAPI configuration. */
@Configuration
public class OpenApiConfiguration {
    //    @Value("${geonetwork.openapi.url:'http://localhost:7979'}")
    //    private String serverUrl;

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

        return new OpenAPI().info(info);
    }

    @Bean
    public org.springdoc.core.customizers.OpenApiCustomizer customOpenApiCustomizer() {
        return openAPI -> {
            // Modify Info object
            //            var rs = openAPI.getComponents().getSchemas().get("OgcApiRecordsRecordGeoJSONDto");
            //            var ps = openAPI.getComponents().getSchemas().get("OgcApiRecordsRecordGeoJSONPropertiesDto");
            //
            //            var stringSchema = new StringSchema().name("davey");
            //            ps.addProperty("davey", stringSchema);
            // Add a global header parameter
            // openAPI.getComponents().addParameters("X-Custom-Header", new
            // Parameter().in("header").name("X-Custom-Header").description("A custom header"));
        };
    }
}
