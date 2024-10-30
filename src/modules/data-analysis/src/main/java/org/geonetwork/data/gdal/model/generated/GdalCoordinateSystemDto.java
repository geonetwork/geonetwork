/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
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

/** GdalCoordinateSystemDto */
@JsonTypeName("coordinateSystem")
@JacksonXmlRootElement(localName = "GdalCoordinateSystemDto")
@XmlRootElement(name = "GdalCoordinateSystemDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:57:35.681169130+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalCoordinateSystemDto {

    private String wkt;

    @Valid
    private List<BigDecimal> dataAxisToSRSAxisMapping = new ArrayList<>();

    private BigDecimal coordinateEpoch;

    public GdalCoordinateSystemDto() {
        super();
    }

    /** Constructor with only required parameters */
    public GdalCoordinateSystemDto(String wkt, List<BigDecimal> dataAxisToSRSAxisMapping) {
        this.wkt = wkt;
        this.dataAxisToSRSAxisMapping = dataAxisToSRSAxisMapping;
    }

    public GdalCoordinateSystemDto wkt(String wkt) {
        this.wkt = wkt;
        return this;
    }

    /**
     * Get wkt
     *
     * @return wkt
     */
    @NotNull
    @Schema(name = "wkt", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("wkt")
    @JacksonXmlProperty(localName = "wkt")
    @XmlElement(name = "wkt")
    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    public GdalCoordinateSystemDto dataAxisToSRSAxisMapping(List<BigDecimal> dataAxisToSRSAxisMapping) {
        this.dataAxisToSRSAxisMapping = dataAxisToSRSAxisMapping;
        return this;
    }

    public GdalCoordinateSystemDto addDataAxisToSRSAxisMappingItem(BigDecimal dataAxisToSRSAxisMappingItem) {
        if (this.dataAxisToSRSAxisMapping == null) {
            this.dataAxisToSRSAxisMapping = new ArrayList<>();
        }
        this.dataAxisToSRSAxisMapping.add(dataAxisToSRSAxisMappingItem);
        return this;
    }

    /**
     * Get dataAxisToSRSAxisMapping
     *
     * @return dataAxisToSRSAxisMapping
     */
    @NotNull
    @Valid
    @Schema(name = "dataAxisToSRSAxisMapping", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("dataAxisToSRSAxisMapping")
    @JacksonXmlProperty(localName = "dataAxisToSRSAxisMapping")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "dataAxisToSRSAxisMapping")
    public List<BigDecimal> getDataAxisToSRSAxisMapping() {
        return dataAxisToSRSAxisMapping;
    }

    public void setDataAxisToSRSAxisMapping(List<BigDecimal> dataAxisToSRSAxisMapping) {
        this.dataAxisToSRSAxisMapping = dataAxisToSRSAxisMapping;
    }

    public GdalCoordinateSystemDto coordinateEpoch(BigDecimal coordinateEpoch) {
        this.coordinateEpoch = coordinateEpoch;
        return this;
    }

    /**
     * Get coordinateEpoch
     *
     * @return coordinateEpoch
     */
    @Valid
    @Schema(name = "coordinateEpoch", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("coordinateEpoch")
    @JacksonXmlProperty(localName = "coordinateEpoch")
    @XmlElement(name = "coordinateEpoch")
    public BigDecimal getCoordinateEpoch() {
        return coordinateEpoch;
    }

    public void setCoordinateEpoch(BigDecimal coordinateEpoch) {
        this.coordinateEpoch = coordinateEpoch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalCoordinateSystemDto coordinateSystem = (GdalCoordinateSystemDto) o;
        return Objects.equals(this.wkt, coordinateSystem.wkt)
                && Objects.equals(this.dataAxisToSRSAxisMapping, coordinateSystem.dataAxisToSRSAxisMapping)
                && Objects.equals(this.coordinateEpoch, coordinateSystem.coordinateEpoch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wkt, dataAxisToSRSAxisMapping, coordinateEpoch);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalCoordinateSystemDto {\n");
        sb.append("    wkt: ").append(toIndentedString(wkt)).append("\n");
        sb.append("    dataAxisToSRSAxisMapping: ")
                .append(toIndentedString(dataAxisToSRSAxisMapping))
                .append("\n");
        sb.append("    coordinateEpoch: ")
                .append(toIndentedString(coordinateEpoch))
                .append("\n");
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

        private GdalCoordinateSystemDto instance;

        public Builder() {
            this(new GdalCoordinateSystemDto());
        }

        protected Builder(GdalCoordinateSystemDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalCoordinateSystemDto value) {
            this.instance.setWkt(value.wkt);
            this.instance.setDataAxisToSRSAxisMapping(value.dataAxisToSRSAxisMapping);
            this.instance.setCoordinateEpoch(value.coordinateEpoch);
            return this;
        }

        public Builder wkt(String wkt) {
            this.instance.wkt(wkt);
            return this;
        }

        public Builder dataAxisToSRSAxisMapping(List<BigDecimal> dataAxisToSRSAxisMapping) {
            this.instance.dataAxisToSRSAxisMapping(dataAxisToSRSAxisMapping);
            return this;
        }

        public Builder coordinateEpoch(BigDecimal coordinateEpoch) {
            this.instance.coordinateEpoch(coordinateEpoch);
            return this;
        }

        /**
         * returns a built GdalCoordinateSystemDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalCoordinateSystemDto build() {
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
