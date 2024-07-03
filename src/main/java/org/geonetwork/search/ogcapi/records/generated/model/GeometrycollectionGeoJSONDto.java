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
 * GeometrycollectionGeoJSONDto
 */

@JsonTypeName("geometrycollectionGeoJSON")
@JacksonXmlRootElement(localName = "GeometrycollectionGeoJSONDto")
@XmlRootElement(name = "GeometrycollectionGeoJSONDto")
@XmlAccessorType(XmlAccessType.FIELD)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]", comments = "Generator version: 7.6.0")
public class GeometrycollectionGeoJSONDto implements GeometryGeoJSONDto {

    private TypeEnum type;
    @Valid
    private List<GeometryGeoJSONDto> geometries = new ArrayList<>();

    public GeometrycollectionGeoJSONDto() {
        super();
    }

    /**
     * Constructor with only required parameters
     */
    public GeometrycollectionGeoJSONDto(TypeEnum type, List<GeometryGeoJSONDto> geometries) {
        this.type = type;
        this.geometries = geometries;
    }

    /**
     * Create a builder with no initialized field (except for the default values).
     */
    public static Builder builder() {
        return new Builder();
    }

    public GeometrycollectionGeoJSONDto type(TypeEnum type) {
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

    public GeometrycollectionGeoJSONDto geometries(List<GeometryGeoJSONDto> geometries) {
        this.geometries = geometries;
        return this;
    }

    public GeometrycollectionGeoJSONDto addGeometriesItem(GeometryGeoJSONDto geometriesItem) {
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
    public List<GeometryGeoJSONDto> getGeometries() {
        return geometries;
    }

    public void setGeometries(List<GeometryGeoJSONDto> geometries) {
        this.geometries = geometries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeometrycollectionGeoJSONDto geometrycollectionGeoJSON = (GeometrycollectionGeoJSONDto) o;
        return Objects.equals(this.type, geometrycollectionGeoJSON.type) &&
            Objects.equals(this.geometries, geometrycollectionGeoJSON.geometries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, geometries);
    }

    @Override
    public String toString() {
        String sb = "class GeometrycollectionGeoJSONDto {\n" +
            "    type: " + toIndentedString(type) + "\n" +
            "    geometries: " + toIndentedString(geometries) + "\n" +
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
        GEOMETRYCOLLECTION("GeometryCollection");

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

        private GeometrycollectionGeoJSONDto instance;

        public Builder() {
            this(new GeometrycollectionGeoJSONDto());
        }

        protected Builder(GeometrycollectionGeoJSONDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GeometrycollectionGeoJSONDto value) {
            this.instance.setType(value.type);
            this.instance.setGeometries(value.geometries);
            return this;
        }

        public Builder type(TypeEnum type) {
            this.instance.type(type);
            return this;
        }

        public Builder geometries(List<GeometryGeoJSONDto> geometries) {
            this.instance.geometries(geometries);
            return this;
        }

        /**
         * returns a built GeometrycollectionGeoJSONDto instance.
         * <p>
         * The builder is not reusable (NullPointerException)
         */
        public GeometrycollectionGeoJSONDto build() {
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

