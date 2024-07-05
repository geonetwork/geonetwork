/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** PointGeoJSONDto */
@JsonTypeName("pointGeoJSON")
@JacksonXmlRootElement(localName = "PointGeoJSONDto")
@XmlRootElement(name = "PointGeoJSONDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]",
    comments = "Generator version: 7.6.0")
public class PointGeoJSONDto implements GeometryGeoJSONDto {

  private TypeEnum type;
  @Valid private List<BigDecimal> coordinates = new ArrayList<>();

  public PointGeoJSONDto() {
    super();
  }

  /** Constructor with only required parameters */
  public PointGeoJSONDto(TypeEnum type, List<BigDecimal> coordinates) {
    this.type = type;
    this.coordinates = coordinates;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public PointGeoJSONDto type(TypeEnum type) {
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
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public PointGeoJSONDto coordinates(List<BigDecimal> coordinates) {
    this.coordinates = coordinates;
    return this;
  }

  public PointGeoJSONDto addCoordinatesItem(BigDecimal coordinatesItem) {
    if (this.coordinates == null) {
      this.coordinates = new ArrayList<>();
    }
    this.coordinates.add(coordinatesItem);
    return this;
  }

  /**
   * Get coordinates
   *
   * @return coordinates
   */
  @NotNull
  @Valid
  @Size(min = 2)
  @Schema(name = "coordinates", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("coordinates")
  @JacksonXmlProperty(localName = "coordinates")
  public List<BigDecimal> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<BigDecimal> coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PointGeoJSONDto pointGeoJSON = (PointGeoJSONDto) o;
    return Objects.equals(this.type, pointGeoJSON.type)
        && Objects.equals(this.coordinates, pointGeoJSON.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, coordinates);
  }

  @Override
  public String toString() {
    String sb =
        "class PointGeoJSONDto {\n"
            + "    type: "
            + toIndentedString(type)
            + "\n"
            + "    coordinates: "
            + toIndentedString(coordinates)
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

  /** Gets or Sets type */
  public enum TypeEnum {
    POINT("Point");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
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

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class Builder {

    private PointGeoJSONDto instance;

    public Builder() {
      this(new PointGeoJSONDto());
    }

    protected Builder(PointGeoJSONDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(PointGeoJSONDto value) {
      this.instance.setType(value.type);
      this.instance.setCoordinates(value.coordinates);
      return this;
    }

    public Builder type(TypeEnum type) {
      this.instance.type(type);
      return this;
    }

    public Builder coordinates(List<BigDecimal> coordinates) {
      this.instance.coordinates(coordinates);
      return this;
    }

    /**
     * returns a built PointGeoJSONDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public PointGeoJSONDto build() {
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