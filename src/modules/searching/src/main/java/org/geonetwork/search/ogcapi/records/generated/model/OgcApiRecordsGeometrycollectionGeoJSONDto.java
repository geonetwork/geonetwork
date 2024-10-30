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

/** OgcApiRecordsGeometrycollectionGeoJSONDto */
@JsonTypeName("geometrycollectionGeoJSON")
@JacksonXmlRootElement(localName = "OgcApiRecordsGeometrycollectionGeoJSONDto")
@XmlRootElement(name = "OgcApiRecordsGeometrycollectionGeoJSONDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsGeometrycollectionGeoJSONDto implements OgcApiRecordsGeometryGeoJSONDto {

    private TypeEnum type;

    @Valid
    private List<OgcApiRecordsGeometryGeoJSONDto> geometries = new ArrayList<>();

    public OgcApiRecordsGeometrycollectionGeoJSONDto() {
        super();
    }

    /** Constructor with only required parameters */
    public OgcApiRecordsGeometrycollectionGeoJSONDto(TypeEnum type, List<OgcApiRecordsGeometryGeoJSONDto> geometries) {
        this.type = type;
        this.geometries = geometries;
    }

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsGeometrycollectionGeoJSONDto type(TypeEnum type) {
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

    public OgcApiRecordsGeometrycollectionGeoJSONDto geometries(List<OgcApiRecordsGeometryGeoJSONDto> geometries) {
        this.geometries = geometries;
        return this;
    }

    public OgcApiRecordsGeometrycollectionGeoJSONDto addGeometriesItem(OgcApiRecordsGeometryGeoJSONDto geometriesItem) {
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
    public List<OgcApiRecordsGeometryGeoJSONDto> getGeometries() {
        return geometries;
    }

    public void setGeometries(List<OgcApiRecordsGeometryGeoJSONDto> geometries) {
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
        OgcApiRecordsGeometrycollectionGeoJSONDto geometrycollectionGeoJSON =
                (OgcApiRecordsGeometrycollectionGeoJSONDto) o;
        return Objects.equals(this.type, geometrycollectionGeoJSON.type)
                && Objects.equals(this.geometries, geometrycollectionGeoJSON.geometries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, geometries);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsGeometrycollectionGeoJSONDto {\n"
                + "    type: "
                + toIndentedString(type)
                + "\n"
                + "    geometries: "
                + toIndentedString(geometries)
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
        GEOMETRY_COLLECTION("GeometryCollection");

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

        private OgcApiRecordsGeometrycollectionGeoJSONDto instance;

        public Builder() {
            this(new OgcApiRecordsGeometrycollectionGeoJSONDto());
        }

        protected Builder(OgcApiRecordsGeometrycollectionGeoJSONDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsGeometrycollectionGeoJSONDto value) {
            this.instance.setType(value.type);
            this.instance.setGeometries(value.geometries);
            return this;
        }

        public Builder type(TypeEnum type) {
            this.instance.type(type);
            return this;
        }

        public Builder geometries(List<OgcApiRecordsGeometryGeoJSONDto> geometries) {
            this.instance.geometries(geometries);
            return this;
        }

        /**
         * returns a built OgcApiRecordsGeometrycollectionGeoJSONDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsGeometrycollectionGeoJSONDto build() {
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
