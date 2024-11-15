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
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/** GdalGeometryFieldSupportedSRSListInnerOneOfDto */
@JsonTypeName("geometryField_supportedSRSList_inner_oneOf")
@JacksonXmlRootElement(localName = "GdalGeometryFieldSupportedSRSListInnerOneOfDto")
@XmlRootElement(name = "GdalGeometryFieldSupportedSRSListInnerOneOfDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
@NoArgsConstructor
@AllArgsConstructor
public class GdalGeometryFieldSupportedSRSListInnerOneOfDto implements GdalGeometryFieldSupportedSRSListInnerDto {

    private GdalGeometryFieldSupportedSRSListInnerOneOfIdDto id;

    public GdalGeometryFieldSupportedSRSListInnerOneOfDto id(GdalGeometryFieldSupportedSRSListInnerOneOfIdDto id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     */
    @Valid
    @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    @XmlElement(name = "id")
    public GdalGeometryFieldSupportedSRSListInnerOneOfIdDto getId() {
        return id;
    }

    public void setId(GdalGeometryFieldSupportedSRSListInnerOneOfIdDto id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalGeometryFieldSupportedSRSListInnerOneOfDto geometryFieldSupportedSRSListInnerOneOf =
                (GdalGeometryFieldSupportedSRSListInnerOneOfDto) o;
        return Objects.equals(this.id, geometryFieldSupportedSRSListInnerOneOf.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalGeometryFieldSupportedSRSListInnerOneOfDto {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

        private GdalGeometryFieldSupportedSRSListInnerOneOfDto instance;

        public Builder() {
            this(new GdalGeometryFieldSupportedSRSListInnerOneOfDto());
        }

        protected Builder(GdalGeometryFieldSupportedSRSListInnerOneOfDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalGeometryFieldSupportedSRSListInnerOneOfDto value) {
            this.instance.setId(value.id);
            return this;
        }

        public Builder id(GdalGeometryFieldSupportedSRSListInnerOneOfIdDto id) {
            this.instance.id(id);
            return this;
        }

        /**
         * returns a built GdalGeometryFieldSupportedSRSListInnerOneOfDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalGeometryFieldSupportedSRSListInnerOneOfDto build() {
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
