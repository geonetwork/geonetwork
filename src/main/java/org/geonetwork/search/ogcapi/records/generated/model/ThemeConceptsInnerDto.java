/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.net.URI;
import java.util.Objects;

/**
 * ThemeConceptsInnerDto
 */

@JsonTypeName("theme_concepts_inner")
@JacksonXmlRootElement(localName = "ThemeConceptsInnerDto")
@XmlRootElement(name = "ThemeConceptsInnerDto")
@XmlAccessorType(XmlAccessType.FIELD)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]", comments = "Generator version: 7.6.0")
public class ThemeConceptsInnerDto {

    private String id;

    private String title;

    private String description;

    private URI url;

    public ThemeConceptsInnerDto() {
        super();
    }

    /**
     * Constructor with only required parameters
     */
    public ThemeConceptsInnerDto(String id) {
        this.id = id;
    }

    /**
     * Create a builder with no initialized field (except for the default values).
     */
    public static Builder builder() {
        return new Builder();
    }

    public ThemeConceptsInnerDto id(String id) {
        this.id = id;
        return this;
    }

    /**
     * An identifier for the concept.
     *
     * @return id
     */
    @NotNull
    @Schema(name = "id", description = "An identifier for the concept.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ThemeConceptsInnerDto title(String title) {
        this.title = title;
        return this;
    }

    /**
     * A human readable title for the concept.
     *
     * @return title
     */

    @Schema(name = "title", description = "A human readable title for the concept.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ThemeConceptsInnerDto description(String description) {
        this.description = description;
        return this;
    }

    /**
     * A human readable description for the concept.
     *
     * @return description
     */

    @Schema(name = "description", description = "A human readable description for the concept.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("description")
    @JacksonXmlProperty(localName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ThemeConceptsInnerDto url(URI url) {
        this.url = url;
        return this;
    }

    /**
     * A URI providing further description of the concept.
     *
     * @return url
     */
    @Valid
    @Schema(name = "url", description = "A URI providing further description of the concept.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("url")
    @JacksonXmlProperty(localName = "url")
    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ThemeConceptsInnerDto themeConceptsInner = (ThemeConceptsInnerDto) o;
        return Objects.equals(this.id, themeConceptsInner.id) &&
            Objects.equals(this.title, themeConceptsInner.title) &&
            Objects.equals(this.description, themeConceptsInner.description) &&
            Objects.equals(this.url, themeConceptsInner.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, url);
    }

    @Override
    public String toString() {
        String sb = "class ThemeConceptsInnerDto {\n" +
            "    id: " + toIndentedString(id) + "\n" +
            "    title: " + toIndentedString(title) + "\n" +
            "    description: " + toIndentedString(description) + "\n" +
            "    url: " + toIndentedString(url) + "\n" +
            "}";
        return sb;
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    /**
     * Create a builder with a shallow copy of this instance.
     */
    public Builder toBuilder() {
        Builder builder = new Builder();
        return builder.copyOf(this);
    }

    public static class Builder {

        private ThemeConceptsInnerDto instance;

        public Builder() {
            this(new ThemeConceptsInnerDto());
        }

        protected Builder(ThemeConceptsInnerDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(ThemeConceptsInnerDto value) {
            this.instance.setId(value.id);
            this.instance.setTitle(value.title);
            this.instance.setDescription(value.description);
            this.instance.setUrl(value.url);
            return this;
        }

        public Builder id(String id) {
            this.instance.id(id);
            return this;
        }

        public Builder title(String title) {
            this.instance.title(title);
            return this;
        }

        public Builder description(String description) {
            this.instance.description(description);
            return this;
        }

        public Builder url(URI url) {
            this.instance.url(url);
            return this;
        }

        /**
         * returns a built ThemeConceptsInnerDto instance.
         * <p>
         * The builder is not reusable (NullPointerException)
         */
        public ThemeConceptsInnerDto build() {
            try {
                return this.instance;
            } finally {
                // ensure that this.instance is not reused
                this.instance = null;
            }
        }

        @Override
        public String toString() {
            return getClass() + "=(" + instance + ")";
        }
    }

}

