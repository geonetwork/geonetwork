/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/** OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto */
@JsonTypeName("recordCommonProperties_externalIds_inner")
@JacksonXmlRootElement(localName = "OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto")
@XmlRootElement(name = "OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto {

    private String scheme;

    private String value;

    public OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto() {
        super();
    }

    /** Constructor with only required parameters */
    public OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto(String value) {
        this.value = value;
    }

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * A reference to an authority or identifier for a knowledge organization system from which the external identifier
     * was obtained. It is recommended that the identifier be a resolvable URI.
     *
     * @return scheme
     */
    @Schema(
            name = "scheme",
            description = "A reference to an authority or identifier for a knowledge organization system from which"
                    + " the external identifier was obtained. It is recommended that the identifier be a"
                    + " resolvable URI.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("scheme")
    @JacksonXmlProperty(localName = "scheme")
    @XmlElement(name = "scheme")
    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto value(String value) {
        this.value = value;
        return this;
    }

    /**
     * The value of the identifier.
     *
     * @return value
     */
    @NotNull
    @Schema(name = "value", description = "The value of the identifier.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("value")
    @JacksonXmlProperty(localName = "value")
    @XmlElement(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto recordCommonPropertiesExternalIdsInner =
                (OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto) o;
        return Objects.equals(this.scheme, recordCommonPropertiesExternalIdsInner.scheme)
                && Objects.equals(this.value, recordCommonPropertiesExternalIdsInner.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheme, value);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto {\n"
                + "    scheme: "
                + toIndentedString(scheme)
                + "\n"
                + "    value: "
                + toIndentedString(value)
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

    public static class Builder {

        private OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto instance;

        public Builder() {
            this(new OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto());
        }

        protected Builder(OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto value) {
            this.instance.setScheme(value.scheme);
            this.instance.setValue(value.value);
            return this;
        }

        public Builder scheme(String scheme) {
            this.instance.scheme(scheme);
            return this;
        }

        public Builder value(String value) {
            this.instance.value(value);
            return this;
        }

        /**
         * returns a built OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto build() {
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
