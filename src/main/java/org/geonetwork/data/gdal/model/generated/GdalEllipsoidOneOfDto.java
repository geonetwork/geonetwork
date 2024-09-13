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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.util.*;
import java.util.Objects;

/** GdalEllipsoidOneOfDto */
@JsonTypeName("ellipsoid_oneOf")
@JacksonXmlRootElement(localName = "GdalEllipsoidOneOfDto")
@XmlRootElement(name = "GdalEllipsoidOneOfDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalEllipsoidOneOfDto {

  private String $schema;

  /** Gets or Sets type */
  public enum TypeEnum {
    ELLIPSOID("Ellipsoid");

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

  private String name;

  private GdalValueAndUnitDto semiMajorAxis = null;

  private GdalValueAndUnitDto semiMinorAxis = null;

  private GdalIdDto id;

  private GdalIdsDto ids = new GdalIdsDto();

  public GdalEllipsoidOneOfDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GdalEllipsoidOneOfDto(
      String name, GdalValueAndUnitDto semiMajorAxis, GdalValueAndUnitDto semiMinorAxis) {
    this.name = name;
    this.semiMajorAxis = semiMajorAxis;
    this.semiMinorAxis = semiMinorAxis;
  }

  public GdalEllipsoidOneOfDto $schema(String $schema) {
    this.$schema = $schema;
    return this;
  }

  /**
   * Get $schema
   *
   * @return $schema
   */
  @Schema(name = "$schema", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("$schema")
  @JacksonXmlProperty(localName = "$schema")
  @XmlElement(name = "$schema")
  public String get$Schema() {
    return $schema;
  }

  public void set$Schema(String $schema) {
    this.$schema = $schema;
  }

  public GdalEllipsoidOneOfDto type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   *
   * @return type
   */
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  @JacksonXmlProperty(localName = "type")
  @XmlElement(name = "type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public GdalEllipsoidOneOfDto name(String name) {
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

  public GdalEllipsoidOneOfDto semiMajorAxis(GdalValueAndUnitDto semiMajorAxis) {
    this.semiMajorAxis = semiMajorAxis;
    return this;
  }

  /**
   * Get semiMajorAxis
   *
   * @return semiMajorAxis
   */
  @NotNull
  @Valid
  @Schema(name = "semi_major_axis", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("semi_major_axis")
  @JacksonXmlProperty(localName = "semi_major_axis")
  @XmlElement(name = "semi_major_axis")
  public GdalValueAndUnitDto getSemiMajorAxis() {
    return semiMajorAxis;
  }

  public void setSemiMajorAxis(GdalValueAndUnitDto semiMajorAxis) {
    this.semiMajorAxis = semiMajorAxis;
  }

  public GdalEllipsoidOneOfDto semiMinorAxis(GdalValueAndUnitDto semiMinorAxis) {
    this.semiMinorAxis = semiMinorAxis;
    return this;
  }

  /**
   * Get semiMinorAxis
   *
   * @return semiMinorAxis
   */
  @NotNull
  @Valid
  @Schema(name = "semi_minor_axis", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("semi_minor_axis")
  @JacksonXmlProperty(localName = "semi_minor_axis")
  @XmlElement(name = "semi_minor_axis")
  public GdalValueAndUnitDto getSemiMinorAxis() {
    return semiMinorAxis;
  }

  public void setSemiMinorAxis(GdalValueAndUnitDto semiMinorAxis) {
    this.semiMinorAxis = semiMinorAxis;
  }

  public GdalEllipsoidOneOfDto id(GdalIdDto id) {
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
  public GdalIdDto getId() {
    return id;
  }

  public void setId(GdalIdDto id) {
    this.id = id;
  }

  public GdalEllipsoidOneOfDto ids(GdalIdsDto ids) {
    this.ids = ids;
    return this;
  }

  /**
   * Get ids
   *
   * @return ids
   */
  @Valid
  @Schema(name = "ids", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ids")
  @JacksonXmlProperty(localName = "ids")
  @XmlElement(name = "ids")
  public GdalIdsDto getIds() {
    return ids;
  }

  public void setIds(GdalIdsDto ids) {
    this.ids = ids;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalEllipsoidOneOfDto ellipsoidOneOf = (GdalEllipsoidOneOfDto) o;
    return Objects.equals(this.$schema, ellipsoidOneOf.$schema)
        && Objects.equals(this.type, ellipsoidOneOf.type)
        && Objects.equals(this.name, ellipsoidOneOf.name)
        && Objects.equals(this.semiMajorAxis, ellipsoidOneOf.semiMajorAxis)
        && Objects.equals(this.semiMinorAxis, ellipsoidOneOf.semiMinorAxis)
        && Objects.equals(this.id, ellipsoidOneOf.id)
        && Objects.equals(this.ids, ellipsoidOneOf.ids);
  }

  @Override
  public int hashCode() {
    return Objects.hash($schema, type, name, semiMajorAxis, semiMinorAxis, id, ids);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalEllipsoidOneOfDto {\n");
    sb.append("    $schema: ").append(toIndentedString($schema)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    semiMajorAxis: ").append(toIndentedString(semiMajorAxis)).append("\n");
    sb.append("    semiMinorAxis: ").append(toIndentedString(semiMinorAxis)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    ids: ").append(toIndentedString(ids)).append("\n");
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

    private GdalEllipsoidOneOfDto instance;

    public Builder() {
      this(new GdalEllipsoidOneOfDto());
    }

    protected Builder(GdalEllipsoidOneOfDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalEllipsoidOneOfDto value) {
      this.instance.set$Schema(value.$schema);
      this.instance.setType(value.type);
      this.instance.setName(value.name);
      this.instance.setSemiMajorAxis(value.semiMajorAxis);
      this.instance.setSemiMinorAxis(value.semiMinorAxis);
      this.instance.setId(value.id);
      this.instance.setIds(value.ids);
      return this;
    }

    public Builder $schema(String $schema) {
      this.instance.$schema($schema);
      return this;
    }

    public Builder type(TypeEnum type) {
      this.instance.type(type);
      return this;
    }

    public Builder name(String name) {
      this.instance.name(name);
      return this;
    }

    public Builder semiMajorAxis(GdalValueAndUnitDto semiMajorAxis) {
      this.instance.semiMajorAxis(semiMajorAxis);
      return this;
    }

    public Builder semiMinorAxis(GdalValueAndUnitDto semiMinorAxis) {
      this.instance.semiMinorAxis(semiMinorAxis);
      return this;
    }

    public Builder id(GdalIdDto id) {
      this.instance.id(id);
      return this;
    }

    public Builder ids(GdalIdsDto ids) {
      this.instance.ids(ids);
      return this;
    }

    /**
     * returns a built GdalEllipsoidOneOfDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalEllipsoidOneOfDto build() {
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
