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
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** OgcApiProcessesAdditionalParameterDto */
@JsonTypeName("additionalParameter")
@JacksonXmlRootElement(localName = "OgcApiProcessesAdditionalParameterDto")
@XmlRootElement(name = "OgcApiProcessesAdditionalParameterDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesAdditionalParameterDto {

  private String name;

  @Valid private List<OgcApiProcessesAdditionalParameterValueInnerDto> value = new ArrayList<>();

  public OgcApiProcessesAdditionalParameterDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiProcessesAdditionalParameterDto(
      String name, List<OgcApiProcessesAdditionalParameterValueInnerDto> value) {
    this.name = name;
    this.value = value;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesAdditionalParameterDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   *
   * @return name
   */
  @NotNull
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  @JacksonXmlProperty(localName = "name")
  @XmlElement(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OgcApiProcessesAdditionalParameterDto value(
      List<OgcApiProcessesAdditionalParameterValueInnerDto> value) {
    this.value = value;
    return this;
  }

  public OgcApiProcessesAdditionalParameterDto addValueItem(
      OgcApiProcessesAdditionalParameterValueInnerDto valueItem) {
    if (this.value == null) {
      this.value = new ArrayList<>();
    }
    this.value.add(valueItem);
    return this;
  }

  /**
   * Get value
   *
   * @return value
   */
  @NotNull
  @Valid
  @Schema(name = "value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value")
  @JacksonXmlProperty(localName = "value")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "value")
  public List<OgcApiProcessesAdditionalParameterValueInnerDto> getValue() {
    return value;
  }

  public void setValue(List<OgcApiProcessesAdditionalParameterValueInnerDto> value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesAdditionalParameterDto additionalParameter =
        (OgcApiProcessesAdditionalParameterDto) o;
    return Objects.equals(this.name, additionalParameter.name)
        && Objects.equals(this.value, additionalParameter.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesAdditionalParameterDto {\n"
            + "    name: "
            + toIndentedString(name)
            + "\n"
            + "    value: "
            + toIndentedString(value)
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

    private OgcApiProcessesAdditionalParameterDto instance;

    public Builder() {
      this(new OgcApiProcessesAdditionalParameterDto());
    }

    protected Builder(OgcApiProcessesAdditionalParameterDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesAdditionalParameterDto value) {
      this.instance.setName(value.name);
      this.instance.setValue(value.value);
      return this;
    }

    public Builder name(String name) {
      this.instance.name(name);
      return this;
    }

    public Builder value(List<OgcApiProcessesAdditionalParameterValueInnerDto> value) {
      this.instance.value(value);
      return this;
    }

    /**
     * returns a built OgcApiProcessesAdditionalParameterDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesAdditionalParameterDto build() {
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
