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
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/** OgcApiProcessesQualifiedInputValueDto */
@JsonTypeName("qualifiedInputValue")
@JacksonXmlRootElement(localName = "OgcApiProcessesQualifiedInputValueDto")
@XmlRootElement(name = "OgcApiProcessesQualifiedInputValueDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesQualifiedInputValueDto implements OgcApiProcessesInlineOrRefDataDto {

  private String mediaType;

  private String encoding;

  private OgcApiProcessesFormatSchemaDto schema;

  private OgcApiProcessesInputValueDto value;

  public OgcApiProcessesQualifiedInputValueDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiProcessesQualifiedInputValueDto(OgcApiProcessesInputValueDto value) {
    this.value = value;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesQualifiedInputValueDto mediaType(String mediaType) {
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

  public OgcApiProcessesQualifiedInputValueDto encoding(String encoding) {
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

  public OgcApiProcessesQualifiedInputValueDto schema(OgcApiProcessesFormatSchemaDto schema) {
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

  public OgcApiProcessesQualifiedInputValueDto value(OgcApiProcessesInputValueDto value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   *
   * @return value
   */
  @NotNull
  @Valid
  @Schema(name = "value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value")
  @JacksonXmlProperty(localName = "value")
  @XmlElement(name = "value")
  public OgcApiProcessesInputValueDto getValue() {
    return value;
  }

  public void setValue(OgcApiProcessesInputValueDto value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesQualifiedInputValueDto qualifiedInputValue =
        (OgcApiProcessesQualifiedInputValueDto) o;
    return Objects.equals(this.mediaType, qualifiedInputValue.mediaType)
        && Objects.equals(this.encoding, qualifiedInputValue.encoding)
        && Objects.equals(this.schema, qualifiedInputValue.schema)
        && Objects.equals(this.value, qualifiedInputValue.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mediaType, encoding, schema, value);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesQualifiedInputValueDto {\n"
            + "    mediaType: "
            + toIndentedString(mediaType)
            + "\n"
            + "    encoding: "
            + toIndentedString(encoding)
            + "\n"
            + "    schema: "
            + toIndentedString(schema)
            + "\n"
            + "    value: "
            + toIndentedString(value)
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

    private OgcApiProcessesQualifiedInputValueDto instance;

    public Builder() {
      this(new OgcApiProcessesQualifiedInputValueDto());
    }

    protected Builder(OgcApiProcessesQualifiedInputValueDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesQualifiedInputValueDto value) {
      this.instance.setMediaType(value.mediaType);
      this.instance.setEncoding(value.encoding);
      this.instance.setSchema(value.schema);
      this.instance.setValue(value.value);
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

    public Builder value(OgcApiProcessesInputValueDto value) {
      this.instance.value(value);
      return this;
    }

    /**
     * returns a built OgcApiProcessesQualifiedInputValueDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesQualifiedInputValueDto build() {
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
