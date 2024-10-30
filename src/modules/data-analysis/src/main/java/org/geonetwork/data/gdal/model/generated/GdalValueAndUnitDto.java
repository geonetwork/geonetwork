/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Objects;

/** GdalValueAndUnitDto */
@JsonTypeName("value_and_unit")
@JacksonXmlRootElement(localName = "GdalValueAndUnitDto")
@XmlRootElement(name = "GdalValueAndUnitDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalValueAndUnitDto implements GdalValueInDegreeOrValueAndUnitDto {

    private BigDecimal value;

    private Object unit = null;

    public GdalValueAndUnitDto() {
        super();
    }

    /** Constructor with only required parameters */
    public GdalValueAndUnitDto(BigDecimal value, Object unit) {
        this.value = value;
        this.unit = unit;
    }

    public GdalValueAndUnitDto value(BigDecimal value) {
        this.value = value;
        return this;
    }

    /**
     * Get value
     *
     * @return value
     */
    @NotNull
    @Valid
    @Schema(name = "value", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value")
    @XmlElement(name = "value")
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public GdalValueAndUnitDto unit(Object unit) {
        this.unit = unit;
        return this;
    }

    /**
     * Get unit
     *
     * @return unit
     */
    @NotNull
    @Schema(name = "unit", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("unit")
    @JacksonXmlProperty(localName = "unit")
    @XmlElement(name = "unit")
    public Object getUnit() {
        return unit;
    }

    public void setUnit(Object unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalValueAndUnitDto valueAndUnit = (GdalValueAndUnitDto) o;
        return Objects.equals(this.value, valueAndUnit.value) && Objects.equals(this.unit, valueAndUnit.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalValueAndUnitDto {\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
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

        private GdalValueAndUnitDto instance;

        public Builder() {
            this(new GdalValueAndUnitDto());
        }

        protected Builder(GdalValueAndUnitDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalValueAndUnitDto value) {
            this.instance.setValue(value.value);
            this.instance.setUnit(value.unit);
            return this;
        }

        public Builder value(BigDecimal value) {
            this.instance.value(value);
            return this;
        }

        public Builder unit(Object unit) {
            this.instance.unit(unit);
            return this;
        }

        /**
         * returns a built GdalValueAndUnitDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalValueAndUnitDto build() {
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
