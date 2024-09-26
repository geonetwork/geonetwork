/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.tasks.ogcapi.processes.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/** OgcApiProcessesOutputDto */
@JsonTypeName("output")
@JacksonXmlRootElement(localName = "OgcApiProcessesOutputDto")
@XmlRootElement(name = "OgcApiProcessesOutputDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesOutputDto {

  private OgcApiProcessesFormatDto format;

  private OgcApiProcessesTransmissionModeDto transmissionMode;

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesOutputDto format(OgcApiProcessesFormatDto format) {
    this.format = format;
    return this;
  }

  /**
   * Get format
   *
   * @return format
   */
  @Valid
  @Schema(name = "format", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("format")
  @JacksonXmlProperty(localName = "format")
  @XmlElement(name = "format")
  public OgcApiProcessesFormatDto getFormat() {
    return format;
  }

  public void setFormat(OgcApiProcessesFormatDto format) {
    this.format = format;
  }

  public OgcApiProcessesOutputDto transmissionMode(
      OgcApiProcessesTransmissionModeDto transmissionMode) {
    this.transmissionMode = transmissionMode;
    return this;
  }

  /**
   * Get transmissionMode
   *
   * @return transmissionMode
   */
  @Valid
  @Schema(name = "transmissionMode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("transmissionMode")
  @JacksonXmlProperty(localName = "transmissionMode")
  @XmlElement(name = "transmissionMode")
  public OgcApiProcessesTransmissionModeDto getTransmissionMode() {
    return transmissionMode;
  }

  public void setTransmissionMode(OgcApiProcessesTransmissionModeDto transmissionMode) {
    this.transmissionMode = transmissionMode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesOutputDto output = (OgcApiProcessesOutputDto) o;
    return Objects.equals(this.format, output.format)
        && Objects.equals(this.transmissionMode, output.transmissionMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(format, transmissionMode);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesOutputDto {\n"
            + "    format: "
            + toIndentedString(format)
            + "\n"
            + "    transmissionMode: "
            + toIndentedString(transmissionMode)
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

  public static class Builder {

    private OgcApiProcessesOutputDto instance;

    public Builder() {
      this(new OgcApiProcessesOutputDto());
    }

    protected Builder(OgcApiProcessesOutputDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesOutputDto value) {
      this.instance.setFormat(value.format);
      this.instance.setTransmissionMode(value.transmissionMode);
      return this;
    }

    public Builder format(OgcApiProcessesFormatDto format) {
      this.instance.format(format);
      return this;
    }

    public Builder transmissionMode(OgcApiProcessesTransmissionModeDto transmissionMode) {
      this.instance.transmissionMode(transmissionMode);
      return this;
    }

    /**
     * returns a built OgcApiProcessesOutputDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesOutputDto build() {
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
