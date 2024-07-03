package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RecordGeoJSONDto
 */

@JsonTypeName("recordGeoJSON")
@JacksonXmlRootElement(localName = "RecordGeoJSONDto")
@XmlRootElement(name = "RecordGeoJSONDto")
@XmlAccessorType(XmlAccessType.FIELD)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]", comments = "Generator version: 7.6.0")
public class RecordGeoJSONDto {

    private String id;
    private TypeEnum type;
    private RecordGeoJSONTimeDto time;
    private RecordGeoJSONGeometryDto geometry;
    private RecordGeoJSONPropertiesDto properties;
    @Valid
    private List<@Valid LinkBaseDto> links = new ArrayList<>();
    @Valid
    private List<LinkTemplateDto> linkTemplates = new ArrayList<>();

    public RecordGeoJSONDto() {
        super();
    }

    /**
     * Constructor with only required parameters
     */
    public RecordGeoJSONDto(String id, TypeEnum type, RecordGeoJSONGeometryDto geometry, RecordGeoJSONPropertiesDto properties) {
        this.id = id;
        this.type = type;
        this.geometry = geometry;
        this.properties = properties;
    }

    /**
     * Create a builder with no initialized field (except for the default values).
     */
    public static Builder builder() {
        return new Builder();
    }

    public RecordGeoJSONDto id(String id) {
        this.id = id;
        return this;
    }

    /**
     * A unique identifier of the catalog record.
     *
     * @return id
     */
    @NotNull
    @Schema(name = "id", description = "A unique identifier of the catalog record.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RecordGeoJSONDto type(TypeEnum type) {
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
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public RecordGeoJSONDto time(RecordGeoJSONTimeDto time) {
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
    public RecordGeoJSONTimeDto getTime() {
        return time;
    }

    public void setTime(RecordGeoJSONTimeDto time) {
        this.time = time;
    }

    public RecordGeoJSONDto geometry(RecordGeoJSONGeometryDto geometry) {
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
    public RecordGeoJSONGeometryDto getGeometry() {
        return geometry;
    }

    public void setGeometry(RecordGeoJSONGeometryDto geometry) {
        this.geometry = geometry;
    }

    public RecordGeoJSONDto properties(RecordGeoJSONPropertiesDto properties) {
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
    public RecordGeoJSONPropertiesDto getProperties() {
        return properties;
    }

    public void setProperties(RecordGeoJSONPropertiesDto properties) {
        this.properties = properties;
    }

    public RecordGeoJSONDto links(List<@Valid LinkBaseDto> links) {
        this.links = links;
        return this;
    }

    public RecordGeoJSONDto addLinksItem(LinkBaseDto linksItem) {
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
    public List<@Valid LinkBaseDto> getLinks() {
        return links;
    }

    public void setLinks(List<@Valid LinkBaseDto> links) {
        this.links = links;
    }

    public RecordGeoJSONDto linkTemplates(List<LinkTemplateDto> linkTemplates) {
        this.linkTemplates = linkTemplates;
        return this;
    }

    public RecordGeoJSONDto addLinkTemplatesItem(LinkTemplateDto linkTemplatesItem) {
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
    public List<LinkTemplateDto> getLinkTemplates() {
        return linkTemplates;
    }

    public void setLinkTemplates(List<LinkTemplateDto> linkTemplates) {
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
        RecordGeoJSONDto recordGeoJSON = (RecordGeoJSONDto) o;
        return Objects.equals(this.id, recordGeoJSON.id) &&
            Objects.equals(this.type, recordGeoJSON.type) &&
            Objects.equals(this.time, recordGeoJSON.time) &&
            Objects.equals(this.geometry, recordGeoJSON.geometry) &&
            Objects.equals(this.properties, recordGeoJSON.properties) &&
            Objects.equals(this.links, recordGeoJSON.links) &&
            Objects.equals(this.linkTemplates, recordGeoJSON.linkTemplates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, time, geometry, properties, links, linkTemplates);
    }

    @Override
    public String toString() {
        String sb = "class RecordGeoJSONDto {\n" +
            "    id: " + toIndentedString(id) + "\n" +
            "    type: " + toIndentedString(type) + "\n" +
            "    time: " + toIndentedString(time) + "\n" +
            "    geometry: " + toIndentedString(geometry) + "\n" +
            "    properties: " + toIndentedString(properties) + "\n" +
            "    links: " + toIndentedString(links) + "\n" +
            "    linkTemplates: " + toIndentedString(linkTemplates) + "\n" +
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

    /**
     * Gets or Sets type
     */
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

        private RecordGeoJSONDto instance;

        public Builder() {
            this(new RecordGeoJSONDto());
        }

        protected Builder(RecordGeoJSONDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(RecordGeoJSONDto value) {
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

        public Builder time(RecordGeoJSONTimeDto time) {
            this.instance.time(time);
            return this;
        }

        public Builder geometry(RecordGeoJSONGeometryDto geometry) {
            this.instance.geometry(geometry);
            return this;
        }

        public Builder properties(RecordGeoJSONPropertiesDto properties) {
            this.instance.properties(properties);
            return this;
        }

        public Builder links(List<@Valid LinkBaseDto> links) {
            this.instance.links(links);
            return this;
        }

        public Builder linkTemplates(List<LinkTemplateDto> linkTemplates) {
            this.instance.linkTemplates(linkTemplates);
            return this;
        }

        /**
         * returns a built RecordGeoJSONDto instance.
         * <p>
         * The builder is not reusable (NullPointerException)
         */
        public RecordGeoJSONDto build() {
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

