package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

/**
 * The extent of the features in the collection. In the Core only spatial and temporal extents are specified. Extensions may add additional members to represent other extents, for example, thermal or pressure ranges.  An array of extents is provided for each extent type (spatial, temporal). The first item in the array describes the overall extent of the data. All subsequent items describe more precise extents, e.g., to identify clusters of data. Clients only interested in the overall extent will only need to access the first extent in the array.
 */

@Schema(name = "extent", description = "The extent of the features in the collection. In the Core only spatial and temporal extents are specified. Extensions may add additional members to represent other extents, for example, thermal or pressure ranges.  An array of extents is provided for each extent type (spatial, temporal). The first item in the array describes the overall extent of the data. All subsequent items describe more precise extents, e.g., to identify clusters of data. Clients only interested in the overall extent will only need to access the first extent in the array.")
@JsonTypeName("extent")
@JacksonXmlRootElement(localName = "ExtentDto")
@XmlRootElement(name = "ExtentDto")
@XmlAccessorType(XmlAccessType.FIELD)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]", comments = "Generator version: 7.6.0")
public class ExtentDto {

    private ExtentSpatialDto spatial;

    private ExtentTemporalDto temporal;

    /**
     * Create a builder with no initialized field (except for the default values).
     */
    public static Builder builder() {
        return new Builder();
    }

    public ExtentDto spatial(ExtentSpatialDto spatial) {
        this.spatial = spatial;
        return this;
    }

    /**
     * Get spatial
     *
     * @return spatial
     */
    @Valid
    @Schema(name = "spatial", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("spatial")
    @JacksonXmlProperty(localName = "spatial")
    public ExtentSpatialDto getSpatial() {
        return spatial;
    }

    public void setSpatial(ExtentSpatialDto spatial) {
        this.spatial = spatial;
    }

    public ExtentDto temporal(ExtentTemporalDto temporal) {
        this.temporal = temporal;
        return this;
    }

    /**
     * Get temporal
     *
     * @return temporal
     */
    @Valid
    @Schema(name = "temporal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("temporal")
    @JacksonXmlProperty(localName = "temporal")
    public ExtentTemporalDto getTemporal() {
        return temporal;
    }

    public void setTemporal(ExtentTemporalDto temporal) {
        this.temporal = temporal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExtentDto extent = (ExtentDto) o;
        return Objects.equals(this.spatial, extent.spatial) &&
            Objects.equals(this.temporal, extent.temporal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spatial, temporal);
    }

    @Override
    public String toString() {
        String sb = "class ExtentDto {\n" +
            "    spatial: " + toIndentedString(spatial) + "\n" +
            "    temporal: " + toIndentedString(temporal) + "\n" +
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

    public static class Builder {

        private ExtentDto instance;

        public Builder() {
            this(new ExtentDto());
        }

        protected Builder(ExtentDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(ExtentDto value) {
            this.instance.setSpatial(value.spatial);
            this.instance.setTemporal(value.temporal);
            return this;
        }

        public Builder spatial(ExtentSpatialDto spatial) {
            this.instance.spatial(spatial);
            return this;
        }

        public Builder temporal(ExtentTemporalDto temporal) {
            this.instance.temporal(temporal);
            return this;
        }

        /**
         * returns a built ExtentDto instance.
         * <p>
         * The builder is not reusable (NullPointerException)
         */
        public ExtentDto build() {
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

