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

/** OgcApiProcessesFormatDto */
@JsonTypeName("format")
@JacksonXmlRootElement(localName = "OgcApiProcessesFormatDto")
@XmlRootElement(name = "OgcApiProcessesFormatDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesFormatDto {

  private String mediaType;

  private String encoding;

  private OgcApiProcessesFormatSchemaDto schema;

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesFormatDto mediaType(String mediaType) {
    this.mediaType = mediaType;
    return this;
  }

  /**
   * Get mediaType
   *
   * @return mediaType
   */
  @Schema(name = "mediaType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mediaType")
  @JacksonXmlProperty(localName = "mediaType")
  @XmlElement(name = "mediaType")
  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  public OgcApiProcessesFormatDto encoding(String encoding) {
    this.encoding = encoding;
    return this;
  }

  /**
   * Get encoding
   *
   * @return encoding
   */
  @Schema(name = "encoding", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("encoding")
  @JacksonXmlProperty(localName = "encoding")
  @XmlElement(name = "encoding")
  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public OgcApiProcessesFormatDto schema(OgcApiProcessesFormatSchemaDto schema) {
    this.schema = schema;
    return this;
  }

  /**
   * Get schema
   *
   * @return schema
   */
  @Valid
  @Schema(name = "schema", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("schema")
  @JacksonXmlProperty(localName = "schema")
  @XmlElement(name = "schema")
  public OgcApiProcessesFormatSchemaDto getSchema() {
    return schema;
  }

  public void setSchema(OgcApiProcessesFormatSchemaDto schema) {
    this.schema = schema;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesFormatDto format = (OgcApiProcessesFormatDto) o;
    return Objects.equals(this.mediaType, format.mediaType)
        && Objects.equals(this.encoding, format.encoding)
        && Objects.equals(this.schema, format.schema);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mediaType, encoding, schema);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesFormatDto {\n"
            + "    mediaType: "
            + toIndentedString(mediaType)
            + "\n"
            + "    encoding: "
            + toIndentedString(encoding)
            + "\n"
            + "    schema: "
            + toIndentedString(schema)
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

    private OgcApiProcessesFormatDto instance;

    public Builder() {
      this(new OgcApiProcessesFormatDto());
    }

    protected Builder(OgcApiProcessesFormatDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesFormatDto value) {
      this.instance.setMediaType(value.mediaType);
      this.instance.setEncoding(value.encoding);
      this.instance.setSchema(value.schema);
      return this;
    }

    public Builder mediaType(String mediaType) {
      this.instance.mediaType(mediaType);
      return this;
    }

    public Builder encoding(String encoding) {
      this.instance.encoding(encoding);
      return this;
    }

    public Builder schema(OgcApiProcessesFormatSchemaDto schema) {
      this.instance.schema(schema);
      return this;
    }

    /**
     * returns a built OgcApiProcessesFormatDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesFormatDto build() {
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
