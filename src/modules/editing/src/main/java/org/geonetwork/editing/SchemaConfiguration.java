/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "geonetwork.schemaplugin", ignoreUnknownFields = false, ignoreInvalidFields = false)
@Data
@NoArgsConstructor
public class SchemaConfiguration {
    List<Schema> schemas;

    @Data
    public static class Schema {
        String name;
        List<Property> properties;

        public List<Property> getProperties() {
            return properties == null ? List.of() : List.copyOf(properties);
        }

        public void setProperties(List<Property> properties) {
            this.properties = properties == null ? List.of() : List.copyOf(properties);
        }

        @Data
        @Setter(onMethod_ = @SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"}))
        @Getter(onMethod_ = @SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"}))
        public static class Property {
            String name;
            String xpath;
            String value;
        }
    }

    public List<Schema> getSchemas() {
        return schemas == null ? List.of() : List.copyOf(schemas);
    }

    public void setSchemas(List<Schema> schemas) {
        this.schemas = schemas == null ? List.of() : List.copyOf(schemas);
    }

    public Optional<Schema> getSchema(String name) {
        if (schemas == null) {
            return Optional.empty();
        }
        return schemas.stream().filter(schema -> schema.getName().equals(name)).findFirst();
    }
}
