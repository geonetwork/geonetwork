/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.tasks.ogcapi.processes.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** JSON schema for exceptions based on RFC 7807 */
@Schema(name = "exception", description = "JSON schema for exceptions based on RFC 7807")
@JsonTypeName("exception")
@JacksonXmlRootElement(localName = "OgcApiProcessesExceptionDto")
@XmlRootElement(name = "OgcApiProcessesExceptionDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesExceptionDto {

  private String type;

  private String title;

  private Integer status;

  private String detail;

  private String instance;

  /**
   * A container for additional, undeclared properties. This is a holder for any undeclared
   * properties as specified with the 'additionalProperties' keyword in the OAS document.
   */
  private Map<String, Object> additionalProperties;

  public OgcApiProcessesExceptionDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiProcessesExceptionDto(String type) {
    this.type = type;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesExceptionDto type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   *
   * @return type
   */
  @NotNull
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  @JacksonXmlProperty(localName = "type")
  @XmlElement(name = "type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public OgcApiProcessesExceptionDto title(String title) {
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

  public OgcApiProcessesExceptionDto status(Integer status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   *
   * @return status
   */
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  @JacksonXmlProperty(localName = "status")
  @XmlElement(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public OgcApiProcessesExceptionDto detail(String detail) {
    this.detail = detail;
    return this;
  }

  /**
   * Get detail
   *
   * @return detail
   */
  @Schema(name = "detail", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("detail")
  @JacksonXmlProperty(localName = "detail")
  @XmlElement(name = "detail")
  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public OgcApiProcessesExceptionDto instance(String instance) {
    this.instance = instance;
    return this;
  }

  /**
   * Get instance
   *
   * @return instance
   */
  @Schema(name = "instance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instance")
  @JacksonXmlProperty(localName = "instance")
  @XmlElement(name = "instance")
  public String getInstance() {
    return instance;
  }

  public void setInstance(String instance) {
    this.instance = instance;
  }

  /**
   * Set the additional (undeclared) property with the specified name and value. If the property
   * does not already exist, create it otherwise replace it.
   */
  @JsonAnySetter
  public OgcApiProcessesExceptionDto putAdditionalProperty(String key, Object value) {
    if (this.additionalProperties == null) {
      this.additionalProperties = new HashMap<String, Object>();
    }
    this.additionalProperties.put(key, value);
    return this;
  }

  /** Return the additional (undeclared) property. */
  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  /** Return the additional (undeclared) property with the specified name. */
  public Object getAdditionalProperty(String key) {
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
    OgcApiProcessesExceptionDto exception = (OgcApiProcessesExceptionDto) o;
    return Objects.equals(this.type, exception.type)
        && Objects.equals(this.title, exception.title)
        && Objects.equals(this.status, exception.status)
        && Objects.equals(this.detail, exception.detail)
        && Objects.equals(this.instance, exception.instance)
        && Objects.equals(this.additionalProperties, exception.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, title, status, detail, instance, additionalProperties);
  }

  @Override
  public String toString() {

    String sb =
        "class OgcApiProcessesExceptionDto {\n"
            + "    type: "
            + toIndentedString(type)
            + "\n"
            + "    title: "
            + toIndentedString(title)
            + "\n"
            + "    status: "
            + toIndentedString(status)
            + "\n"
            + "    detail: "
            + toIndentedString(detail)
            + "\n"
            + "    instance: "
            + toIndentedString(instance)
            + "\n"
            + "    additionalProperties: "
            + toIndentedString(additionalProperties)
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

    private OgcApiProcessesExceptionDto instance;

    public Builder() {
      this(new OgcApiProcessesExceptionDto());
    }

    protected Builder(OgcApiProcessesExceptionDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesExceptionDto value) {
      this.instance.setType(value.type);
      this.instance.setTitle(value.title);
      this.instance.setStatus(value.status);
      this.instance.setDetail(value.detail);
      this.instance.setInstance(value.instance);
      return this;
    }

    public Builder type(String type) {
      this.instance.type(type);
      return this;
    }

    public Builder title(String title) {
      this.instance.title(title);
      return this;
    }

    public Builder status(Integer status) {
      this.instance.status(status);
      return this;
    }

    public Builder detail(String detail) {
      this.instance.detail(detail);
      return this;
    }

    public Builder instance(String instance) {
      this.instance.instance(instance);
      return this;
    }

    public Builder additionalProperties(Map<String, Object> additionalProperties) {
      this.instance.additionalProperties = additionalProperties;
      return this;
    }

    /**
     * returns a built OgcApiProcessesExceptionDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesExceptionDto build() {
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
