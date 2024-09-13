/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.openapitools.jackson.nullable.JsonNullable;

/** GdalFeatureDto */
@JsonTypeName("Feature")
@JacksonXmlRootElement(localName = "GdalFeatureDto")
@XmlRootElement(name = "GdalFeatureDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalFeatureDto {

  /** Gets or Sets type */
  public enum TypeEnum {
    FEATURE("Feature");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private GdalFeatureIdDto id;

  private JsonNullable<Object> properties = JsonNullable.<Object>undefined();

  private JsonNullable<GdalFeatureGeometryDto> geometry =
      JsonNullable.<GdalFeatureGeometryDto>undefined();

  @Valid private List<BigDecimal> bbox = new ArrayList<>();

  public GdalFeatureDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GdalFeatureDto(TypeEnum type, Object properties, GdalFeatureGeometryDto geometry) {
    this.type = type;
    this.properties = JsonNullable.of(properties);
    this.geometry = JsonNullable.of(geometry);
  }

  public GdalFeatureDto type(TypeEnum type) {
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
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public GdalFeatureDto id(GdalFeatureIdDto id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   *
   * @return id
   */
  @Valid
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  @JacksonXmlProperty(localName = "id")
  @XmlElement(name = "id")
  public GdalFeatureIdDto getId() {
    return id;
  }

  public void setId(GdalFeatureIdDto id) {
    this.id = id;
  }

  public GdalFeatureDto properties(Object properties) {
    this.properties = JsonNullable.of(properties);
    return this;
  }

  /**
   * Get properties
   *
   * @return properties
   */
  @NotNull
  @Schema(name = "properties", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("properties")
  @JacksonXmlProperty(localName = "properties")
  @XmlElement(name = "properties")
  public JsonNullable<Object> getProperties() {
    return properties;
  }

  public void setProperties(JsonNullable<Object> properties) {
    this.properties = properties;
  }

  public GdalFeatureDto geometry(GdalFeatureGeometryDto geometry) {
    this.geometry = JsonNullable.of(geometry);
    return this;
  }

  /**
   * Get geometry
   *
   * @return geometry
   */
  @NotNull
  @Valid
  @Schema(name = "geometry", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("geometry")
  @JacksonXmlProperty(localName = "geometry")
  @XmlElement(name = "geometry")
  public JsonNullable<GdalFeatureGeometryDto> getGeometry() {
    return geometry;
  }

  public void setGeometry(JsonNullable<GdalFeatureGeometryDto> geometry) {
    this.geometry = geometry;
  }

  public GdalFeatureDto bbox(List<BigDecimal> bbox) {
    this.bbox = bbox;
    return this;
  }

  public GdalFeatureDto addBboxItem(BigDecimal bboxItem) {
    if (this.bbox == null) {
      this.bbox = new ArrayList<>();
    }
    this.bbox.add(bboxItem);
    return this;
  }

  /**
   * Get bbox
   *
   * @return bbox
   */
  @Valid
  @Size(min = 4)
  @Schema(name = "bbox", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bbox")
  @JacksonXmlProperty(localName = "bbox")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "bbox")
  public List<BigDecimal> getBbox() {
    return bbox;
  }

  public void setBbox(List<BigDecimal> bbox) {
    this.bbox = bbox;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalFeatureDto feature = (GdalFeatureDto) o;
    return Objects.equals(this.type, feature.type)
        && Objects.equals(this.id, feature.id)
        && Objects.equals(this.properties, feature.properties)
        && Objects.equals(this.geometry, feature.geometry)
        && Objects.equals(this.bbox, feature.bbox);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, id, properties, geometry, bbox);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalFeatureDto {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    geometry: ").append(toIndentedString(geometry)).append("\n");
    sb.append("    bbox: ").append(toIndentedString(bbox)).append("\n");
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

    private GdalFeatureDto instance;

    public Builder() {
      this(new GdalFeatureDto());
    }

    protected Builder(GdalFeatureDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalFeatureDto value) {
      this.instance.setType(value.type);
      this.instance.setId(value.id);
      this.instance.setProperties(value.properties);
      this.instance.setGeometry(value.geometry);
      this.instance.setBbox(value.bbox);
      return this;
    }

    public Builder type(TypeEnum type) {
      this.instance.type(type);
      return this;
    }

    public Builder id(GdalFeatureIdDto id) {
      this.instance.id(id);
      return this;
    }

    public Builder properties(Object properties) {
      this.instance.properties(properties);
      return this;
    }

    public Builder properties(JsonNullable<Object> properties) {
      this.instance.properties = properties;
      return this;
    }

    public Builder geometry(GdalFeatureGeometryDto geometry) {
      this.instance.geometry(geometry);
      return this;
    }

    public Builder geometry(JsonNullable<GdalFeatureGeometryDto> geometry) {
      this.instance.geometry = geometry;
      return this;
    }

    public Builder bbox(List<BigDecimal> bbox) {
      this.instance.bbox(bbox);
      return this;
    }

    /**
     * returns a built GdalFeatureDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalFeatureDto build() {
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
