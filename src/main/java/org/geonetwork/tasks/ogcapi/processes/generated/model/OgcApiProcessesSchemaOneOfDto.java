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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** OgcApiProcessesSchemaOneOfDto */
@JsonTypeName("schema_oneOf")
@JacksonXmlRootElement(localName = "OgcApiProcessesSchemaOneOfDto")
@XmlRootElement(name = "OgcApiProcessesSchemaOneOfDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesSchemaOneOfDto implements OgcApiProcessesSchemaDto {

  private String title;

  private BigDecimal multipleOf;

  private BigDecimal maximum;

  private Boolean exclusiveMaximum = false;

  private BigDecimal minimum;

  private Boolean exclusiveMinimum = false;

  private Integer maxLength;

  private Integer minLength = 0;

  private String pattern;

  private Integer maxItems;

  private Integer minItems = 0;

  private Boolean uniqueItems = false;

  private Integer maxProperties;

  private Integer minProperties = 0;

  @Valid private Set<String> required = new LinkedHashSet<>();

  @Valid private List<Object> _enum = new ArrayList<>();
  private TypeEnum type;
  private String description;
  private String format;
  private Object _default;
  private Boolean nullable = false;
  private Boolean readOnly = false;
  private Boolean writeOnly = false;
  private Object example;
  private Boolean deprecated = false;
  private String contentMediaType;
  private String contentEncoding;
  private String contentSchema;

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesSchemaOneOfDto title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   *
   * @return title
   */
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  @JacksonXmlProperty(localName = "title")
  @XmlElement(name = "title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public OgcApiProcessesSchemaOneOfDto multipleOf(BigDecimal multipleOf) {
    this.multipleOf = multipleOf;
    return this;
  }

  /**
   * Get multipleOf minimum: 0
   *
   * @return multipleOf
   */
  @Valid
  @DecimalMin(value = "0", inclusive = false)
  @Schema(name = "multipleOf", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("multipleOf")
  @JacksonXmlProperty(localName = "multipleOf")
  @XmlElement(name = "multipleOf")
  public BigDecimal getMultipleOf() {
    return multipleOf;
  }

  public void setMultipleOf(BigDecimal multipleOf) {
    this.multipleOf = multipleOf;
  }

  public OgcApiProcessesSchemaOneOfDto maximum(BigDecimal maximum) {
    this.maximum = maximum;
    return this;
  }

  /**
   * Get maximum
   *
   * @return maximum
   */
  @Valid
  @Schema(name = "maximum", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maximum")
  @JacksonXmlProperty(localName = "maximum")
  @XmlElement(name = "maximum")
  public BigDecimal getMaximum() {
    return maximum;
  }

  public void setMaximum(BigDecimal maximum) {
    this.maximum = maximum;
  }

  public OgcApiProcessesSchemaOneOfDto exclusiveMaximum(Boolean exclusiveMaximum) {
    this.exclusiveMaximum = exclusiveMaximum;
    return this;
  }

  /**
   * Get exclusiveMaximum
   *
   * @return exclusiveMaximum
   */
  @Schema(name = "exclusiveMaximum", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exclusiveMaximum")
  @JacksonXmlProperty(localName = "exclusiveMaximum")
  @XmlElement(name = "exclusiveMaximum")
  public Boolean getExclusiveMaximum() {
    return exclusiveMaximum;
  }

  public void setExclusiveMaximum(Boolean exclusiveMaximum) {
    this.exclusiveMaximum = exclusiveMaximum;
  }

  public OgcApiProcessesSchemaOneOfDto minimum(BigDecimal minimum) {
    this.minimum = minimum;
    return this;
  }

  /**
   * Get minimum
   *
   * @return minimum
   */
  @Valid
  @Schema(name = "minimum", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minimum")
  @JacksonXmlProperty(localName = "minimum")
  @XmlElement(name = "minimum")
  public BigDecimal getMinimum() {
    return minimum;
  }

  public void setMinimum(BigDecimal minimum) {
    this.minimum = minimum;
  }

  public OgcApiProcessesSchemaOneOfDto exclusiveMinimum(Boolean exclusiveMinimum) {
    this.exclusiveMinimum = exclusiveMinimum;
    return this;
  }

  /**
   * Get exclusiveMinimum
   *
   * @return exclusiveMinimum
   */
  @Schema(name = "exclusiveMinimum", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exclusiveMinimum")
  @JacksonXmlProperty(localName = "exclusiveMinimum")
  @XmlElement(name = "exclusiveMinimum")
  public Boolean getExclusiveMinimum() {
    return exclusiveMinimum;
  }

  public void setExclusiveMinimum(Boolean exclusiveMinimum) {
    this.exclusiveMinimum = exclusiveMinimum;
  }

  public OgcApiProcessesSchemaOneOfDto maxLength(Integer maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  /**
   * Get maxLength minimum: 0
   *
   * @return maxLength
   */
  @Min(0)
  @Schema(name = "maxLength", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxLength")
  @JacksonXmlProperty(localName = "maxLength")
  @XmlElement(name = "maxLength")
  public Integer getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  public OgcApiProcessesSchemaOneOfDto minLength(Integer minLength) {
    this.minLength = minLength;
    return this;
  }

  /**
   * Get minLength minimum: 0
   *
   * @return minLength
   */
  @Min(0)
  @Schema(name = "minLength", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minLength")
  @JacksonXmlProperty(localName = "minLength")
  @XmlElement(name = "minLength")
  public Integer getMinLength() {
    return minLength;
  }

  public void setMinLength(Integer minLength) {
    this.minLength = minLength;
  }

  public OgcApiProcessesSchemaOneOfDto pattern(String pattern) {
    this.pattern = pattern;
    return this;
  }

  /**
   * Get pattern
   *
   * @return pattern
   */
  @Schema(name = "pattern", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pattern")
  @JacksonXmlProperty(localName = "pattern")
  @XmlElement(name = "pattern")
  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public OgcApiProcessesSchemaOneOfDto maxItems(Integer maxItems) {
    this.maxItems = maxItems;
    return this;
  }

  /**
   * Get maxItems minimum: 0
   *
   * @return maxItems
   */
  @Min(0)
  @Schema(name = "maxItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxItems")
  @JacksonXmlProperty(localName = "maxItems")
  @XmlElement(name = "maxItems")
  public Integer getMaxItems() {
    return maxItems;
  }

  public void setMaxItems(Integer maxItems) {
    this.maxItems = maxItems;
  }

  public OgcApiProcessesSchemaOneOfDto minItems(Integer minItems) {
    this.minItems = minItems;
    return this;
  }

  /**
   * Get minItems minimum: 0
   *
   * @return minItems
   */
  @Min(0)
  @Schema(name = "minItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minItems")
  @JacksonXmlProperty(localName = "minItems")
  @XmlElement(name = "minItems")
  public Integer getMinItems() {
    return minItems;
  }

  public void setMinItems(Integer minItems) {
    this.minItems = minItems;
  }

  public OgcApiProcessesSchemaOneOfDto uniqueItems(Boolean uniqueItems) {
    this.uniqueItems = uniqueItems;
    return this;
  }

  /**
   * Get uniqueItems
   *
   * @return uniqueItems
   */
  @Schema(name = "uniqueItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uniqueItems")
  @JacksonXmlProperty(localName = "uniqueItems")
  @XmlElement(name = "uniqueItems")
  public Boolean getUniqueItems() {
    return uniqueItems;
  }

  public void setUniqueItems(Boolean uniqueItems) {
    this.uniqueItems = uniqueItems;
  }

  public OgcApiProcessesSchemaOneOfDto maxProperties(Integer maxProperties) {
    this.maxProperties = maxProperties;
    return this;
  }

  /**
   * Get maxProperties minimum: 0
   *
   * @return maxProperties
   */
  @Min(0)
  @Schema(name = "maxProperties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxProperties")
  @JacksonXmlProperty(localName = "maxProperties")
  @XmlElement(name = "maxProperties")
  public Integer getMaxProperties() {
    return maxProperties;
  }

  public void setMaxProperties(Integer maxProperties) {
    this.maxProperties = maxProperties;
  }

  public OgcApiProcessesSchemaOneOfDto minProperties(Integer minProperties) {
    this.minProperties = minProperties;
    return this;
  }

  /**
   * Get minProperties minimum: 0
   *
   * @return minProperties
   */
  @Min(0)
  @Schema(name = "minProperties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minProperties")
  @JacksonXmlProperty(localName = "minProperties")
  @XmlElement(name = "minProperties")
  public Integer getMinProperties() {
    return minProperties;
  }

  public void setMinProperties(Integer minProperties) {
    this.minProperties = minProperties;
  }

  public OgcApiProcessesSchemaOneOfDto required(Set<String> required) {
    this.required = required;
    return this;
  }

  public OgcApiProcessesSchemaOneOfDto addRequiredItem(String requiredItem) {
    if (this.required == null) {
      this.required = new LinkedHashSet<>();
    }
    this.required.add(requiredItem);
    return this;
  }

  /**
   * Get required
   *
   * @return required
   */
  @Size(min = 1)
  @Schema(name = "required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required")
  @JacksonXmlProperty(localName = "required")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "required")
  public Set<String> getRequired() {
    return required;
  }

  @JsonDeserialize(as = LinkedHashSet.class)
  public void setRequired(Set<String> required) {
    this.required = required;
  }

  public OgcApiProcessesSchemaOneOfDto _enum(List<Object> _enum) {
    this._enum = _enum;
    return this;
  }

  public OgcApiProcessesSchemaOneOfDto addEnumItem(Object _enumItem) {
    if (this._enum == null) {
      this._enum = new ArrayList<>();
    }
    this._enum.add(_enumItem);
    return this;
  }

  /**
   * Get _enum
   *
   * @return _enum
   */
  @Size(min = 1)
  @Schema(name = "enum", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enum")
  @JacksonXmlProperty(localName = "enum")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "enum")
  public List<Object> getEnum() {
    return _enum;
  }

  public void setEnum(List<Object> _enum) {
    this._enum = _enum;
  }

  public OgcApiProcessesSchemaOneOfDto type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   *
   * @return type
   */
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  @JacksonXmlProperty(localName = "type")
  @XmlElement(name = "type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public OgcApiProcessesSchemaOneOfDto description(String description) {
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

  public OgcApiProcessesSchemaOneOfDto format(String format) {
    this.format = format;
    return this;
  }

  /**
   * Get format
   *
   * @return format
   */
  @Schema(name = "format", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("format")
  @JacksonXmlProperty(localName = "format")
  @XmlElement(name = "format")
  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public OgcApiProcessesSchemaOneOfDto _default(Object _default) {
    this._default = _default;
    return this;
  }

  /**
   * Get _default
   *
   * @return _default
   */
  @Schema(name = "default", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("default")
  @JacksonXmlProperty(localName = "default")
  @XmlElement(name = "default")
  public Object getDefault() {
    return _default;
  }

  public void setDefault(Object _default) {
    this._default = _default;
  }

  public OgcApiProcessesSchemaOneOfDto nullable(Boolean nullable) {
    this.nullable = nullable;
    return this;
  }

  /**
   * Get nullable
   *
   * @return nullable
   */
  @Schema(name = "nullable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nullable")
  @JacksonXmlProperty(localName = "nullable")
  @XmlElement(name = "nullable")
  public Boolean getNullable() {
    return nullable;
  }

  public void setNullable(Boolean nullable) {
    this.nullable = nullable;
  }

  public OgcApiProcessesSchemaOneOfDto readOnly(Boolean readOnly) {
    this.readOnly = readOnly;
    return this;
  }

  /**
   * Get readOnly
   *
   * @return readOnly
   */
  @Schema(name = "readOnly", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("readOnly")
  @JacksonXmlProperty(localName = "readOnly")
  @XmlElement(name = "readOnly")
  public Boolean getReadOnly() {
    return readOnly;
  }

  public void setReadOnly(Boolean readOnly) {
    this.readOnly = readOnly;
  }

  public OgcApiProcessesSchemaOneOfDto writeOnly(Boolean writeOnly) {
    this.writeOnly = writeOnly;
    return this;
  }

  /**
   * Get writeOnly
   *
   * @return writeOnly
   */
  @Schema(name = "writeOnly", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("writeOnly")
  @JacksonXmlProperty(localName = "writeOnly")
  @XmlElement(name = "writeOnly")
  public Boolean getWriteOnly() {
    return writeOnly;
  }

  public void setWriteOnly(Boolean writeOnly) {
    this.writeOnly = writeOnly;
  }

  public OgcApiProcessesSchemaOneOfDto example(Object example) {
    this.example = example;
    return this;
  }

  /**
   * Get example
   *
   * @return example
   */
  @Schema(name = "example", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("example")
  @JacksonXmlProperty(localName = "example")
  @XmlElement(name = "example")
  public Object getExample() {
    return example;
  }

  public void setExample(Object example) {
    this.example = example;
  }

  public OgcApiProcessesSchemaOneOfDto deprecated(Boolean deprecated) {
    this.deprecated = deprecated;
    return this;
  }

  /**
   * Get deprecated
   *
   * @return deprecated
   */
  @Schema(name = "deprecated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deprecated")
  @JacksonXmlProperty(localName = "deprecated")
  @XmlElement(name = "deprecated")
  public Boolean getDeprecated() {
    return deprecated;
  }

  public void setDeprecated(Boolean deprecated) {
    this.deprecated = deprecated;
  }

  public OgcApiProcessesSchemaOneOfDto contentMediaType(String contentMediaType) {
    this.contentMediaType = contentMediaType;
    return this;
  }

  /**
   * Get contentMediaType
   *
   * @return contentMediaType
   */
  @Schema(name = "contentMediaType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contentMediaType")
  @JacksonXmlProperty(localName = "contentMediaType")
  @XmlElement(name = "contentMediaType")
  public String getContentMediaType() {
    return contentMediaType;
  }

  public void setContentMediaType(String contentMediaType) {
    this.contentMediaType = contentMediaType;
  }

  public OgcApiProcessesSchemaOneOfDto contentEncoding(String contentEncoding) {
    this.contentEncoding = contentEncoding;
    return this;
  }

  /**
   * Get contentEncoding
   *
   * @return contentEncoding
   */
  @Schema(name = "contentEncoding", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contentEncoding")
  @JacksonXmlProperty(localName = "contentEncoding")
  @XmlElement(name = "contentEncoding")
  public String getContentEncoding() {
    return contentEncoding;
  }

  public void setContentEncoding(String contentEncoding) {
    this.contentEncoding = contentEncoding;
  }

  public OgcApiProcessesSchemaOneOfDto contentSchema(String contentSchema) {
    this.contentSchema = contentSchema;
    return this;
  }

  /**
   * Get contentSchema
   *
   * @return contentSchema
   */
  @Schema(name = "contentSchema", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contentSchema")
  @JacksonXmlProperty(localName = "contentSchema")
  @XmlElement(name = "contentSchema")
  public String getContentSchema() {
    return contentSchema;
  }

  public void setContentSchema(String contentSchema) {
    this.contentSchema = contentSchema;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesSchemaOneOfDto schemaOneOf = (OgcApiProcessesSchemaOneOfDto) o;
    return Objects.equals(this.title, schemaOneOf.title)
        && Objects.equals(this.multipleOf, schemaOneOf.multipleOf)
        && Objects.equals(this.maximum, schemaOneOf.maximum)
        && Objects.equals(this.exclusiveMaximum, schemaOneOf.exclusiveMaximum)
        && Objects.equals(this.minimum, schemaOneOf.minimum)
        && Objects.equals(this.exclusiveMinimum, schemaOneOf.exclusiveMinimum)
        && Objects.equals(this.maxLength, schemaOneOf.maxLength)
        && Objects.equals(this.minLength, schemaOneOf.minLength)
        && Objects.equals(this.pattern, schemaOneOf.pattern)
        && Objects.equals(this.maxItems, schemaOneOf.maxItems)
        && Objects.equals(this.minItems, schemaOneOf.minItems)
        && Objects.equals(this.uniqueItems, schemaOneOf.uniqueItems)
        && Objects.equals(this.maxProperties, schemaOneOf.maxProperties)
        && Objects.equals(this.minProperties, schemaOneOf.minProperties)
        && Objects.equals(this.required, schemaOneOf.required)
        && Objects.equals(this._enum, schemaOneOf._enum)
        && Objects.equals(this.type, schemaOneOf.type)
        && Objects.equals(this.description, schemaOneOf.description)
        && Objects.equals(this.format, schemaOneOf.format)
        && Objects.equals(this._default, schemaOneOf._default)
        && Objects.equals(this.nullable, schemaOneOf.nullable)
        && Objects.equals(this.readOnly, schemaOneOf.readOnly)
        && Objects.equals(this.writeOnly, schemaOneOf.writeOnly)
        && Objects.equals(this.example, schemaOneOf.example)
        && Objects.equals(this.deprecated, schemaOneOf.deprecated)
        && Objects.equals(this.contentMediaType, schemaOneOf.contentMediaType)
        && Objects.equals(this.contentEncoding, schemaOneOf.contentEncoding)
        && Objects.equals(this.contentSchema, schemaOneOf.contentSchema);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        title,
        multipleOf,
        maximum,
        exclusiveMaximum,
        minimum,
        exclusiveMinimum,
        maxLength,
        minLength,
        pattern,
        maxItems,
        minItems,
        uniqueItems,
        maxProperties,
        minProperties,
        required,
        _enum,
        type,
        description,
        format,
        _default,
        nullable,
        readOnly,
        writeOnly,
        example,
        deprecated,
        contentMediaType,
        contentEncoding,
        contentSchema);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesSchemaOneOfDto {\n"
            + "    title: "
            + toIndentedString(title)
            + "\n"
            + "    multipleOf: "
            + toIndentedString(multipleOf)
            + "\n"
            + "    maximum: "
            + toIndentedString(maximum)
            + "\n"
            + "    exclusiveMaximum: "
            + toIndentedString(exclusiveMaximum)
            + "\n"
            + "    minimum: "
            + toIndentedString(minimum)
            + "\n"
            + "    exclusiveMinimum: "
            + toIndentedString(exclusiveMinimum)
            + "\n"
            + "    maxLength: "
            + toIndentedString(maxLength)
            + "\n"
            + "    minLength: "
            + toIndentedString(minLength)
            + "\n"
            + "    pattern: "
            + toIndentedString(pattern)
            + "\n"
            + "    maxItems: "
            + toIndentedString(maxItems)
            + "\n"
            + "    minItems: "
            + toIndentedString(minItems)
            + "\n"
            + "    uniqueItems: "
            + toIndentedString(uniqueItems)
            + "\n"
            + "    maxProperties: "
            + toIndentedString(maxProperties)
            + "\n"
            + "    minProperties: "
            + toIndentedString(minProperties)
            + "\n"
            + "    required: "
            + toIndentedString(required)
            + "\n"
            + "    _enum: "
            + toIndentedString(_enum)
            + "\n"
            + "    type: "
            + toIndentedString(type)
            + "\n"
            + "    description: "
            + toIndentedString(description)
            + "\n"
            + "    format: "
            + toIndentedString(format)
            + "\n"
            + "    _default: "
            + toIndentedString(_default)
            + "\n"
            + "    nullable: "
            + toIndentedString(nullable)
            + "\n"
            + "    readOnly: "
            + toIndentedString(readOnly)
            + "\n"
            + "    writeOnly: "
            + toIndentedString(writeOnly)
            + "\n"
            + "    example: "
            + toIndentedString(example)
            + "\n"
            + "    deprecated: "
            + toIndentedString(deprecated)
            + "\n"
            + "    contentMediaType: "
            + toIndentedString(contentMediaType)
            + "\n"
            + "    contentEncoding: "
            + toIndentedString(contentEncoding)
            + "\n"
            + "    contentSchema: "
            + toIndentedString(contentSchema)
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

  /** Gets or Sets type */
  public enum TypeEnum {
    ARRAY("array"),

    BOOLEAN("boolean"),

    INTEGER("integer"),

    NUMBER("number"),

    OBJECT("object"),

    STRING("string");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
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

    private OgcApiProcessesSchemaOneOfDto instance;

    public Builder() {
      this(new OgcApiProcessesSchemaOneOfDto());
    }

    protected Builder(OgcApiProcessesSchemaOneOfDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesSchemaOneOfDto value) {
      this.instance.setTitle(value.title);
      this.instance.setMultipleOf(value.multipleOf);
      this.instance.setMaximum(value.maximum);
      this.instance.setExclusiveMaximum(value.exclusiveMaximum);
      this.instance.setMinimum(value.minimum);
      this.instance.setExclusiveMinimum(value.exclusiveMinimum);
      this.instance.setMaxLength(value.maxLength);
      this.instance.setMinLength(value.minLength);
      this.instance.setPattern(value.pattern);
      this.instance.setMaxItems(value.maxItems);
      this.instance.setMinItems(value.minItems);
      this.instance.setUniqueItems(value.uniqueItems);
      this.instance.setMaxProperties(value.maxProperties);
      this.instance.setMinProperties(value.minProperties);
      this.instance.setRequired(value.required);
      this.instance.setEnum(value._enum);
      this.instance.setType(value.type);
      this.instance.setDescription(value.description);
      this.instance.setFormat(value.format);
      this.instance.setDefault(value._default);
      this.instance.setNullable(value.nullable);
      this.instance.setReadOnly(value.readOnly);
      this.instance.setWriteOnly(value.writeOnly);
      this.instance.setExample(value.example);
      this.instance.setDeprecated(value.deprecated);
      this.instance.setContentMediaType(value.contentMediaType);
      this.instance.setContentEncoding(value.contentEncoding);
      this.instance.setContentSchema(value.contentSchema);
      return this;
    }

    public Builder title(String title) {
      this.instance.title(title);
      return this;
    }

    public Builder multipleOf(BigDecimal multipleOf) {
      this.instance.multipleOf(multipleOf);
      return this;
    }

    public Builder maximum(BigDecimal maximum) {
      this.instance.maximum(maximum);
      return this;
    }

    public Builder exclusiveMaximum(Boolean exclusiveMaximum) {
      this.instance.exclusiveMaximum(exclusiveMaximum);
      return this;
    }

    public Builder minimum(BigDecimal minimum) {
      this.instance.minimum(minimum);
      return this;
    }

    public Builder exclusiveMinimum(Boolean exclusiveMinimum) {
      this.instance.exclusiveMinimum(exclusiveMinimum);
      return this;
    }

    public Builder maxLength(Integer maxLength) {
      this.instance.maxLength(maxLength);
      return this;
    }

    public Builder minLength(Integer minLength) {
      this.instance.minLength(minLength);
      return this;
    }

    public Builder pattern(String pattern) {
      this.instance.pattern(pattern);
      return this;
    }

    public Builder maxItems(Integer maxItems) {
      this.instance.maxItems(maxItems);
      return this;
    }

    public Builder minItems(Integer minItems) {
      this.instance.minItems(minItems);
      return this;
    }

    public Builder uniqueItems(Boolean uniqueItems) {
      this.instance.uniqueItems(uniqueItems);
      return this;
    }

    public Builder maxProperties(Integer maxProperties) {
      this.instance.maxProperties(maxProperties);
      return this;
    }

    public Builder minProperties(Integer minProperties) {
      this.instance.minProperties(minProperties);
      return this;
    }

    public Builder required(Set<String> required) {
      this.instance.required(required);
      return this;
    }

    public Builder _enum(List<Object> _enum) {
      this.instance._enum(_enum);
      return this;
    }

    public Builder type(TypeEnum type) {
      this.instance.type(type);
      return this;
    }

    public Builder description(String description) {
      this.instance.description(description);
      return this;
    }

    public Builder format(String format) {
      this.instance.format(format);
      return this;
    }

    public Builder _default(Object _default) {
      this.instance._default(_default);
      return this;
    }

    public Builder nullable(Boolean nullable) {
      this.instance.nullable(nullable);
      return this;
    }

    public Builder readOnly(Boolean readOnly) {
      this.instance.readOnly(readOnly);
      return this;
    }

    public Builder writeOnly(Boolean writeOnly) {
      this.instance.writeOnly(writeOnly);
      return this;
    }

    public Builder example(Object example) {
      this.instance.example(example);
      return this;
    }

    public Builder deprecated(Boolean deprecated) {
      this.instance.deprecated(deprecated);
      return this;
    }

    public Builder contentMediaType(String contentMediaType) {
      this.instance.contentMediaType(contentMediaType);
      return this;
    }

    public Builder contentEncoding(String contentEncoding) {
      this.instance.contentEncoding(contentEncoding);
      return this;
    }

    public Builder contentSchema(String contentSchema) {
      this.instance.contentSchema(contentSchema);
      return this;
    }

    /**
     * returns a built OgcApiProcessesSchemaOneOfDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesSchemaOneOfDto build() {
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
