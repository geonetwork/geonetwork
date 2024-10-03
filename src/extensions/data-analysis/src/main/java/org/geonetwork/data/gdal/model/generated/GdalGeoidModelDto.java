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
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.util.Objects;

/** GdalGeoidModelDto */
@JsonTypeName("geoid_model")
@JacksonXmlRootElement(localName = "GdalGeoidModelDto")
@XmlRootElement(name = "GdalGeoidModelDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalGeoidModelDto {

  private String name;

  private Object interpolationCrs = null;

  private Object id = null;

  public GdalGeoidModelDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GdalGeoidModelDto(String name) {
    this.name = name;
  }

  public GdalGeoidModelDto name(String name) {
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

  public GdalGeoidModelDto interpolationCrs(Object interpolationCrs) {
    this.interpolationCrs = interpolationCrs;
    return this;
  }

  /**
   * Get interpolationCrs
   *
   * @return interpolationCrs
   */
  @Schema(name = "interpolation_crs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("interpolation_crs")
  @JacksonXmlProperty(localName = "interpolation_crs")
  @XmlElement(name = "interpolation_crs")
  public Object getInterpolationCrs() {
    return interpolationCrs;
  }

  public void setInterpolationCrs(Object interpolationCrs) {
    this.interpolationCrs = interpolationCrs;
  }

  public GdalGeoidModelDto id(Object id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   *
   * @return id
   */
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  @JacksonXmlProperty(localName = "id")
  @XmlElement(name = "id")
  public Object getId() {
    return id;
  }

  public void setId(Object id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalGeoidModelDto geoidModel = (GdalGeoidModelDto) o;
    return Objects.equals(this.name, geoidModel.name)
        && Objects.equals(this.interpolationCrs, geoidModel.interpolationCrs)
        && Objects.equals(this.id, geoidModel.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, interpolationCrs, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalGeoidModelDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    interpolationCrs: ").append(toIndentedString(interpolationCrs)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("}");
    return sb.toString();
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

  public static class Builder {

    private GdalGeoidModelDto instance;

    public Builder() {
      this(new GdalGeoidModelDto());
    }

    protected Builder(GdalGeoidModelDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalGeoidModelDto value) {
      this.instance.setName(value.name);
      this.instance.setInterpolationCrs(value.interpolationCrs);
      this.instance.setId(value.id);
      return this;
    }

    public Builder name(String name) {
      this.instance.name(name);
      return this;
    }

    public Builder interpolationCrs(Object interpolationCrs) {
      this.instance.interpolationCrs(interpolationCrs);
      return this;
    }

    public Builder id(Object id) {
      this.instance.id(id);
      return this;
    }

    /**
     * returns a built GdalGeoidModelDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalGeoidModelDto build() {
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
