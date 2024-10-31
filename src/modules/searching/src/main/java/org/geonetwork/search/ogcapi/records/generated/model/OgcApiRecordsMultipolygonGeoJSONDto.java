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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** OgcApiRecordsMultipolygonGeoJSONDto */
@JsonTypeName("multipolygonGeoJSON")
@JacksonXmlRootElement(localName = "OgcApiRecordsMultipolygonGeoJSONDto")
@XmlRootElement(name = "OgcApiRecordsMultipolygonGeoJSONDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsMultipolygonGeoJSONDto implements OgcApiRecordsGeometryGeoJSONDto {

    private TypeEnum type;

    @Valid
    private List<List<List<List<BigDecimal>>>> coordinates = new ArrayList<>();

    public OgcApiRecordsMultipolygonGeoJSONDto() {
        super();
    }

    /** Constructor with only required parameters */
    public OgcApiRecordsMultipolygonGeoJSONDto(TypeEnum type, List<List<List<List<BigDecimal>>>> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsMultipolygonGeoJSONDto type(TypeEnum type) {
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

    public OgcApiRecordsMultipolygonGeoJSONDto coordinates(List<List<List<List<BigDecimal>>>> coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public OgcApiRecordsMultipolygonGeoJSONDto addCoordinatesItem(List<List<List<BigDecimal>>> coordinatesItem) {
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
    public List<List<List<List<BigDecimal>>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<List<BigDecimal>>>> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OgcApiRecordsMultipolygonGeoJSONDto multipolygonGeoJSON = (OgcApiRecordsMultipolygonGeoJSONDto) o;
        return Objects.equals(this.type, multipolygonGeoJSON.type)
                && Objects.equals(this.coordinates, multipolygonGeoJSON.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, coordinates);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsMultipolygonGeoJSONDto {\n"
                + "    type: "
                + toIndentedString(type)
                + "\n"
                + "    coordinates: "
                + toIndentedString(coordinates)
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
        MULTI_POLYGON("MultiPolygon");

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

        private OgcApiRecordsMultipolygonGeoJSONDto instance;

        public Builder() {
            this(new OgcApiRecordsMultipolygonGeoJSONDto());
        }

        protected Builder(OgcApiRecordsMultipolygonGeoJSONDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsMultipolygonGeoJSONDto value) {
            this.instance.setType(value.type);
            this.instance.setCoordinates(value.coordinates);
            return this;
        }

        public Builder type(TypeEnum type) {
            this.instance.type(type);
            return this;
        }

        public Builder coordinates(List<List<List<List<BigDecimal>>>> coordinates) {
            this.instance.coordinates(coordinates);
            return this;
        }

        /**
         * returns a built OgcApiRecordsMultipolygonGeoJSONDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsMultipolygonGeoJSONDto build() {
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
