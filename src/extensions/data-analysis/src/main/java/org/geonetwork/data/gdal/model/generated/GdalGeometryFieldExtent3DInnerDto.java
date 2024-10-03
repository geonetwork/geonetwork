/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.annotation.Generated;
import jakarta.xml.bind.annotation.*;
import java.util.Objects;

/** GdalGeometryFieldExtent3DInnerDto */
@JsonTypeName("geometryField_extent3D_inner")
@JacksonXmlRootElement(localName = "GdalGeometryFieldExtent3DInnerDto")
@XmlRootElement(name = "GdalGeometryFieldExtent3DInnerDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalGeometryFieldExtent3DInnerDto {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalGeometryFieldExtent3DInnerDto {\n");
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

    private GdalGeometryFieldExtent3DInnerDto instance;

    public Builder() {
      this(new GdalGeometryFieldExtent3DInnerDto());
    }

    protected Builder(GdalGeometryFieldExtent3DInnerDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalGeometryFieldExtent3DInnerDto value) {
      return this;
    }

    /**
     * returns a built GdalGeometryFieldExtent3DInnerDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalGeometryFieldExtent3DInnerDto build() {
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
