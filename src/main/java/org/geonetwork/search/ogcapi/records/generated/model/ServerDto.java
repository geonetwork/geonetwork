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
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** ServerDto */
@JsonTypeName("Server")
@JacksonXmlRootElement(localName = "ServerDto")
@XmlRootElement(name = "ServerDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]",
    comments = "Generator version: 7.6.0")
public class ServerDto {

  private String url;

  private String description;

  @Valid private Map<String, ServerVariableDto> variables = new HashMap<>();

  public ServerDto() {
    super();
  }

  /** Constructor with only required parameters */
  public ServerDto(String url) {
    this.url = url;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public ServerDto url(String url) {
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
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public ServerDto description(String description) {
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
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ServerDto variables(Map<String, ServerVariableDto> variables) {
    this.variables = variables;
    return this;
  }

  public ServerDto putVariablesItem(String key, ServerVariableDto variablesItem) {
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
  public Map<String, ServerVariableDto> getVariables() {
    return variables;
  }

  public void setVariables(Map<String, ServerVariableDto> variables) {
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
    ServerDto server = (ServerDto) o;
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
    String sb =
        "class ServerDto {\n"
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

    private ServerDto instance;

    public Builder() {
      this(new ServerDto());
    }

    protected Builder(ServerDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(ServerDto value) {
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

    public Builder variables(Map<String, ServerVariableDto> variables) {
      this.instance.variables(variables);
      return this;
    }

    /**
     * returns a built ServerDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public ServerDto build() {
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
