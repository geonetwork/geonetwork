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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Objects;

/** GdalBandDto */
@JsonTypeName("Band")
@JacksonXmlRootElement(localName = "GdalBandDto")
@XmlRootElement(name = "GdalBandDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalBandDto {

    private String name;

    /** Gets or Sets commonName */
    public enum CommonNameEnum {
        COASTAL("coastal"),

        BLUE("blue"),

        GREEN("green"),

        RED("red"),

        REDEDGE("rededge"),

        YELLOW("yellow"),

        PAN("pan"),

        NIR("nir"),

        NIR08("nir08"),

        NIR09("nir09"),

        CIRRUS("cirrus"),

        SWIR16("swir16"),

        SWIR22("swir22"),

        LWIR("lwir"),

        LWIR11("lwir11"),

        LWIR12("lwir12");

        private String value;

        CommonNameEnum(String value) {
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
        public static CommonNameEnum fromValue(String value) {
            for (CommonNameEnum b : CommonNameEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    private CommonNameEnum commonName;

    private String description;

    private BigDecimal centerWavelength;

    private BigDecimal fullWidthHalfMax;

    private BigDecimal solarIllumination;

    public GdalBandDto name(String name) {
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
    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GdalBandDto commonName(CommonNameEnum commonName) {
        this.commonName = commonName;
        return this;
    }

    /**
     * Get commonName
     *
     * @return commonName
     */
    @Schema(name = "common_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("common_name")
    @JacksonXmlProperty(localName = "common_name")
    @XmlElement(name = "common_name")
    public CommonNameEnum getCommonName() {
        return commonName;
    }

    public void setCommonName(CommonNameEnum commonName) {
        this.commonName = commonName;
    }

    public GdalBandDto description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     *
     * @return description
     */
    @Size(min = 1)
    @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("description")
    @JacksonXmlProperty(localName = "description")
    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GdalBandDto centerWavelength(BigDecimal centerWavelength) {
        this.centerWavelength = centerWavelength;
        return this;
    }

    /**
     * Get centerWavelength
     *
     * @return centerWavelength
     */
    @Valid
    @Schema(name = "center_wavelength", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("center_wavelength")
    @JacksonXmlProperty(localName = "center_wavelength")
    @XmlElement(name = "center_wavelength")
    public BigDecimal getCenterWavelength() {
        return centerWavelength;
    }

    public void setCenterWavelength(BigDecimal centerWavelength) {
        this.centerWavelength = centerWavelength;
    }

    public GdalBandDto fullWidthHalfMax(BigDecimal fullWidthHalfMax) {
        this.fullWidthHalfMax = fullWidthHalfMax;
        return this;
    }

    /**
     * Get fullWidthHalfMax
     *
     * @return fullWidthHalfMax
     */
    @Valid
    @Schema(name = "full_width_half_max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("full_width_half_max")
    @JacksonXmlProperty(localName = "full_width_half_max")
    @XmlElement(name = "full_width_half_max")
    public BigDecimal getFullWidthHalfMax() {
        return fullWidthHalfMax;
    }

    public void setFullWidthHalfMax(BigDecimal fullWidthHalfMax) {
        this.fullWidthHalfMax = fullWidthHalfMax;
    }

    public GdalBandDto solarIllumination(BigDecimal solarIllumination) {
        this.solarIllumination = solarIllumination;
        return this;
    }

    /**
     * Get solarIllumination minimum: 0
     *
     * @return solarIllumination
     */
    @Valid
    @DecimalMin("0")
    @Schema(name = "solar_illumination", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("solar_illumination")
    @JacksonXmlProperty(localName = "solar_illumination")
    @XmlElement(name = "solar_illumination")
    public BigDecimal getSolarIllumination() {
        return solarIllumination;
    }

    public void setSolarIllumination(BigDecimal solarIllumination) {
        this.solarIllumination = solarIllumination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalBandDto band = (GdalBandDto) o;
        return Objects.equals(this.name, band.name)
                && Objects.equals(this.commonName, band.commonName)
                && Objects.equals(this.description, band.description)
                && Objects.equals(this.centerWavelength, band.centerWavelength)
                && Objects.equals(this.fullWidthHalfMax, band.fullWidthHalfMax)
                && Objects.equals(this.solarIllumination, band.solarIllumination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, commonName, description, centerWavelength, fullWidthHalfMax, solarIllumination);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalBandDto {\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    commonName: ").append(toIndentedString(commonName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    centerWavelength: ")
                .append(toIndentedString(centerWavelength))
                .append("\n");
        sb.append("    fullWidthHalfMax: ")
                .append(toIndentedString(fullWidthHalfMax))
                .append("\n");
        sb.append("    solarIllumination: ")
                .append(toIndentedString(solarIllumination))
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

        private GdalBandDto instance;

        public Builder() {
            this(new GdalBandDto());
        }

        protected Builder(GdalBandDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalBandDto value) {
            this.instance.setName(value.name);
            this.instance.setCommonName(value.commonName);
            this.instance.setDescription(value.description);
            this.instance.setCenterWavelength(value.centerWavelength);
            this.instance.setFullWidthHalfMax(value.fullWidthHalfMax);
            this.instance.setSolarIllumination(value.solarIllumination);
            return this;
        }

        public Builder name(String name) {
            this.instance.name(name);
            return this;
        }

        public Builder commonName(CommonNameEnum commonName) {
            this.instance.commonName(commonName);
            return this;
        }

        public Builder description(String description) {
            this.instance.description(description);
            return this;
        }

        public Builder centerWavelength(BigDecimal centerWavelength) {
            this.instance.centerWavelength(centerWavelength);
            return this;
        }

        public Builder fullWidthHalfMax(BigDecimal fullWidthHalfMax) {
            this.instance.fullWidthHalfMax(fullWidthHalfMax);
            return this;
        }

        public Builder solarIllumination(BigDecimal solarIllumination) {
            this.instance.solarIllumination(solarIllumination);
            return this;
        }

        /**
         * returns a built GdalBandDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalBandDto build() {
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
