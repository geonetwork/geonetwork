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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** The spatial extent of the features in the collection. */
@Schema(
    name = "extent_spatial",
    description = "The spatial extent of the features in the collection.")
@JsonTypeName("extent_spatial")
@JacksonXmlRootElement(localName = "ExtentSpatialDto")
@XmlRootElement(name = "ExtentSpatialDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]",
    comments = "Generator version: 7.6.0")
public class ExtentSpatialDto {

  @Valid private List<ExtentSpatialBboxInnerDto> bbox = new ArrayList<>();
  private CrsEnum crs = CrsEnum._1_3_CRS84;

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public ExtentSpatialDto bbox(List<ExtentSpatialBboxInnerDto> bbox) {
    this.bbox = bbox;
    return this;
  }

  public ExtentSpatialDto addBboxItem(ExtentSpatialBboxInnerDto bboxItem) {
    if (this.bbox == null) {
      this.bbox = new ArrayList<>();
    }
    this.bbox.add(bboxItem);
    return this;
  }

  /**
   * One or more bounding boxes that describe the spatial extent of the dataset. In the Core only a
   * single bounding box is supported. Extensions may support additional areas. The first bounding
   * box describes the overall spatial extent of the data. All subsequent bounding boxes describe
   * more precise bounding boxes, e.g., to identify clusters of data. Clients only interested in the
   * overall spatial extent will only need to access the first bounding box in the array.
   *
   * @return bbox
   */
  @Valid
  @Size(min = 1)
  @Schema(
      name = "bbox",
      description =
          "One or more bounding boxes that describe the spatial extent of the dataset. In the Core"
              + " only a single bounding box is supported.  Extensions may support additional"
              + " areas. The first bounding box describes the overall spatial extent of the data."
              + " All subsequent bounding boxes describe more precise bounding boxes, e.g., to"
              + " identify clusters of data. Clients only interested in the overall spatial extent"
              + " will only need to access the first bounding box in the array.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bbox")
  @JacksonXmlProperty(localName = "bbox")
  public List<ExtentSpatialBboxInnerDto> getBbox() {
    return bbox;
  }

  public void setBbox(List<ExtentSpatialBboxInnerDto> bbox) {
    this.bbox = bbox;
  }

  public ExtentSpatialDto crs(CrsEnum crs) {
    this.crs = crs;
    return this;
  }

  /**
   * Coordinate reference system of the coordinates in the spatial extent (property `bbox`). The
   * default reference system is WGS 84 longitude/latitude. In the Core the only other supported
   * coordinate reference system is WGS 84 longitude/latitude/ellipsoidal height for coordinates
   * with height. Extensions may support additional coordinate reference systems and add additional
   * enum values.
   *
   * @return crs
   */
  @Schema(
      name = "crs",
      description =
          "Coordinate reference system of the coordinates in the spatial extent (property `bbox`)."
              + " The default reference system is WGS 84 longitude/latitude. In the Core the only"
              + " other supported coordinate reference system is WGS 84"
              + " longitude/latitude/ellipsoidal height for coordinates with height. Extensions may"
              + " support additional coordinate reference systems and add additional enum values.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crs")
  @JacksonXmlProperty(localName = "crs")
  public CrsEnum getCrs() {
    return crs;
  }

  public void setCrs(CrsEnum crs) {
    this.crs = crs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExtentSpatialDto extentSpatial = (ExtentSpatialDto) o;
    return Objects.equals(this.bbox, extentSpatial.bbox)
        && Objects.equals(this.crs, extentSpatial.crs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bbox, crs);
  }

  @Override
  public String toString() {
    String sb =
        "class ExtentSpatialDto {\n"
            + "    bbox: "
            + toIndentedString(bbox)
            + "\n"
            + "    crs: "
            + toIndentedString(crs)
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

  /**
   * Coordinate reference system of the coordinates in the spatial extent (property `bbox`). The
   * default reference system is WGS 84 longitude/latitude. In the Core the only other supported
   * coordinate reference system is WGS 84 longitude/latitude/ellipsoidal height for coordinates
   * with height. Extensions may support additional coordinate reference systems and add additional
   * enum values.
   */
  public enum CrsEnum {
    _1_3_CRS84("http://www.opengis.net/def/crs/OGC/1.3/CRS84"),

    _0_CRS84H("http://www.opengis.net/def/crs/OGC/0/CRS84h");

    private final String value;

    CrsEnum(String value) {
      this.value = value;
    }

    @JsonCreator
    public static CrsEnum fromValue(String value) {
      for (CrsEnum b : CrsEnum.values()) {
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

    private ExtentSpatialDto instance;

    public Builder() {
      this(new ExtentSpatialDto());
    }

    protected Builder(ExtentSpatialDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(ExtentSpatialDto value) {
      this.instance.setBbox(value.bbox);
      this.instance.setCrs(value.crs);
      return this;
    }

    public Builder bbox(List<ExtentSpatialBboxInnerDto> bbox) {
      this.instance.bbox(bbox);
      return this;
    }

    public Builder crs(CrsEnum crs) {
      this.instance.crs(crs);
      return this;
    }

    /**
     * returns a built ExtentSpatialDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public ExtentSpatialDto build() {
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
