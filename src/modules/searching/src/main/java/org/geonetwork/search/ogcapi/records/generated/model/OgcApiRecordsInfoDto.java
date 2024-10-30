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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/** OgcApiRecordsInfoDto */
@JsonTypeName("Info")
@JacksonXmlRootElement(localName = "OgcApiRecordsInfoDto")
@XmlRootElement(name = "OgcApiRecordsInfoDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsInfoDto {

    private String title;

    private String description;

    private String termsOfService;

    private OgcApiRecordsContactDto contact;

    private OgcApiRecordsLicenseDto license;

    private String version;

    public OgcApiRecordsInfoDto() {
        super();
    }

    /** Constructor with only required parameters */
    public OgcApiRecordsInfoDto(String title, String version) {
        this.title = title;
        this.version = version;
    }

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsInfoDto title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Get title
     *
     * @return title
     */
    @NotNull
    @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title")
    @XmlElement(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public OgcApiRecordsInfoDto description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     *
     * @return description
     */
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

    public OgcApiRecordsInfoDto termsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
        return this;
    }

    /**
     * Get termsOfService
     *
     * @return termsOfService
     */
    @Schema(name = "termsOfService", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("termsOfService")
    @JacksonXmlProperty(localName = "termsOfService")
    @XmlElement(name = "termsOfService")
    public String getTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    public OgcApiRecordsInfoDto contact(OgcApiRecordsContactDto contact) {
        this.contact = contact;
        return this;
    }

    /**
     * Get contact
     *
     * @return contact
     */
    @Valid
    @Schema(name = "contact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("contact")
    @JacksonXmlProperty(localName = "contact")
    @XmlElement(name = "contact")
    public OgcApiRecordsContactDto getContact() {
        return contact;
    }

    public void setContact(OgcApiRecordsContactDto contact) {
        this.contact = contact;
    }

    public OgcApiRecordsInfoDto license(OgcApiRecordsLicenseDto license) {
        this.license = license;
        return this;
    }

    /**
     * Get license
     *
     * @return license
     */
    @Valid
    @Schema(name = "license", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("license")
    @JacksonXmlProperty(localName = "license")
    @XmlElement(name = "license")
    public OgcApiRecordsLicenseDto getLicense() {
        return license;
    }

    public void setLicense(OgcApiRecordsLicenseDto license) {
        this.license = license;
    }

    public OgcApiRecordsInfoDto version(String version) {
        this.version = version;
        return this;
    }

    /**
     * Get version
     *
     * @return version
     */
    @NotNull
    @Schema(name = "version", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("version")
    @JacksonXmlProperty(localName = "version")
    @XmlElement(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OgcApiRecordsInfoDto info = (OgcApiRecordsInfoDto) o;
        return Objects.equals(this.title, info.title)
                && Objects.equals(this.description, info.description)
                && Objects.equals(this.termsOfService, info.termsOfService)
                && Objects.equals(this.contact, info.contact)
                && Objects.equals(this.license, info.license)
                && Objects.equals(this.version, info.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, termsOfService, contact, license, version);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsInfoDto {\n"
                + "    title: "
                + toIndentedString(title)
                + "\n"
                + "    description: "
                + toIndentedString(description)
                + "\n"
                + "    termsOfService: "
                + toIndentedString(termsOfService)
                + "\n"
                + "    contact: "
                + toIndentedString(contact)
                + "\n"
                + "    license: "
                + toIndentedString(license)
                + "\n"
                + "    version: "
                + toIndentedString(version)
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

    public static class Builder {

        private OgcApiRecordsInfoDto instance;

        public Builder() {
            this(new OgcApiRecordsInfoDto());
        }

        protected Builder(OgcApiRecordsInfoDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsInfoDto value) {
            this.instance.setTitle(value.title);
            this.instance.setDescription(value.description);
            this.instance.setTermsOfService(value.termsOfService);
            this.instance.setContact(value.contact);
            this.instance.setLicense(value.license);
            this.instance.setVersion(value.version);
            return this;
        }

        public Builder title(String title) {
            this.instance.title(title);
            return this;
        }

        public Builder description(String description) {
            this.instance.description(description);
            return this;
        }

        public Builder termsOfService(String termsOfService) {
            this.instance.termsOfService(termsOfService);
            return this;
        }

        public Builder contact(OgcApiRecordsContactDto contact) {
            this.instance.contact(contact);
            return this;
        }

        public Builder license(OgcApiRecordsLicenseDto license) {
            this.instance.license(license);
            return this;
        }

        public Builder version(String version) {
            this.instance.version(version);
            return this;
        }

        /**
         * returns a built OgcApiRecordsInfoDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsInfoDto build() {
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
