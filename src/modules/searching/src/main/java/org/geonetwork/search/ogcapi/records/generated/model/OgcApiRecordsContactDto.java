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
import jakarta.validation.constraints.Email;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/** OgcApiRecordsContactDto */
@JsonTypeName("Contact")
@JacksonXmlRootElement(localName = "OgcApiRecordsContactDto")
@XmlRootElement(name = "OgcApiRecordsContactDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsContactDto {

    private String name;

    private String url;

    private String email;

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsContactDto name(String name) {
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

    public OgcApiRecordsContactDto url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Get url
     *
     * @return url
     */
    @Schema(name = "url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("url")
    @JacksonXmlProperty(localName = "url")
    @XmlElement(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public OgcApiRecordsContactDto email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Get email
     *
     * @return email
     */
    @Email
    @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("email")
    @JacksonXmlProperty(localName = "email")
    @XmlElement(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OgcApiRecordsContactDto contact = (OgcApiRecordsContactDto) o;
        return Objects.equals(this.name, contact.name)
                && Objects.equals(this.url, contact.url)
                && Objects.equals(this.email, contact.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, email);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsContactDto {\n"
                + "    name: "
                + toIndentedString(name)
                + "\n"
                + "    url: "
                + toIndentedString(url)
                + "\n"
                + "    email: "
                + toIndentedString(email)
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

        private OgcApiRecordsContactDto instance;

        public Builder() {
            this(new OgcApiRecordsContactDto());
        }

        protected Builder(OgcApiRecordsContactDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsContactDto value) {
            this.instance.setName(value.name);
            this.instance.setUrl(value.url);
            this.instance.setEmail(value.email);
            return this;
        }

        public Builder name(String name) {
            this.instance.name(name);
            return this;
        }

        public Builder url(String url) {
            this.instance.url(url);
            return this;
        }

        public Builder email(String email) {
            this.instance.email(email);
            return this;
        }

        /**
         * returns a built OgcApiRecordsContactDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsContactDto build() {
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
