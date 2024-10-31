/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("geonetwork.tasks.data-ingester")
@Data
@NoArgsConstructor
public class DataIngesterConfiguration {
    List<Resource> resources;

    @Data
    public static class Resource {
        private enum Type {
            dataset,
            service
        }

        Type type;
        List<Property> properties;

        @Data
        public static class Property {
            /**
             * Define where to collect properties in data analysis context. Can be related to the data source, the
             * layers, the dataset columns.
             */
            enum Context {
                DatasetInfo,
                DatasetLayer,
                DatasetColumns
            }

            String name;
            String help;
            String context;

            List<Operation> operations;

            @Data
            static class Operation {
                // Replace by SpecialUpdateTags
                enum OperationType {
                    gn_add,
                    gn_create,
                    gn_delete,
                    gn_replace
                }

                String schema;

                @NotNull
                OperationType operation;

                String xpath;
                String condition;
                String value;
            }

            List<Property> actions;
        }
    }
}
