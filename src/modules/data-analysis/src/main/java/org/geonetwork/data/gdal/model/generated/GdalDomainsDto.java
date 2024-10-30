/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.annotation.Generated;
import jakarta.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** GdalDomainsDto */
@JsonTypeName("domains")
@JacksonXmlRootElement(localName = "GdalDomainsDto")
@XmlRootElement(name = "GdalDomainsDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalDomainsDto {
    /**
     * A container for additional, undeclared properties. This is a holder for any undeclared
     * properties as specified with the 'additionalProperties' keyword in the OAS document.
     */
    private Map<String, GdalDomainDto> additionalProperties;

    /**
     * Set the additional (undeclared) property with the specified name and value. If the property
     * does not already exist, create it otherwise replace it.
     */
    @JsonAnySetter
    public GdalDomainsDto putAdditionalProperty(String key, GdalDomainDto value) {
        if (this.additionalProperties == null) {
            this.additionalProperties = new HashMap<String, GdalDomainDto>();
        }
        this.additionalProperties.put(key, value);
        return this;
    }

    /** Return the additional (undeclared) property. */
    @JsonAnyGetter
    public Map<String, GdalDomainDto> getAdditionalProperties() {
        return additionalProperties;
    }

    /** Return the additional (undeclared) property with the specified name. */
    public GdalDomainDto getAdditionalProperty(String key) {
        if (this.additionalProperties == null) {
            return null;
        }
        return this.additionalProperties.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(additionalProperties);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalDomainsDto {\n");

        sb.append("    additionalProperties: ")
                .append(toIndentedString(additionalProperties))
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

        private GdalDomainsDto instance;

        public Builder() {
            this(new GdalDomainsDto());
        }

        protected Builder(GdalDomainsDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalDomainsDto value) {
            return this;
        }

        public Builder additionalProperties(Map<String, GdalDomainDto> additionalProperties) {
            this.instance.additionalProperties = additionalProperties;
            return this;
        }

        /**
         * returns a built GdalDomainsDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalDomainsDto build() {
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
