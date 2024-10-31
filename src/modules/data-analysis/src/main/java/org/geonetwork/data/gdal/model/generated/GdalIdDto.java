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
import java.util.Objects;

/** GdalIdDto */
@JsonTypeName("id")
@JacksonXmlRootElement(localName = "GdalIdDto")
@XmlRootElement(name = "GdalIdDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalIdDto {

    private String authority;

    private GdalIdCodeDto code;

    private GdalIdVersionDto version;

    private String authorityCitation;

    private String uri;

    public GdalIdDto() {
        super();
    }

    /** Constructor with only required parameters */
    public GdalIdDto(String authority, GdalIdCodeDto code) {
        this.authority = authority;
        this.code = code;
    }

    public GdalIdDto authority(String authority) {
        this.authority = authority;
        return this;
    }

    /**
     * Get authority
     *
     * @return authority
     */
    @NotNull
    @Schema(name = "authority", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("authority")
    @JacksonXmlProperty(localName = "authority")
    @XmlElement(name = "authority")
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public GdalIdDto code(GdalIdCodeDto code) {
        this.code = code;
        return this;
    }

    /**
     * Get code
     *
     * @return code
     */
    @NotNull
    @Valid
    @Schema(name = "code", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("code")
    @JacksonXmlProperty(localName = "code")
    @XmlElement(name = "code")
    public GdalIdCodeDto getCode() {
        return code;
    }

    public void setCode(GdalIdCodeDto code) {
        this.code = code;
    }

    public GdalIdDto version(GdalIdVersionDto version) {
        this.version = version;
        return this;
    }

    /**
     * Get version
     *
     * @return version
     */
    @Valid
    @Schema(name = "version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("version")
    @JacksonXmlProperty(localName = "version")
    @XmlElement(name = "version")
    public GdalIdVersionDto getVersion() {
        return version;
    }

    public void setVersion(GdalIdVersionDto version) {
        this.version = version;
    }

    public GdalIdDto authorityCitation(String authorityCitation) {
        this.authorityCitation = authorityCitation;
        return this;
    }

    /**
     * Get authorityCitation
     *
     * @return authorityCitation
     */
    @Schema(name = "authority_citation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("authority_citation")
    @JacksonXmlProperty(localName = "authority_citation")
    @XmlElement(name = "authority_citation")
    public String getAuthorityCitation() {
        return authorityCitation;
    }

    public void setAuthorityCitation(String authorityCitation) {
        this.authorityCitation = authorityCitation;
    }

    public GdalIdDto uri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * Get uri
     *
     * @return uri
     */
    @Schema(name = "uri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("uri")
    @JacksonXmlProperty(localName = "uri")
    @XmlElement(name = "uri")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalIdDto id = (GdalIdDto) o;
        return Objects.equals(this.authority, id.authority)
                && Objects.equals(this.code, id.code)
                && Objects.equals(this.version, id.version)
                && Objects.equals(this.authorityCitation, id.authorityCitation)
                && Objects.equals(this.uri, id.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority, code, version, authorityCitation, uri);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalIdDto {\n");
        sb.append("    authority: ").append(toIndentedString(authority)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    authorityCitation: ")
                .append(toIndentedString(authorityCitation))
                .append("\n");
        sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
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

        private GdalIdDto instance;

        public Builder() {
            this(new GdalIdDto());
        }

        protected Builder(GdalIdDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalIdDto value) {
            this.instance.setAuthority(value.authority);
            this.instance.setCode(value.code);
            this.instance.setVersion(value.version);
            this.instance.setAuthorityCitation(value.authorityCitation);
            this.instance.setUri(value.uri);
            return this;
        }

        public Builder authority(String authority) {
            this.instance.authority(authority);
            return this;
        }

        public Builder code(GdalIdCodeDto code) {
            this.instance.code(code);
            return this;
        }

        public Builder version(GdalIdVersionDto version) {
            this.instance.version(version);
            return this;
        }

        public Builder authorityCitation(String authorityCitation) {
            this.instance.authorityCitation(authorityCitation);
            return this;
        }

        public Builder uri(String uri) {
            this.instance.uri(uri);
            return this;
        }

        /**
         * returns a built GdalIdDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalIdDto build() {
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
