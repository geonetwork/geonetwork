package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

/**
 * ComponentsDto
 */

@JsonTypeName("Components")
@JacksonXmlRootElement(localName = "ComponentsDto")
@XmlRootElement(name = "ComponentsDto")
@XmlAccessorType(XmlAccessType.FIELD)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]", comments = "Generator version: 7.6.0")
public class ComponentsDto {

    private Object schemas;

    private Object responses;

    private Object parameters;

    private Object examples;

    private Object requestBodies;

    private Object headers;

    private Object securitySchemes;

    private Object links;

    private Object callbacks;

    /**
     * Create a builder with no initialized field (except for the default values).
     */
    public static Builder builder() {
        return new Builder();
    }

    public ComponentsDto schemas(Object schemas) {
        this.schemas = schemas;
        return this;
    }

    /**
     * Get schemas
     *
     * @return schemas
     */

    @Schema(name = "schemas", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("schemas")
    @JacksonXmlProperty(localName = "schemas")
    public Object getSchemas() {
        return schemas;
    }

    public void setSchemas(Object schemas) {
        this.schemas = schemas;
    }

    public ComponentsDto responses(Object responses) {
        this.responses = responses;
        return this;
    }

    /**
     * Get responses
     *
     * @return responses
     */

    @Schema(name = "responses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("responses")
    @JacksonXmlProperty(localName = "responses")
    public Object getResponses() {
        return responses;
    }

    public void setResponses(Object responses) {
        this.responses = responses;
    }

    public ComponentsDto parameters(Object parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * Get parameters
     *
     * @return parameters
     */

    @Schema(name = "parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("parameters")
    @JacksonXmlProperty(localName = "parameters")
    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

    public ComponentsDto examples(Object examples) {
        this.examples = examples;
        return this;
    }

    /**
     * Get examples
     *
     * @return examples
     */

    @Schema(name = "examples", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("examples")
    @JacksonXmlProperty(localName = "examples")
    public Object getExamples() {
        return examples;
    }

    public void setExamples(Object examples) {
        this.examples = examples;
    }

    public ComponentsDto requestBodies(Object requestBodies) {
        this.requestBodies = requestBodies;
        return this;
    }

    /**
     * Get requestBodies
     *
     * @return requestBodies
     */

    @Schema(name = "requestBodies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("requestBodies")
    @JacksonXmlProperty(localName = "requestBodies")
    public Object getRequestBodies() {
        return requestBodies;
    }

    public void setRequestBodies(Object requestBodies) {
        this.requestBodies = requestBodies;
    }

    public ComponentsDto headers(Object headers) {
        this.headers = headers;
        return this;
    }

    /**
     * Get headers
     *
     * @return headers
     */

    @Schema(name = "headers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("headers")
    @JacksonXmlProperty(localName = "headers")
    public Object getHeaders() {
        return headers;
    }

    public void setHeaders(Object headers) {
        this.headers = headers;
    }

    public ComponentsDto securitySchemes(Object securitySchemes) {
        this.securitySchemes = securitySchemes;
        return this;
    }

    /**
     * Get securitySchemes
     *
     * @return securitySchemes
     */

    @Schema(name = "securitySchemes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("securitySchemes")
    @JacksonXmlProperty(localName = "securitySchemes")
    public Object getSecuritySchemes() {
        return securitySchemes;
    }

    public void setSecuritySchemes(Object securitySchemes) {
        this.securitySchemes = securitySchemes;
    }

    public ComponentsDto links(Object links) {
        this.links = links;
        return this;
    }

    /**
     * Get links
     *
     * @return links
     */

    @Schema(name = "links", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("links")
    @JacksonXmlProperty(localName = "links")
    public Object getLinks() {
        return links;
    }

    public void setLinks(Object links) {
        this.links = links;
    }

    public ComponentsDto callbacks(Object callbacks) {
        this.callbacks = callbacks;
        return this;
    }

    /**
     * Get callbacks
     *
     * @return callbacks
     */

    @Schema(name = "callbacks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("callbacks")
    @JacksonXmlProperty(localName = "callbacks")
    public Object getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(Object callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComponentsDto components = (ComponentsDto) o;
        return Objects.equals(this.schemas, components.schemas) &&
            Objects.equals(this.responses, components.responses) &&
            Objects.equals(this.parameters, components.parameters) &&
            Objects.equals(this.examples, components.examples) &&
            Objects.equals(this.requestBodies, components.requestBodies) &&
            Objects.equals(this.headers, components.headers) &&
            Objects.equals(this.securitySchemes, components.securitySchemes) &&
            Objects.equals(this.links, components.links) &&
            Objects.equals(this.callbacks, components.callbacks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemas, responses, parameters, examples, requestBodies, headers, securitySchemes, links, callbacks);
    }

    @Override
    public String toString() {
        String sb = "class ComponentsDto {\n" +
            "    schemas: " + toIndentedString(schemas) + "\n" +
            "    responses: " + toIndentedString(responses) + "\n" +
            "    parameters: " + toIndentedString(parameters) + "\n" +
            "    examples: " + toIndentedString(examples) + "\n" +
            "    requestBodies: " + toIndentedString(requestBodies) + "\n" +
            "    headers: " + toIndentedString(headers) + "\n" +
            "    securitySchemes: " + toIndentedString(securitySchemes) + "\n" +
            "    links: " + toIndentedString(links) + "\n" +
            "    callbacks: " + toIndentedString(callbacks) + "\n" +
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

        private ComponentsDto instance;

        public Builder() {
            this(new ComponentsDto());
        }

        protected Builder(ComponentsDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(ComponentsDto value) {
            this.instance.setSchemas(value.schemas);
            this.instance.setResponses(value.responses);
            this.instance.setParameters(value.parameters);
            this.instance.setExamples(value.examples);
            this.instance.setRequestBodies(value.requestBodies);
            this.instance.setHeaders(value.headers);
            this.instance.setSecuritySchemes(value.securitySchemes);
            this.instance.setLinks(value.links);
            this.instance.setCallbacks(value.callbacks);
            return this;
        }

        public Builder schemas(Object schemas) {
            this.instance.schemas(schemas);
            return this;
        }

        public Builder responses(Object responses) {
            this.instance.responses(responses);
            return this;
        }

        public Builder parameters(Object parameters) {
            this.instance.parameters(parameters);
            return this;
        }

        public Builder examples(Object examples) {
            this.instance.examples(examples);
            return this;
        }

        public Builder requestBodies(Object requestBodies) {
            this.instance.requestBodies(requestBodies);
            return this;
        }

        public Builder headers(Object headers) {
            this.instance.headers(headers);
            return this;
        }

        public Builder securitySchemes(Object securitySchemes) {
            this.instance.securitySchemes(securitySchemes);
            return this;
        }

        public Builder links(Object links) {
            this.instance.links(links);
            return this;
        }

        public Builder callbacks(Object callbacks) {
            this.instance.callbacks(callbacks);
            return this;
        }

        /**
         * returns a built ComponentsDto instance.
         * <p>
         * The builder is not reusable (NullPointerException)
         */
        public ComponentsDto build() {
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

