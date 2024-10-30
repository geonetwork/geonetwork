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

/** OgcApiRecordsRecordGeoJSONDto */
@JsonTypeName("recordGeoJSON")
@JacksonXmlRootElement(localName = "OgcApiRecordsRecordGeoJSONDto")
@XmlRootElement(name = "OgcApiRecordsRecordGeoJSONDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsRecordGeoJSONDto {

    private String id;
    private TypeEnum type;
    private OgcApiRecordsRecordGeoJSONTimeDto time;
    private OgcApiRecordsRecordGeoJSONGeometryDto geometry;
    private OgcApiRecordsRecordGeoJSONPropertiesDto properties;

    @Valid
    private List<@Valid OgcApiRecordsLinkBaseDto> links = new ArrayList<>();

    @Valid
    private List<OgcApiRecordsLinkTemplateDto> linkTemplates = new ArrayList<>();

    public OgcApiRecordsRecordGeoJSONDto() {
        super();
    }

    /** Constructor with only required parameters */
    public OgcApiRecordsRecordGeoJSONDto(
            String id,
            TypeEnum type,
            OgcApiRecordsRecordGeoJSONGeometryDto geometry,
            OgcApiRecordsRecordGeoJSONPropertiesDto properties) {
        this.id = id;
        this.type = type;
        this.geometry = geometry;
        this.properties = properties;
    }

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsRecordGeoJSONDto id(String id) {
        this.id = id;
        return this;
    }

    /**
     * A unique identifier of the catalog record.
     *
     * @return id
     */
    @NotNull
    @Schema(
            name = "id",
            description = "A unique identifier of the catalog record.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OgcApiRecordsRecordGeoJSONDto type(TypeEnum type) {
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

    public OgcApiRecordsRecordGeoJSONDto time(OgcApiRecordsRecordGeoJSONTimeDto time) {
        this.time = time;
        return this;
    }

    /**
     * Get time
     *
     * @return time
     */
    @Valid
    @Schema(name = "time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("time")
    @JacksonXmlProperty(localName = "time")
    @XmlElement(name = "time")
    public OgcApiRecordsRecordGeoJSONTimeDto getTime() {
        return time;
    }

    public void setTime(OgcApiRecordsRecordGeoJSONTimeDto time) {
        this.time = time;
    }

    public OgcApiRecordsRecordGeoJSONDto geometry(OgcApiRecordsRecordGeoJSONGeometryDto geometry) {
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
    public OgcApiRecordsRecordGeoJSONGeometryDto getGeometry() {
        return geometry;
    }

    public void setGeometry(OgcApiRecordsRecordGeoJSONGeometryDto geometry) {
        this.geometry = geometry;
    }

    public OgcApiRecordsRecordGeoJSONDto properties(OgcApiRecordsRecordGeoJSONPropertiesDto properties) {
        this.properties = properties;
        return this;
    }

    /**
     * Get properties
     *
     * @return properties
     */
    @NotNull
    @Valid
    @Schema(name = "properties", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("properties")
    @JacksonXmlProperty(localName = "properties")
    @XmlElement(name = "properties")
    public OgcApiRecordsRecordGeoJSONPropertiesDto getProperties() {
        return properties;
    }

    public void setProperties(OgcApiRecordsRecordGeoJSONPropertiesDto properties) {
        this.properties = properties;
    }

    public OgcApiRecordsRecordGeoJSONDto links(List<@Valid OgcApiRecordsLinkBaseDto> links) {
        this.links = links;
        return this;
    }

    public OgcApiRecordsRecordGeoJSONDto addLinksItem(OgcApiRecordsLinkBaseDto linksItem) {
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
    public List<@Valid OgcApiRecordsLinkBaseDto> getLinks() {
        return links;
    }

    public void setLinks(List<@Valid OgcApiRecordsLinkBaseDto> links) {
        this.links = links;
    }

    public OgcApiRecordsRecordGeoJSONDto linkTemplates(List<OgcApiRecordsLinkTemplateDto> linkTemplates) {
        this.linkTemplates = linkTemplates;
        return this;
    }

    public OgcApiRecordsRecordGeoJSONDto addLinkTemplatesItem(OgcApiRecordsLinkTemplateDto linkTemplatesItem) {
        if (this.linkTemplates == null) {
            this.linkTemplates = new ArrayList<>();
        }
        this.linkTemplates.add(linkTemplatesItem);
        return this;
    }

    /**
     * Get linkTemplates
     *
     * @return linkTemplates
     */
    @Valid
    @Schema(name = "linkTemplates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("linkTemplates")
    @JacksonXmlProperty(localName = "linkTemplates")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "linkTemplates")
    public List<OgcApiRecordsLinkTemplateDto> getLinkTemplates() {
        return linkTemplates;
    }

    public void setLinkTemplates(List<OgcApiRecordsLinkTemplateDto> linkTemplates) {
        this.linkTemplates = linkTemplates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OgcApiRecordsRecordGeoJSONDto recordGeoJSON = (OgcApiRecordsRecordGeoJSONDto) o;
        return Objects.equals(this.id, recordGeoJSON.id)
                && Objects.equals(this.type, recordGeoJSON.type)
                && Objects.equals(this.time, recordGeoJSON.time)
                && Objects.equals(this.geometry, recordGeoJSON.geometry)
                && Objects.equals(this.properties, recordGeoJSON.properties)
                && Objects.equals(this.links, recordGeoJSON.links)
                && Objects.equals(this.linkTemplates, recordGeoJSON.linkTemplates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, time, geometry, properties, links, linkTemplates);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsRecordGeoJSONDto {\n"
                + "    id: "
                + toIndentedString(id)
                + "\n"
                + "    type: "
                + toIndentedString(type)
                + "\n"
                + "    time: "
                + toIndentedString(time)
                + "\n"
                + "    geometry: "
                + toIndentedString(geometry)
                + "\n"
                + "    properties: "
                + toIndentedString(properties)
                + "\n"
                + "    links: "
                + toIndentedString(links)
                + "\n"
                + "    linkTemplates: "
                + toIndentedString(linkTemplates)
                + "\n"
                + "}";
        return sb;
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
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

        private OgcApiRecordsRecordGeoJSONDto instance;

        public Builder() {
            this(new OgcApiRecordsRecordGeoJSONDto());
        }

        protected Builder(OgcApiRecordsRecordGeoJSONDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsRecordGeoJSONDto value) {
            this.instance.setId(value.id);
            this.instance.setType(value.type);
            this.instance.setTime(value.time);
            this.instance.setGeometry(value.geometry);
            this.instance.setProperties(value.properties);
            this.instance.setLinks(value.links);
            this.instance.setLinkTemplates(value.linkTemplates);
            return this;
        }

        public Builder id(String id) {
            this.instance.id(id);
            return this;
        }

        public Builder type(TypeEnum type) {
            this.instance.type(type);
            return this;
        }

        public Builder time(OgcApiRecordsRecordGeoJSONTimeDto time) {
            this.instance.time(time);
            return this;
        }

        public Builder geometry(OgcApiRecordsRecordGeoJSONGeometryDto geometry) {
            this.instance.geometry(geometry);
            return this;
        }

        public Builder properties(OgcApiRecordsRecordGeoJSONPropertiesDto properties) {
            this.instance.properties(properties);
            return this;
        }

        public Builder links(List<@Valid OgcApiRecordsLinkBaseDto> links) {
            this.instance.links(links);
            return this;
        }

        public Builder linkTemplates(List<OgcApiRecordsLinkTemplateDto> linkTemplates) {
            this.instance.linkTemplates(linkTemplates);
            return this;
        }

        /**
         * returns a built OgcApiRecordsRecordGeoJSONDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsRecordGeoJSONDto build() {
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
