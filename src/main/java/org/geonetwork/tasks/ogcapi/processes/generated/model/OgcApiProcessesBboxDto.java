/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.tasks.ogcapi.processes.generated.model;

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
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.Objects;

/** OgcApiProcessesBboxDto */
@JsonTypeName("bbox")
@JacksonXmlRootElement(localName = "OgcApiProcessesBboxDto")
@XmlRootElement(name = "OgcApiProcessesBboxDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesBboxDto implements OgcApiProcessesInputValueNoObjectDto {

  private OgcApiProcessesBboxBboxDto bbox;
  //  private OgcApiProcessesBboxBboxDto bbox = new ArrayList<>();
  private CrsEnum crs = CrsEnum._1_3_CRS84;

  public OgcApiProcessesBboxDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiProcessesBboxDto(OgcApiProcessesBboxBboxDto bbox) {
    this.bbox = bbox;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesBboxDto bbox(OgcApiProcessesBboxBboxDto bbox) {
    this.bbox = bbox;
    return this;
  }

  /**
   * Get bbox
   *
   * @return bbox
   */
  @NotNull
  @Valid
  @Schema(name = "bbox", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("bbox")
  @JacksonXmlProperty(localName = "bbox")
  @XmlElement(name = "bbox")
  public OgcApiProcessesBboxBboxDto getBbox() {
    return bbox;
  }

  public void setBbox(OgcApiProcessesBboxBboxDto bbox) {
    this.bbox = bbox;
  }

  public OgcApiProcessesBboxDto crs(CrsEnum crs) {
    this.crs = crs;
    return this;
  }

  /**
   * Get crs
   *
   * @return crs
   */
  @Valid
  @Schema(name = "crs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crs")
  @JacksonXmlProperty(localName = "crs")
  @XmlElement(name = "crs")
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
    OgcApiProcessesBboxDto bbox = (OgcApiProcessesBboxDto) o;
    return Objects.equals(this.bbox, bbox.bbox) && Objects.equals(this.crs, bbox.crs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bbox, crs);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesBboxDto {\n"
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

  /** Gets or Sets crs */
  public enum CrsEnum {
    _1_3_CRS84(URI.create("http://www.opengis.net/def/crs/OGC/1.3/CRS84")),

    _0_CRS84H(URI.create("http://www.opengis.net/def/crs/OGC/0/CRS84h"));

    private final URI value;

    CrsEnum(URI value) {
      this.value = value;
    }

    @JsonCreator
    public static CrsEnum fromValue(URI value) {
      for (CrsEnum b : CrsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    @JsonValue
    public URI getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class Builder {

    private OgcApiProcessesBboxDto instance;

    public Builder() {
      this(new OgcApiProcessesBboxDto());
    }

    protected Builder(OgcApiProcessesBboxDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesBboxDto value) {
      this.instance.setBbox(value.bbox);
      this.instance.setCrs(value.crs);
      return this;
    }

    public Builder bbox(OgcApiProcessesBboxBboxDto bbox) {
      this.instance.bbox(bbox);
      return this;
    }

    public Builder crs(CrsEnum crs) {
      this.instance.crs(crs);
      return this;
    }

    /**
     * returns a built OgcApiProcessesBboxDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesBboxDto build() {
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
