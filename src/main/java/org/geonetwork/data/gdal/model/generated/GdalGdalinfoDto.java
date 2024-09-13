/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
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

/** GdalGdalinfoDto */
@JsonTypeName("gdalinfo")
@JacksonXmlRootElement(localName = "GdalGdalinfoDto")
@XmlRootElement(name = "GdalGdalinfoDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalGdalinfoDto {

  private String description;

  private String driverShortName;

  private String driverLongName;

  @Valid private List<String> files = new ArrayList<>();

  private GdalArrayOfTwoIntegersDto size = new GdalArrayOfTwoIntegersDto();

  private GdalCoordinateSystemDto coordinateSystem;

  @Valid private List<BigDecimal> geoTransform = new ArrayList<>();

  private GdalCornerCoordinatesDto cornerCoordinates;

  private GdalGeoJSONPolygonDto wgs84Extent;

  @Valid private List<@Valid GdalBandDto> bands = new ArrayList<>();

  private GdalStacDto stac;

  private GdalMetadataDto metadata = new GdalMetadataDto();

  public GdalGdalinfoDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GdalGdalinfoDto(GdalArrayOfTwoIntegersDto size, List<@Valid GdalBandDto> bands) {
    this.size = size;
    this.bands = bands;
  }

  public GdalGdalinfoDto description(String description) {
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

  public GdalGdalinfoDto driverShortName(String driverShortName) {
    this.driverShortName = driverShortName;
    return this;
  }

  /**
   * Get driverShortName
   *
   * @return driverShortName
   */
  @Schema(name = "driverShortName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("driverShortName")
  @JacksonXmlProperty(localName = "driverShortName")
  @XmlElement(name = "driverShortName")
  public String getDriverShortName() {
    return driverShortName;
  }

  public void setDriverShortName(String driverShortName) {
    this.driverShortName = driverShortName;
  }

  public GdalGdalinfoDto driverLongName(String driverLongName) {
    this.driverLongName = driverLongName;
    return this;
  }

  /**
   * Get driverLongName
   *
   * @return driverLongName
   */
  @Schema(name = "driverLongName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("driverLongName")
  @JacksonXmlProperty(localName = "driverLongName")
  @XmlElement(name = "driverLongName")
  public String getDriverLongName() {
    return driverLongName;
  }

  public void setDriverLongName(String driverLongName) {
    this.driverLongName = driverLongName;
  }

  public GdalGdalinfoDto files(List<String> files) {
    this.files = files;
    return this;
  }

  public GdalGdalinfoDto addFilesItem(String filesItem) {
    if (this.files == null) {
      this.files = new ArrayList<>();
    }
    this.files.add(filesItem);
    return this;
  }

  /**
   * Get files
   *
   * @return files
   */
  @Schema(name = "files", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("files")
  @JacksonXmlProperty(localName = "files")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "files")
  public List<String> getFiles() {
    return files;
  }

  public void setFiles(List<String> files) {
    this.files = files;
  }

  public GdalGdalinfoDto size(GdalArrayOfTwoIntegersDto size) {
    this.size = size;
    return this;
  }

  /**
   * Get size
   *
   * @return size
   */
  @NotNull
  @Valid
  @Schema(name = "size", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("size")
  @JacksonXmlProperty(localName = "size")
  @XmlElement(name = "size")
  public GdalArrayOfTwoIntegersDto getSize() {
    return size;
  }

  public void setSize(GdalArrayOfTwoIntegersDto size) {
    this.size = size;
  }

  public GdalGdalinfoDto coordinateSystem(GdalCoordinateSystemDto coordinateSystem) {
    this.coordinateSystem = coordinateSystem;
    return this;
  }

  /**
   * Get coordinateSystem
   *
   * @return coordinateSystem
   */
  @Valid
  @Schema(name = "coordinateSystem", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("coordinateSystem")
  @JacksonXmlProperty(localName = "coordinateSystem")
  @XmlElement(name = "coordinateSystem")
  public GdalCoordinateSystemDto getCoordinateSystem() {
    return coordinateSystem;
  }

  public void setCoordinateSystem(GdalCoordinateSystemDto coordinateSystem) {
    this.coordinateSystem = coordinateSystem;
  }

  public GdalGdalinfoDto geoTransform(List<BigDecimal> geoTransform) {
    this.geoTransform = geoTransform;
    return this;
  }

  public GdalGdalinfoDto addGeoTransformItem(BigDecimal geoTransformItem) {
    if (this.geoTransform == null) {
      this.geoTransform = new ArrayList<>();
    }
    this.geoTransform.add(geoTransformItem);
    return this;
  }

  /**
   * Get geoTransform
   *
   * @return geoTransform
   */
  @Valid
  @Schema(name = "geoTransform", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("geoTransform")
  @JacksonXmlProperty(localName = "geoTransform")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "geoTransform")
  public List<BigDecimal> getGeoTransform() {
    return geoTransform;
  }

  public void setGeoTransform(List<BigDecimal> geoTransform) {
    this.geoTransform = geoTransform;
  }

  public GdalGdalinfoDto cornerCoordinates(GdalCornerCoordinatesDto cornerCoordinates) {
    this.cornerCoordinates = cornerCoordinates;
    return this;
  }

  /**
   * Get cornerCoordinates
   *
   * @return cornerCoordinates
   */
  @Valid
  @Schema(name = "cornerCoordinates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cornerCoordinates")
  @JacksonXmlProperty(localName = "cornerCoordinates")
  @XmlElement(name = "cornerCoordinates")
  public GdalCornerCoordinatesDto getCornerCoordinates() {
    return cornerCoordinates;
  }

  public void setCornerCoordinates(GdalCornerCoordinatesDto cornerCoordinates) {
    this.cornerCoordinates = cornerCoordinates;
  }

  public GdalGdalinfoDto wgs84Extent(GdalGeoJSONPolygonDto wgs84Extent) {
    this.wgs84Extent = wgs84Extent;
    return this;
  }

  /**
   * Get wgs84Extent
   *
   * @return wgs84Extent
   */
  @Valid
  @Schema(name = "wgs84Extent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wgs84Extent")
  @JacksonXmlProperty(localName = "wgs84Extent")
  @XmlElement(name = "wgs84Extent")
  public GdalGeometryDto getWgs84Extent() {
    return wgs84Extent;
  }

  public void setWgs84Extent(GdalGeoJSONPolygonDto wgs84Extent) {
    this.wgs84Extent = wgs84Extent;
  }

  public GdalGdalinfoDto bands(List<@Valid GdalBandDto> bands) {
    this.bands = bands;
    return this;
  }

  public GdalGdalinfoDto addBandsItem(GdalBandDto bandsItem) {
    if (this.bands == null) {
      this.bands = new ArrayList<>();
    }
    this.bands.add(bandsItem);
    return this;
  }

  /**
   * Get bands
   *
   * @return bands
   */
  @NotNull
  @Valid
  @Schema(name = "bands", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("bands")
  @JacksonXmlProperty(localName = "bands")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "bands")
  public List<@Valid GdalBandDto> getBands() {
    return bands;
  }

  public void setBands(List<@Valid GdalBandDto> bands) {
    this.bands = bands;
  }

  public GdalGdalinfoDto stac(GdalStacDto stac) {
    this.stac = stac;
    return this;
  }

  /**
   * Get stac
   *
   * @return stac
   */
  @Valid
  @Schema(name = "stac", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stac")
  @JacksonXmlProperty(localName = "stac")
  @XmlElement(name = "stac")
  public GdalStacDto getStac() {
    return stac;
  }

  public void setStac(GdalStacDto stac) {
    this.stac = stac;
  }

  public GdalGdalinfoDto metadata(GdalMetadataDto metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get metadata
   *
   * @return metadata
   */
  @Valid
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  @JacksonXmlProperty(localName = "metadata")
  @XmlElement(name = "metadata")
  public GdalMetadataDto getMetadata() {
    return metadata;
  }

  public void setMetadata(GdalMetadataDto metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalGdalinfoDto gdalinfo = (GdalGdalinfoDto) o;
    return Objects.equals(this.description, gdalinfo.description)
        && Objects.equals(this.driverShortName, gdalinfo.driverShortName)
        && Objects.equals(this.driverLongName, gdalinfo.driverLongName)
        && Objects.equals(this.files, gdalinfo.files)
        && Objects.equals(this.size, gdalinfo.size)
        && Objects.equals(this.coordinateSystem, gdalinfo.coordinateSystem)
        && Objects.equals(this.geoTransform, gdalinfo.geoTransform)
        && Objects.equals(this.cornerCoordinates, gdalinfo.cornerCoordinates)
        && Objects.equals(this.wgs84Extent, gdalinfo.wgs84Extent)
        && Objects.equals(this.bands, gdalinfo.bands)
        && Objects.equals(this.stac, gdalinfo.stac)
        && Objects.equals(this.metadata, gdalinfo.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        description,
        driverShortName,
        driverLongName,
        files,
        size,
        coordinateSystem,
        geoTransform,
        cornerCoordinates,
        wgs84Extent,
        bands,
        stac,
        metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalGdalinfoDto {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    driverShortName: ").append(toIndentedString(driverShortName)).append("\n");
    sb.append("    driverLongName: ").append(toIndentedString(driverLongName)).append("\n");
    sb.append("    files: ").append(toIndentedString(files)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    coordinateSystem: ").append(toIndentedString(coordinateSystem)).append("\n");
    sb.append("    geoTransform: ").append(toIndentedString(geoTransform)).append("\n");
    sb.append("    cornerCoordinates: ").append(toIndentedString(cornerCoordinates)).append("\n");
    sb.append("    wgs84Extent: ").append(toIndentedString(wgs84Extent)).append("\n");
    sb.append("    bands: ").append(toIndentedString(bands)).append("\n");
    sb.append("    stac: ").append(toIndentedString(stac)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

    private GdalGdalinfoDto instance;

    public Builder() {
      this(new GdalGdalinfoDto());
    }

    protected Builder(GdalGdalinfoDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalGdalinfoDto value) {
      this.instance.setDescription(value.description);
      this.instance.setDriverShortName(value.driverShortName);
      this.instance.setDriverLongName(value.driverLongName);
      this.instance.setFiles(value.files);
      this.instance.setSize(value.size);
      this.instance.setCoordinateSystem(value.coordinateSystem);
      this.instance.setGeoTransform(value.geoTransform);
      this.instance.setCornerCoordinates(value.cornerCoordinates);
      this.instance.setWgs84Extent(value.wgs84Extent);
      this.instance.setBands(value.bands);
      this.instance.setStac(value.stac);
      this.instance.setMetadata(value.metadata);
      return this;
    }

    public Builder description(String description) {
      this.instance.description(description);
      return this;
    }

    public Builder driverShortName(String driverShortName) {
      this.instance.driverShortName(driverShortName);
      return this;
    }

    public Builder driverLongName(String driverLongName) {
      this.instance.driverLongName(driverLongName);
      return this;
    }

    public Builder files(List<String> files) {
      this.instance.files(files);
      return this;
    }

    public Builder size(GdalArrayOfTwoIntegersDto size) {
      this.instance.size(size);
      return this;
    }

    public Builder coordinateSystem(GdalCoordinateSystemDto coordinateSystem) {
      this.instance.coordinateSystem(coordinateSystem);
      return this;
    }

    public Builder geoTransform(List<BigDecimal> geoTransform) {
      this.instance.geoTransform(geoTransform);
      return this;
    }

    public Builder cornerCoordinates(GdalCornerCoordinatesDto cornerCoordinates) {
      this.instance.cornerCoordinates(cornerCoordinates);
      return this;
    }

    public Builder wgs84Extent(GdalGeoJSONPolygonDto wgs84Extent) {
      this.instance.wgs84Extent(wgs84Extent);
      return this;
    }

    public Builder bands(List<@Valid GdalBandDto> bands) {
      this.instance.bands(bands);
      return this;
    }

    public Builder stac(GdalStacDto stac) {
      this.instance.stac(stac);
      return this;
    }

    public Builder metadata(GdalMetadataDto metadata) {
      this.instance.metadata(metadata);
      return this;
    }

    /**
     * returns a built GdalGdalinfoDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalGdalinfoDto build() {
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
