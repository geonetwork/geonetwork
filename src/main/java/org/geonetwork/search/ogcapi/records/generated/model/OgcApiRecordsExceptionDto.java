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

/** OgcApiRecordsExceptionDto */
@JsonTypeName("exception")
@JacksonXmlRootElement(localName = "OgcApiRecordsExceptionDto")
@XmlRootElement(name = "OgcApiRecordsExceptionDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiRecordsExceptionDto {

  private String code;

  private String description;

  public OgcApiRecordsExceptionDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiRecordsExceptionDto(String code) {
    this.code = code;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiRecordsExceptionDto code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   *
   * @return code
   */
  @NotNull
  @Schema(name = "code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  @JacksonXmlProperty(localName = "code")
  @XmlElement(name = "code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public OgcApiRecordsExceptionDto description(String description) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiRecordsExceptionDto exception = (OgcApiRecordsExceptionDto) o;
    return Objects.equals(this.code, exception.code)
        && Objects.equals(this.description, exception.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, description);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiRecordsExceptionDto {\n"
            + "    code: "
            + toIndentedString(code)
            + "\n"
            + "    description: "
            + toIndentedString(description)
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

    private OgcApiRecordsExceptionDto instance;

    public Builder() {
      this(new OgcApiRecordsExceptionDto());
    }

    protected Builder(OgcApiRecordsExceptionDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiRecordsExceptionDto value) {
      this.instance.setCode(value.code);
      this.instance.setDescription(value.description);
      return this;
    }

    public Builder code(String code) {
      this.instance.code(code);
      return this;
    }

    public Builder description(String description) {
      this.instance.description(description);
      return this;
    }

    /**
     * returns a built OgcApiRecordsExceptionDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiRecordsExceptionDto build() {
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
