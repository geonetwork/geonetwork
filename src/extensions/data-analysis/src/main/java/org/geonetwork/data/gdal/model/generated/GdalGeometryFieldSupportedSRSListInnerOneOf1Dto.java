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
import jakarta.xml.bind.annotation.*;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/** GdalGeometryFieldSupportedSRSListInnerOneOf1Dto */
@JsonTypeName("geometryField_supportedSRSList_inner_oneOf_1")
@JacksonXmlRootElement(localName = "GdalGeometryFieldSupportedSRSListInnerOneOf1Dto")
@XmlRootElement(name = "GdalGeometryFieldSupportedSRSListInnerOneOf1Dto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
@NoArgsConstructor
@AllArgsConstructor
public class GdalGeometryFieldSupportedSRSListInnerOneOf1Dto
    implements GdalGeometryFieldSupportedSRSListInnerDto {

  private String wkt;

  public GdalGeometryFieldSupportedSRSListInnerOneOf1Dto wkt(String wkt) {
    this.wkt = wkt;
    return this;
  }

  /**
   * Get wkt
   *
   * @return wkt
   */
  @Schema(name = "wkt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wkt")
  @JacksonXmlProperty(localName = "wkt")
  @XmlElement(name = "wkt")
  public String getWkt() {
    return wkt;
  }

  public void setWkt(String wkt) {
    this.wkt = wkt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalGeometryFieldSupportedSRSListInnerOneOf1Dto geometryFieldSupportedSRSListInnerOneOf1 =
        (GdalGeometryFieldSupportedSRSListInnerOneOf1Dto) o;
    return Objects.equals(this.wkt, geometryFieldSupportedSRSListInnerOneOf1.wkt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wkt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalGeometryFieldSupportedSRSListInnerOneOf1Dto {\n");
    sb.append("    wkt: ").append(toIndentedString(wkt)).append("\n");
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

    private GdalGeometryFieldSupportedSRSListInnerOneOf1Dto instance;

    public Builder() {
      this(new GdalGeometryFieldSupportedSRSListInnerOneOf1Dto());
    }

    protected Builder(GdalGeometryFieldSupportedSRSListInnerOneOf1Dto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalGeometryFieldSupportedSRSListInnerOneOf1Dto value) {
      this.instance.setWkt(value.wkt);
      return this;
    }

    public Builder wkt(String wkt) {
      this.instance.wkt(wkt);
      return this;
    }

    /**
     * returns a built GdalGeometryFieldSupportedSRSListInnerOneOf1Dto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalGeometryFieldSupportedSRSListInnerOneOf1Dto build() {
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
