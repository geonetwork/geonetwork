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
import io.swagger.v3.oas.models.media.*;
import org.geonetwork.ogcapi.service.configuration.OgcElasticFieldsMapperConfig;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ElasticTypingSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** OpenAPI configuration. */
@Configuration
public class OpenApiConfiguration {

    @Autowired
    ElasticTypingSystem elasticTypingSystem;

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

    /**
     * adds in the dynamic properties
     *
     * @return updated swagger doc
     */
    @Bean
    public org.springdoc.core.customizers.OpenApiCustomizer customOpenApiCustomizer() {
        return openAPI -> {
            // Modify Info object
            var properties = openAPI.getComponents().getSchemas().get("OgcApiRecordsRecordGeoJSONPropertiesDto");
            for (var field : this.elasticTypingSystem.getFinalElasticTypes().entrySet()) {
                if (!field.getValue().getConfig().getAddProperty()) {
                    continue;
                }
                var info = field.getValue();
                if (info.isList()) {
                    var t = getType(info);
                    var arrayProperties = new ArraySchema().items(t);
                    properties.addProperty(info.getConfig().getOgcProperty(), arrayProperties);

                } else {
                    var t = getType(info);
                    properties.addProperty(info.getConfig().getOgcProperty(), t);
                }
            }
        };
    }

    /**
     * given an ElasticTypeInfo, return a OpenApi schema for it.
     *
     * @param info type info
     * @return OpenApi schema for the type
     */
    public Schema getType(ElasticTypingSystem.ElasticTypeInfo info) {
        if (info.getType().equals(OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.STRING)) {
            var stringSchema = new StringSchema().name(info.getConfig().getOgcProperty());
            return stringSchema;
        }
        if (info.getType().equals(OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.INTEGER)) {
            var intSchema = new IntegerSchema().name(info.getConfig().getOgcProperty());
            return intSchema;
        }
        if (info.getType().equals(OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.DOUBLE)) {
            var numberSchema = new NumberSchema().name(info.getConfig().getOgcProperty());
            return numberSchema;
        }
        if (info.getType().equals(OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.DATE)) {
            var dateSchema = new DateSchema().name(info.getConfig().getOgcProperty());
            return dateSchema;
        }
        if (info.getType().equals(OgcElasticFieldsMapperConfig.OgcElasticFieldMapperConfig.SimpleType.BOOLEAN)) {
            var boolSchema = new BooleanSchema().name(info.getConfig().getOgcProperty());
            return boolSchema;
        }
        throw new RuntimeException("Unsupported ElasticType: " + info.getType());
    }
}
