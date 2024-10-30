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
import jakarta.xml.bind.annotation.*;
import java.util.Objects;

/** GdalOgrinfoDto */
@JsonTypeName("ogrinfo")
@JacksonXmlRootElement(localName = "GdalOgrinfoDto")
@XmlRootElement(name = "GdalOgrinfoDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalOgrinfoDto {

    private GdalOgrinfoDatasetDto dataset;

    public GdalOgrinfoDto dataset(GdalOgrinfoDatasetDto dataset) {
        this.dataset = dataset;
        return this;
    }

    /**
     * Get dataset
     *
     * @return dataset
     */
    @Valid
    @Schema(name = "dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("dataset")
    @JacksonXmlProperty(localName = "dataset")
    @XmlElement(name = "dataset")
    public GdalOgrinfoDatasetDto getDataset() {
        return dataset;
    }

    public void setDataset(GdalOgrinfoDatasetDto dataset) {
        this.dataset = dataset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalOgrinfoDto ogrinfo = (GdalOgrinfoDto) o;
        return Objects.equals(this.dataset, ogrinfo.dataset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataset);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalOgrinfoDto {\n");
        sb.append("    dataset: ").append(toIndentedString(dataset)).append("\n");
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

        private GdalOgrinfoDto instance;

        public Builder() {
            this(new GdalOgrinfoDto());
        }

        protected Builder(GdalOgrinfoDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalOgrinfoDto value) {
            this.instance.setDataset(value.dataset);
            return this;
        }

        public Builder dataset(GdalOgrinfoDatasetDto dataset) {
            this.instance.dataset(dataset);
            return this;
        }

        /**
         * returns a built GdalOgrinfoDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalOgrinfoDto build() {
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
