/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.tasks.ogcapi.processes.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** OgcApiProcessesDescriptionTypeAdditionalParametersDto */
@JsonTypeName("descriptionType_additionalParameters")
@JacksonXmlRootElement(localName = "OgcApiProcessesDescriptionTypeAdditionalParametersDto")
@XmlRootElement(name = "OgcApiProcessesDescriptionTypeAdditionalParametersDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesDescriptionTypeAdditionalParametersDto {

  private String title;

  private String role;

  private String href;

  @Valid private List<@Valid OgcApiProcessesAdditionalParameterDto> parameters = new ArrayList<>();

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesDescriptionTypeAdditionalParametersDto title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   *
   * @return title
   */
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  @JacksonXmlProperty(localName = "title")
  @XmlElement(name = "title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public OgcApiProcessesDescriptionTypeAdditionalParametersDto role(String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   *
   * @return role
   */
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  @JacksonXmlProperty(localName = "role")
  @XmlElement(name = "role")
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public OgcApiProcessesDescriptionTypeAdditionalParametersDto href(String href) {
    this.href = href;
    return this;
  }

  /**
   * Get href
   *
   * @return href
   */
  @Schema(name = "href", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("href")
  @JacksonXmlProperty(localName = "href")
  @XmlElement(name = "href")
  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public OgcApiProcessesDescriptionTypeAdditionalParametersDto parameters(
      List<@Valid OgcApiProcessesAdditionalParameterDto> parameters) {
    this.parameters = parameters;
    return this;
  }

  public OgcApiProcessesDescriptionTypeAdditionalParametersDto addParametersItem(
      OgcApiProcessesAdditionalParameterDto parametersItem) {
    if (this.parameters == null) {
      this.parameters = new ArrayList<>();
    }
    this.parameters.add(parametersItem);
    return this;
  }

  /**
   * Get parameters
   *
   * @return parameters
   */
  @Valid
  @Schema(name = "parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parameters")
  @JacksonXmlProperty(localName = "parameters")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "parameters")
  public List<@Valid OgcApiProcessesAdditionalParameterDto> getParameters() {
    return parameters;
  }

  public void setParameters(List<@Valid OgcApiProcessesAdditionalParameterDto> parameters) {
    this.parameters = parameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesDescriptionTypeAdditionalParametersDto descriptionTypeAdditionalParameters =
        (OgcApiProcessesDescriptionTypeAdditionalParametersDto) o;
    return Objects.equals(this.title, descriptionTypeAdditionalParameters.title)
        && Objects.equals(this.role, descriptionTypeAdditionalParameters.role)
        && Objects.equals(this.href, descriptionTypeAdditionalParameters.href)
        && Objects.equals(this.parameters, descriptionTypeAdditionalParameters.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, role, href, parameters);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesDescriptionTypeAdditionalParametersDto {\n"
            + "    title: "
            + toIndentedString(title)
            + "\n"
            + "    role: "
            + toIndentedString(role)
            + "\n"
            + "    href: "
            + toIndentedString(href)
            + "\n"
            + "    parameters: "
            + toIndentedString(parameters)
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

    private OgcApiProcessesDescriptionTypeAdditionalParametersDto instance;

    public Builder() {
      this(new OgcApiProcessesDescriptionTypeAdditionalParametersDto());
    }

    protected Builder(OgcApiProcessesDescriptionTypeAdditionalParametersDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesDescriptionTypeAdditionalParametersDto value) {
      this.instance.setTitle(value.title);
      this.instance.setRole(value.role);
      this.instance.setHref(value.href);
      this.instance.setParameters(value.parameters);
      return this;
    }

    public Builder title(String title) {
      this.instance.title(title);
      return this;
    }

    public Builder role(String role) {
      this.instance.role(role);
      return this;
    }

    public Builder href(String href) {
      this.instance.href(href);
      return this;
    }

    public Builder parameters(List<@Valid OgcApiProcessesAdditionalParameterDto> parameters) {
      this.instance.parameters(parameters);
      return this;
    }

    /**
     * returns a built OgcApiProcessesDescriptionTypeAdditionalParametersDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesDescriptionTypeAdditionalParametersDto build() {
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
