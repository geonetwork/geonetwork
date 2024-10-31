/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** OgcApiRecordsServerDto */
@JsonTypeName("Server")
@JacksonXmlRootElement(localName = "OgcApiRecordsServerDto")
@XmlRootElement(name = "OgcApiRecordsServerDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsServerDto {

    private String url;

    private String description;

    @Valid
    private Map<String, OgcApiRecordsServerVariableDto> variables = new HashMap<>();

    public OgcApiRecordsServerDto() {
        super();
    }

    /** Constructor with only required parameters */
    public OgcApiRecordsServerDto(String url) {
        this.url = url;
    }

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsServerDto url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Get url
     *
     * @return url
     */
    @NotNull
    @Schema(name = "url", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("url")
    @JacksonXmlProperty(localName = "url")
    @XmlElement(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public OgcApiRecordsServerDto description(String description) {
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

    public OgcApiRecordsServerDto variables(Map<String, OgcApiRecordsServerVariableDto> variables) {
        this.variables = variables;
        return this;
    }

    public OgcApiRecordsServerDto putVariablesItem(String key, OgcApiRecordsServerVariableDto variablesItem) {
        if (this.variables == null) {
            this.variables = new HashMap<>();
        }
        this.variables.put(key, variablesItem);
        return this;
    }

    /**
     * Get variables
     *
     * @return variables
     */
    @Valid
    @Schema(name = "variables", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("variables")
    @JacksonXmlProperty(localName = "variables")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "variables")
    public Map<String, OgcApiRecordsServerVariableDto> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, OgcApiRecordsServerVariableDto> variables) {
        this.variables = variables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OgcApiRecordsServerDto server = (OgcApiRecordsServerDto) o;
        return Objects.equals(this.url, server.url)
                && Objects.equals(this.description, server.description)
                && Objects.equals(this.variables, server.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, description, variables);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsServerDto {\n"
                + "    url: "
                + toIndentedString(url)
                + "\n"
                + "    description: "
                + toIndentedString(description)
                + "\n"
                + "    variables: "
                + toIndentedString(variables)
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

        private OgcApiRecordsServerDto instance;

        public Builder() {
            this(new OgcApiRecordsServerDto());
        }

        protected Builder(OgcApiRecordsServerDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsServerDto value) {
            this.instance.setUrl(value.url);
            this.instance.setDescription(value.description);
            this.instance.setVariables(value.variables);
            return this;
        }

        public Builder url(String url) {
            this.instance.url(url);
            return this;
        }

        public Builder description(String description) {
            this.instance.description(description);
            return this;
        }

        public Builder variables(Map<String, OgcApiRecordsServerVariableDto> variables) {
            this.instance.variables(variables);
            return this;
        }

        /**
         * returns a built OgcApiRecordsServerDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsServerDto build() {
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
