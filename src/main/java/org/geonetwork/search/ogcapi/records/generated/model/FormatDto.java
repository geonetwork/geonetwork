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
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

/**
 * FormatDto
 */

@JsonTypeName("format")
@JacksonXmlRootElement(localName = "FormatDto")
@XmlRootElement(name = "FormatDto")
@XmlAccessorType(XmlAccessType.FIELD)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]", comments = "Generator version: 7.6.0")
public class FormatDto {

    private String name;

    private String mediaType;

    /**
     * Create a builder with no initialized field (except for the default values).
     */
    public static Builder builder() {
        return new Builder();
    }

    public FormatDto name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     */

    @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FormatDto mediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    /**
     * Get mediaType
     *
     * @return mediaType
     */

    @Schema(name = "mediaType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("mediaType")
    @JacksonXmlProperty(localName = "mediaType")
    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormatDto format = (FormatDto) o;
        return Objects.equals(this.name, format.name) &&
            Objects.equals(this.mediaType, format.mediaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mediaType);
    }

    @Override
    public String toString() {
        String sb = "class FormatDto {\n" +
            "    name: " + toIndentedString(name) + "\n" +
            "    mediaType: " + toIndentedString(mediaType) + "\n" +
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

        private FormatDto instance;

        public Builder() {
            this(new FormatDto());
        }

        protected Builder(FormatDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(FormatDto value) {
            this.instance.setName(value.name);
            this.instance.setMediaType(value.mediaType);
            return this;
        }

        public Builder name(String name) {
            this.instance.name(name);
            return this;
        }

        public Builder mediaType(String mediaType) {
            this.instance.mediaType(mediaType);
            return this;
        }

        /**
         * returns a built FormatDto instance.
         * <p>
         * The builder is not reusable (NullPointerException)
         */
        public FormatDto build() {
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

