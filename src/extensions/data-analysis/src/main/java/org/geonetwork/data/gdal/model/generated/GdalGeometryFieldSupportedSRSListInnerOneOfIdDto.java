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

/** GdalGeometryFieldSupportedSRSListInnerOneOfIdDto */
@JsonTypeName("geometryField_supportedSRSList_inner_oneOf_id")
@JacksonXmlRootElement(localName = "GdalGeometryFieldSupportedSRSListInnerOneOfIdDto")
@XmlRootElement(name = "GdalGeometryFieldSupportedSRSListInnerOneOfIdDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
@NoArgsConstructor
@AllArgsConstructor
public class GdalGeometryFieldSupportedSRSListInnerOneOfIdDto {

  private String authority;

  private String code;

  public GdalGeometryFieldSupportedSRSListInnerOneOfIdDto authority(String authority) {
    this.authority = authority;
    return this;
  }

  /**
   * Get authority
   *
   * @return authority
   */
  @Schema(name = "authority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("authority")
  @JacksonXmlProperty(localName = "authority")
  @XmlElement(name = "authority")
  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  public GdalGeometryFieldSupportedSRSListInnerOneOfIdDto code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   *
   * @return code
   */
  @Schema(name = "code", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("code")
  @JacksonXmlProperty(localName = "code")
  @XmlElement(name = "code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalGeometryFieldSupportedSRSListInnerOneOfIdDto geometryFieldSupportedSRSListInnerOneOfId =
        (GdalGeometryFieldSupportedSRSListInnerOneOfIdDto) o;
    return Objects.equals(this.authority, geometryFieldSupportedSRSListInnerOneOfId.authority)
        && Objects.equals(this.code, geometryFieldSupportedSRSListInnerOneOfId.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authority, code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalGeometryFieldSupportedSRSListInnerOneOfIdDto {\n");
    sb.append("    authority: ").append(toIndentedString(authority)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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

    private GdalGeometryFieldSupportedSRSListInnerOneOfIdDto instance;

    public Builder() {
      this(new GdalGeometryFieldSupportedSRSListInnerOneOfIdDto());
    }

    protected Builder(GdalGeometryFieldSupportedSRSListInnerOneOfIdDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalGeometryFieldSupportedSRSListInnerOneOfIdDto value) {
      this.instance.setAuthority(value.authority);
      this.instance.setCode(value.code);
      return this;
    }

    public Builder authority(String authority) {
      this.instance.authority(authority);
      return this;
    }

    public Builder code(String code) {
      this.instance.code(code);
      return this;
    }

    /**
     * returns a built GdalGeometryFieldSupportedSRSListInnerOneOfIdDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalGeometryFieldSupportedSRSListInnerOneOfIdDto build() {
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
