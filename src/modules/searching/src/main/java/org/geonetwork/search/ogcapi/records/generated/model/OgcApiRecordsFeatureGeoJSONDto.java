/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.openapitools.jackson.nullable.JsonNullable;

/** OgcApiRecordsFeatureGeoJSONDto */
@JsonTypeName("featureGeoJSON")
@JacksonXmlRootElement(localName = "OgcApiRecordsFeatureGeoJSONDto")
@XmlRootElement(name = "OgcApiRecordsFeatureGeoJSONDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsFeatureGeoJSONDto {

    private TypeEnum type;
    private OgcApiRecordsGeometryGeoJSONDto geometry;
    private JsonNullable<Object> properties = JsonNullable.undefined();
    private OgcApiRecordsFeatureGeoJSONIdDto id;

    @Valid
    private List<@Valid OgcApiRecordsLinkDto> links = new ArrayList<>();

    public OgcApiRecordsFeatureGeoJSONDto() {
        super();
    }

    /** Constructor with only required parameters */
    public OgcApiRecordsFeatureGeoJSONDto(TypeEnum type, OgcApiRecordsGeometryGeoJSONDto geometry, Object properties) {
        this.type = type;
        this.geometry = geometry;
        this.properties = JsonNullable.of(properties);
    }

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsFeatureGeoJSONDto type(TypeEnum type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     */
    @NotNull
    @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("type")
    @JacksonXmlProperty(localName = "type")
    @XmlElement(name = "type")
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public OgcApiRecordsFeatureGeoJSONDto geometry(OgcApiRecordsGeometryGeoJSONDto geometry) {
        this.geometry = geometry;
        return this;
    }

    /**
     * Get geometry
     *
     * @return geometry
     */
    @NotNull
    @Valid
    @Schema(name = "geometry", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("geometry")
    @JacksonXmlProperty(localName = "geometry")
    @XmlElement(name = "geometry")
    public OgcApiRecordsGeometryGeoJSONDto getGeometry() {
        return geometry;
    }

    public void setGeometry(OgcApiRecordsGeometryGeoJSONDto geometry) {
        this.geometry = geometry;
    }

    public OgcApiRecordsFeatureGeoJSONDto properties(Object properties) {
        this.properties = JsonNullable.of(properties);
        return this;
    }

    /**
     * Get properties
     *
     * @return properties
     */
    @NotNull
    @Schema(name = "properties", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("properties")
    @JacksonXmlProperty(localName = "properties")
    @XmlElement(name = "properties")
    public JsonNullable<Object> getProperties() {
        return properties;
    }

    public void setProperties(JsonNullable<Object> properties) {
        this.properties = properties;
    }

    public OgcApiRecordsFeatureGeoJSONDto id(OgcApiRecordsFeatureGeoJSONIdDto id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     */
    @Valid
    @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    @XmlElement(name = "id")
    public OgcApiRecordsFeatureGeoJSONIdDto getId() {
        return id;
    }

    public void setId(OgcApiRecordsFeatureGeoJSONIdDto id) {
        this.id = id;
    }

    public OgcApiRecordsFeatureGeoJSONDto links(List<@Valid OgcApiRecordsLinkDto> links) {
        this.links = links;
        return this;
    }

    public OgcApiRecordsFeatureGeoJSONDto addLinksItem(OgcApiRecordsLinkDto linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

    /**
     * Get links
     *
     * @return links
     */
    @Valid
    @Schema(name = "links", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("links")
    @JacksonXmlProperty(localName = "links")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "links")
    public List<@Valid OgcApiRecordsLinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<@Valid OgcApiRecordsLinkDto> links) {
        this.links = links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OgcApiRecordsFeatureGeoJSONDto featureGeoJSON = (OgcApiRecordsFeatureGeoJSONDto) o;
        return Objects.equals(this.type, featureGeoJSON.type)
                && Objects.equals(this.geometry, featureGeoJSON.geometry)
                && Objects.equals(this.properties, featureGeoJSON.properties)
                && Objects.equals(this.id, featureGeoJSON.id)
                && Objects.equals(this.links, featureGeoJSON.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, geometry, properties, id, links);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsFeatureGeoJSONDto {\n"
                + "    type: "
                + toIndentedString(type)
                + "\n"
                + "    geometry: "
                + toIndentedString(geometry)
                + "\n"
                + "    properties: "
                + toIndentedString(properties)
                + "\n"
                + "    id: "
                + toIndentedString(id)
                + "\n"
                + "    links: "
                + toIndentedString(links)
                + "\n"
                + "}";
        return sb;
    }

    /** Convert the given object to string with each line indented by 4 spaces (except the first line). */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    /** Create a builder with a shallow copy of this instance. */
    public Builder toBuilder() {
        Builder builder = new Builder();
        return builder.copyOf(this);
    }

    /** Gets or Sets type */
    public enum TypeEnum {
        FEATURE("Feature");

        private final String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static TypeEnum fromValue(String value) {
            for (TypeEnum b : TypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public static class Builder {

        private OgcApiRecordsFeatureGeoJSONDto instance;

        public Builder() {
            this(new OgcApiRecordsFeatureGeoJSONDto());
        }

        protected Builder(OgcApiRecordsFeatureGeoJSONDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsFeatureGeoJSONDto value) {
            this.instance.setType(value.type);
            this.instance.setGeometry(value.geometry);
            this.instance.setProperties(value.properties);
            this.instance.setId(value.id);
            this.instance.setLinks(value.links);
            return this;
        }

        public Builder type(TypeEnum type) {
            this.instance.type(type);
            return this;
        }

        public Builder geometry(OgcApiRecordsGeometryGeoJSONDto geometry) {
            this.instance.geometry(geometry);
            return this;
        }

        public Builder properties(Object properties) {
            this.instance.properties(properties);
            return this;
        }

        public Builder properties(JsonNullable<Object> properties) {
            this.instance.properties = properties;
            return this;
        }

        public Builder id(OgcApiRecordsFeatureGeoJSONIdDto id) {
            this.instance.id(id);
            return this;
        }

        public Builder links(List<@Valid OgcApiRecordsLinkDto> links) {
            this.instance.links(links);
            return this;
        }

        /**
         * returns a built OgcApiRecordsFeatureGeoJSONDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsFeatureGeoJSONDto build() {
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
