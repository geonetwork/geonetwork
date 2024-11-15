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

/** GdalGeoJSONPolygonDto */
@JsonTypeName("GeoJSON_Polygon")
@JacksonXmlRootElement(localName = "GdalGeoJSONPolygonDto")
@XmlRootElement(name = "GdalGeoJSONPolygonDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalGeoJSONPolygonDto
        implements GdalFeatureGeometryDto, GdalGeoJSONGeometryCollectionGeometriesInnerDto, GdalGeometryDto {

    /** Gets or Sets type */
    public enum TypeEnum {
        POLYGON("Polygon");

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
    private List<List<List<BigDecimal>>> coordinates = new ArrayList<>();

    @Valid
    private List<BigDecimal> bbox = new ArrayList<>();

    public GdalGeoJSONPolygonDto() {
        super();
    }

    /** Constructor with only required parameters */
    public GdalGeoJSONPolygonDto(TypeEnum type, List<List<List<BigDecimal>>> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public GdalGeoJSONPolygonDto type(TypeEnum type) {
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

    public GdalGeoJSONPolygonDto coordinates(List<List<List<BigDecimal>>> coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public GdalGeoJSONPolygonDto addCoordinatesItem(List<List<BigDecimal>> coordinatesItem) {
        if (this.coordinates == null) {
            this.coordinates = new ArrayList<>();
        }
        this.coordinates.add(coordinatesItem);
        return this;
    }

    /**
     * Get coordinates
     *
     * @return coordinates
     */
    @NotNull
    @Valid
    @Schema(name = "coordinates", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("coordinates")
    @JacksonXmlProperty(localName = "coordinates")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "coordinates")
    public List<List<List<BigDecimal>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<BigDecimal>>> coordinates) {
        this.coordinates = coordinates;
    }

    public GdalGeoJSONPolygonDto bbox(List<BigDecimal> bbox) {
        this.bbox = bbox;
        return this;
    }

    public GdalGeoJSONPolygonDto addBboxItem(BigDecimal bboxItem) {
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
        GdalGeoJSONPolygonDto geoJSONPolygon = (GdalGeoJSONPolygonDto) o;
        return Objects.equals(this.type, geoJSONPolygon.type)
                && Objects.equals(this.coordinates, geoJSONPolygon.coordinates)
                && Objects.equals(this.bbox, geoJSONPolygon.bbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, coordinates, bbox);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalGeoJSONPolygonDto {\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
        sb.append("    bbox: ").append(toIndentedString(bbox)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /** Convert the given object to string with each line indented by 4 spaces (except the first line). */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    public static class Builder {

        private GdalGeoJSONPolygonDto instance;

        public Builder() {
            this(new GdalGeoJSONPolygonDto());
        }

        protected Builder(GdalGeoJSONPolygonDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalGeoJSONPolygonDto value) {
            this.instance.setType(value.type);
            this.instance.setCoordinates(value.coordinates);
            this.instance.setBbox(value.bbox);
            return this;
        }

        public Builder type(TypeEnum type) {
            this.instance.type(type);
            return this;
        }

        public Builder coordinates(List<List<List<BigDecimal>>> coordinates) {
            this.instance.coordinates(coordinates);
            return this;
        }

        public Builder bbox(List<BigDecimal> bbox) {
            this.instance.bbox(bbox);
            return this;
        }

        /**
         * returns a built GdalGeoJSONPolygonDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalGeoJSONPolygonDto build() {
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
