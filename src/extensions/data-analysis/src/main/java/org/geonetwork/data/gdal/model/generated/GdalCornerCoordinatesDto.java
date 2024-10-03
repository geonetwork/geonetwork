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
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.util.Objects;

/** GdalCornerCoordinatesDto */
@JsonTypeName("cornerCoordinates")
@JacksonXmlRootElement(localName = "GdalCornerCoordinatesDto")
@XmlRootElement(name = "GdalCornerCoordinatesDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalCornerCoordinatesDto {

  private GdalArrayOfTwoNumbersDto upperLeft = new GdalArrayOfTwoNumbersDto();

  private GdalArrayOfTwoNumbersDto lowerLeft = new GdalArrayOfTwoNumbersDto();

  private GdalArrayOfTwoNumbersDto lowerRight = new GdalArrayOfTwoNumbersDto();

  private GdalArrayOfTwoNumbersDto upperRight = new GdalArrayOfTwoNumbersDto();

  private GdalArrayOfTwoNumbersDto center = new GdalArrayOfTwoNumbersDto();

  public GdalCornerCoordinatesDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GdalCornerCoordinatesDto(
      GdalArrayOfTwoNumbersDto upperLeft,
      GdalArrayOfTwoNumbersDto lowerLeft,
      GdalArrayOfTwoNumbersDto lowerRight,
      GdalArrayOfTwoNumbersDto upperRight,
      GdalArrayOfTwoNumbersDto center) {
    this.upperLeft = upperLeft;
    this.lowerLeft = lowerLeft;
    this.lowerRight = lowerRight;
    this.upperRight = upperRight;
    this.center = center;
  }

  public GdalCornerCoordinatesDto upperLeft(GdalArrayOfTwoNumbersDto upperLeft) {
    this.upperLeft = upperLeft;
    return this;
  }

  /**
   * Get upperLeft
   *
   * @return upperLeft
   */
  @NotNull
  @Valid
  @Schema(name = "upperLeft", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("upperLeft")
  @JacksonXmlProperty(localName = "upperLeft")
  @XmlElement(name = "upperLeft")
  public GdalArrayOfTwoNumbersDto getUpperLeft() {
    return upperLeft;
  }

  public void setUpperLeft(GdalArrayOfTwoNumbersDto upperLeft) {
    this.upperLeft = upperLeft;
  }

  public GdalCornerCoordinatesDto lowerLeft(GdalArrayOfTwoNumbersDto lowerLeft) {
    this.lowerLeft = lowerLeft;
    return this;
  }

  /**
   * Get lowerLeft
   *
   * @return lowerLeft
   */
  @NotNull
  @Valid
  @Schema(name = "lowerLeft", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lowerLeft")
  @JacksonXmlProperty(localName = "lowerLeft")
  @XmlElement(name = "lowerLeft")
  public GdalArrayOfTwoNumbersDto getLowerLeft() {
    return lowerLeft;
  }

  public void setLowerLeft(GdalArrayOfTwoNumbersDto lowerLeft) {
    this.lowerLeft = lowerLeft;
  }

  public GdalCornerCoordinatesDto lowerRight(GdalArrayOfTwoNumbersDto lowerRight) {
    this.lowerRight = lowerRight;
    return this;
  }

  /**
   * Get lowerRight
   *
   * @return lowerRight
   */
  @NotNull
  @Valid
  @Schema(name = "lowerRight", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lowerRight")
  @JacksonXmlProperty(localName = "lowerRight")
  @XmlElement(name = "lowerRight")
  public GdalArrayOfTwoNumbersDto getLowerRight() {
    return lowerRight;
  }

  public void setLowerRight(GdalArrayOfTwoNumbersDto lowerRight) {
    this.lowerRight = lowerRight;
  }

  public GdalCornerCoordinatesDto upperRight(GdalArrayOfTwoNumbersDto upperRight) {
    this.upperRight = upperRight;
    return this;
  }

  /**
   * Get upperRight
   *
   * @return upperRight
   */
  @NotNull
  @Valid
  @Schema(name = "upperRight", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("upperRight")
  @JacksonXmlProperty(localName = "upperRight")
  @XmlElement(name = "upperRight")
  public GdalArrayOfTwoNumbersDto getUpperRight() {
    return upperRight;
  }

  public void setUpperRight(GdalArrayOfTwoNumbersDto upperRight) {
    this.upperRight = upperRight;
  }

  public GdalCornerCoordinatesDto center(GdalArrayOfTwoNumbersDto center) {
    this.center = center;
    return this;
  }

  /**
   * Get center
   *
   * @return center
   */
  @NotNull
  @Valid
  @Schema(name = "center", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("center")
  @JacksonXmlProperty(localName = "center")
  @XmlElement(name = "center")
  public GdalArrayOfTwoNumbersDto getCenter() {
    return center;
  }

  public void setCenter(GdalArrayOfTwoNumbersDto center) {
    this.center = center;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalCornerCoordinatesDto cornerCoordinates = (GdalCornerCoordinatesDto) o;
    return Objects.equals(this.upperLeft, cornerCoordinates.upperLeft)
        && Objects.equals(this.lowerLeft, cornerCoordinates.lowerLeft)
        && Objects.equals(this.lowerRight, cornerCoordinates.lowerRight)
        && Objects.equals(this.upperRight, cornerCoordinates.upperRight)
        && Objects.equals(this.center, cornerCoordinates.center);
  }

  @Override
  public int hashCode() {
    return Objects.hash(upperLeft, lowerLeft, lowerRight, upperRight, center);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalCornerCoordinatesDto {\n");
    sb.append("    upperLeft: ").append(toIndentedString(upperLeft)).append("\n");
    sb.append("    lowerLeft: ").append(toIndentedString(lowerLeft)).append("\n");
    sb.append("    lowerRight: ").append(toIndentedString(lowerRight)).append("\n");
    sb.append("    upperRight: ").append(toIndentedString(upperRight)).append("\n");
    sb.append("    center: ").append(toIndentedString(center)).append("\n");
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

    private GdalCornerCoordinatesDto instance;

    public Builder() {
      this(new GdalCornerCoordinatesDto());
    }

    protected Builder(GdalCornerCoordinatesDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalCornerCoordinatesDto value) {
      this.instance.setUpperLeft(value.upperLeft);
      this.instance.setLowerLeft(value.lowerLeft);
      this.instance.setLowerRight(value.lowerRight);
      this.instance.setUpperRight(value.upperRight);
      this.instance.setCenter(value.center);
      return this;
    }

    public Builder upperLeft(GdalArrayOfTwoNumbersDto upperLeft) {
      this.instance.upperLeft(upperLeft);
      return this;
    }

    public Builder lowerLeft(GdalArrayOfTwoNumbersDto lowerLeft) {
      this.instance.lowerLeft(lowerLeft);
      return this;
    }

    public Builder lowerRight(GdalArrayOfTwoNumbersDto lowerRight) {
      this.instance.lowerRight(lowerRight);
      return this;
    }

    public Builder upperRight(GdalArrayOfTwoNumbersDto upperRight) {
      this.instance.upperRight(upperRight);
      return this;
    }

    public Builder center(GdalArrayOfTwoNumbersDto center) {
      this.instance.center(center);
      return this;
    }

    /**
     * returns a built GdalCornerCoordinatesDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalCornerCoordinatesDto build() {
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
