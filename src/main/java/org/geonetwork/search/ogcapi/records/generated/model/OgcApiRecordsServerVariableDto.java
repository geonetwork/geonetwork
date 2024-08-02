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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** OgcApiRecordsServerVariableDto */
@JsonTypeName("ServerVariable")
@JacksonXmlRootElement(localName = "OgcApiRecordsServerVariableDto")
@XmlRootElement(name = "OgcApiRecordsServerVariableDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiRecordsServerVariableDto {

  @Valid private List<String> _enum = new ArrayList<>();

  private String _default;

  private String description;

  public OgcApiRecordsServerVariableDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiRecordsServerVariableDto(String _default) {
    this._default = _default;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiRecordsServerVariableDto _enum(List<String> _enum) {
    this._enum = _enum;
    return this;
  }

  public OgcApiRecordsServerVariableDto addEnumItem(String _enumItem) {
    if (this._enum == null) {
      this._enum = new ArrayList<>();
    }
    this._enum.add(_enumItem);
    return this;
  }

  /**
   * Get _enum
   *
   * @return _enum
   */
  @Schema(name = "enum", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enum")
  @JacksonXmlProperty(localName = "enum")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "enum")
  public List<String> getEnum() {
    return _enum;
  }

  public void setEnum(List<String> _enum) {
    this._enum = _enum;
  }

  public OgcApiRecordsServerVariableDto _default(String _default) {
    this._default = _default;
    return this;
  }

  /**
   * Get _default
   *
   * @return _default
   */
  @NotNull
  @Schema(name = "default", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("default")
  @JacksonXmlProperty(localName = "default")
  @XmlElement(name = "default")
  public String getDefault() {
    return _default;
  }

  public void setDefault(String _default) {
    this._default = _default;
  }

  public OgcApiRecordsServerVariableDto description(String description) {
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
    OgcApiRecordsServerVariableDto serverVariable = (OgcApiRecordsServerVariableDto) o;
    return Objects.equals(this._enum, serverVariable._enum)
        && Objects.equals(this._default, serverVariable._default)
        && Objects.equals(this.description, serverVariable.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_enum, _default, description);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiRecordsServerVariableDto {\n"
            + "    _enum: "
            + toIndentedString(_enum)
            + "\n"
            + "    _default: "
            + toIndentedString(_default)
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

    private OgcApiRecordsServerVariableDto instance;

    public Builder() {
      this(new OgcApiRecordsServerVariableDto());
    }

    protected Builder(OgcApiRecordsServerVariableDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiRecordsServerVariableDto value) {
      this.instance.setEnum(value._enum);
      this.instance.setDefault(value._default);
      this.instance.setDescription(value.description);
      return this;
    }

    public Builder _enum(List<String> _enum) {
      this.instance._enum(_enum);
      return this;
    }

    public Builder _default(String _default) {
      this.instance._default(_default);
      return this;
    }

    public Builder description(String description) {
      this.instance.description(description);
      return this;
    }

    /**
     * returns a built OgcApiRecordsServerVariableDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiRecordsServerVariableDto build() {
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
