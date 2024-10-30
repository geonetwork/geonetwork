/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

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
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** GdalGeoJSONGeometryCollectionDto */
@JsonTypeName("GeoJSON_GeometryCollection")
@JacksonXmlRootElement(localName = "GdalGeoJSONGeometryCollectionDto")
@XmlRootElement(name = "GdalGeoJSONGeometryCollectionDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalGeoJSONGeometryCollectionDto implements GdalFeatureGeometryDto {

    /** Gets or Sets type */
    public enum TypeEnum {
        GEOMETRY_COLLECTION("GeometryCollection");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
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
    }

    private TypeEnum type;

    @Valid
    private List<GdalGeoJSONGeometryCollectionGeometriesInnerDto> geometries = new ArrayList<>();

    @Valid
    private List<BigDecimal> bbox = new ArrayList<>();

    public GdalGeoJSONGeometryCollectionDto() {
        super();
    }

    /** Constructor with only required parameters */
    public GdalGeoJSONGeometryCollectionDto(
            TypeEnum type, List<GdalGeoJSONGeometryCollectionGeometriesInnerDto> geometries) {
        this.type = type;
        this.geometries = geometries;
    }

    public GdalGeoJSONGeometryCollectionDto type(TypeEnum type) {
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

    public GdalGeoJSONGeometryCollectionDto geometries(
            List<GdalGeoJSONGeometryCollectionGeometriesInnerDto> geometries) {
        this.geometries = geometries;
        return this;
    }

    public GdalGeoJSONGeometryCollectionDto addGeometriesItem(
            GdalGeoJSONGeometryCollectionGeometriesInnerDto geometriesItem) {
        if (this.geometries == null) {
            this.geometries = new ArrayList<>();
        }
        this.geometries.add(geometriesItem);
        return this;
    }

    /**
     * Get geometries
     *
     * @return geometries
     */
    @NotNull
    @Valid
    @Schema(name = "geometries", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("geometries")
    @JacksonXmlProperty(localName = "geometries")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "geometries")
    public List<GdalGeoJSONGeometryCollectionGeometriesInnerDto> getGeometries() {
        return geometries;
    }

    public void setGeometries(List<GdalGeoJSONGeometryCollectionGeometriesInnerDto> geometries) {
        this.geometries = geometries;
    }

    public GdalGeoJSONGeometryCollectionDto bbox(List<BigDecimal> bbox) {
        this.bbox = bbox;
        return this;
    }

    public GdalGeoJSONGeometryCollectionDto addBboxItem(BigDecimal bboxItem) {
        if (this.bbox == null) {
            this.bbox = new ArrayList<>();
        }
        this.bbox.add(bboxItem);
        return this;
    }

    /**
     * Get bbox
     *
     * @return bbox
     */
    @Valid
    @Size(min = 4)
    @Schema(name = "bbox", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("bbox")
    @JacksonXmlProperty(localName = "bbox")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "bbox")
    public List<BigDecimal> getBbox() {
        return bbox;
    }

    public void setBbox(List<BigDecimal> bbox) {
        this.bbox = bbox;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalGeoJSONGeometryCollectionDto geoJSONGeometryCollection = (GdalGeoJSONGeometryCollectionDto) o;
        return Objects.equals(this.type, geoJSONGeometryCollection.type)
                && Objects.equals(this.geometries, geoJSONGeometryCollection.geometries)
                && Objects.equals(this.bbox, geoJSONGeometryCollection.bbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, geometries, bbox);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalGeoJSONGeometryCollectionDto {\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    geometries: ").append(toIndentedString(geometries)).append("\n");
        sb.append("    bbox: ").append(toIndentedString(bbox)).append("\n");
        sb.append("}");
        return sb.toString();
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

    public static class Builder {

        private GdalGeoJSONGeometryCollectionDto instance;

        public Builder() {
            this(new GdalGeoJSONGeometryCollectionDto());
        }

        protected Builder(GdalGeoJSONGeometryCollectionDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalGeoJSONGeometryCollectionDto value) {
            this.instance.setType(value.type);
            this.instance.setGeometries(value.geometries);
            this.instance.setBbox(value.bbox);
            return this;
        }

        public Builder type(TypeEnum type) {
            this.instance.type(type);
            return this;
        }

        public Builder geometries(List<GdalGeoJSONGeometryCollectionGeometriesInnerDto> geometries) {
            this.instance.geometries(geometries);
            return this;
        }

        public Builder bbox(List<BigDecimal> bbox) {
            this.instance.bbox(bbox);
            return this;
        }

        /**
         * returns a built GdalGeoJSONGeometryCollectionDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalGeoJSONGeometryCollectionDto build() {
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

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    /** Create a builder with a shallow copy of this instance. */
    public Builder toBuilder() {
        Builder builder = new Builder();
        return builder.copyOf(this);
    }
}
