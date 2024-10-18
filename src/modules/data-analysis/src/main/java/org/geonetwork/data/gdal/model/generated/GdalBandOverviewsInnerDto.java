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
import jakarta.xml.bind.annotation.*;
import java.util.Objects;

/** GdalBandOverviewsInnerDto */
@JsonTypeName("band_overviews_inner")
@JacksonXmlRootElement(localName = "GdalBandOverviewsInnerDto")
@XmlRootElement(name = "GdalBandOverviewsInnerDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalBandOverviewsInnerDto {

  private GdalArrayOfTwoIntegersDto size = new GdalArrayOfTwoIntegersDto();

  public GdalBandOverviewsInnerDto size(GdalArrayOfTwoIntegersDto size) {
    this.size = size;
    return this;
  }

  /**
   * Get size
   *
   * @return size
   */
  @Valid
  @Schema(name = "size", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("size")
  @JacksonXmlProperty(localName = "size")
  @XmlElement(name = "size")
  public GdalArrayOfTwoIntegersDto getSize() {
    return size;
  }

  public void setSize(GdalArrayOfTwoIntegersDto size) {
    this.size = size;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalBandOverviewsInnerDto bandOverviewsInner = (GdalBandOverviewsInnerDto) o;
    return Objects.equals(this.size, bandOverviewsInner.size);
  }

  @Override
  public int hashCode() {
    return Objects.hash(size);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalBandOverviewsInnerDto {\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
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

    private GdalBandOverviewsInnerDto instance;

    public Builder() {
      this(new GdalBandOverviewsInnerDto());
    }

    protected Builder(GdalBandOverviewsInnerDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalBandOverviewsInnerDto value) {
      this.instance.setSize(value.size);
      return this;
    }

    public Builder size(GdalArrayOfTwoIntegersDto size) {
      this.instance.size(size);
      return this;
    }

    /**
     * returns a built GdalBandOverviewsInnerDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalBandOverviewsInnerDto build() {
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
