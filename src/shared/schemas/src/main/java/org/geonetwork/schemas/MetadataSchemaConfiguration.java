/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetadataSchemaConfiguration {
    /**
     * Unique name of the schema used in GeoNetwork (lower case). if a profile then it should be named after the base
     * schema using base-schema-name.profile-name eg. iso19139.mcp.
     */
    @NotBlank
    String name;

    @NotBlank
    String id;

    @NotBlank
    String version;

    /**
     * Name of other schema that this schema depends on. This schema will fail to load if that schema is not present.
     */
    String depends;

    /** Official URL of XML schema definitions (XSD) for this metadata schema */
    @NotBlank
    String schemaLocation;

    @NotBlank
    String primeNamespace;

    Map<String, String> standardTitle;
    Map<String, String> standardDescription;
    Map<String, String> standardUrl;

    /** Information used to detect whether a metadata record belongs to this schema */
    String autodetect;

    @Builder
    @Data
    public static class Formatter {
        @NotBlank
        String name;

        String title;

        String officialProfileName;

        String contentType;
    }

    List<Formatter> formatters;

    @Data
    public static class Filter {
        @NotBlank
        String xpath;

        @NotBlank
        String jsonpath;

        enum Operation {
            editing,
            download,
            dynamic
        }

        @NotNull
        Operation ifNotOperation;

        boolean keepMarkedElement = false;
    }

    List<Filter> filters;

    @Data
    public static class Substitute {
        @NotBlank
        String field;

        List<String> substitutes;
    }

    List<Substitute> substitutes;

    @Data
    public static class Suggestion {
        @NotBlank
        String field;

        List<String> suggestions;
    }

    List<Suggestion> suggestions;

    /** Should we deprecate it? */
    @Deprecated
    boolean readwriteUuid = false;
}
