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
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.util.Objects;

/** GdalFieldDto */
@JsonTypeName("field")
@JacksonXmlRootElement(localName = "GdalFieldDto")
@XmlRootElement(name = "GdalFieldDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalFieldDto {

  private String name;

  private GdalFieldTypeDto type;

  private GdalFieldSubTypeDto subType;

  private Integer width;

  private Integer precision;

  private Boolean nullable;

  private Boolean uniqueConstraint;

  private String defaultValue;

  private String alias;

  private String domainName;

  private String comment;

  private String timezone;

  public GdalFieldDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GdalFieldDto(
      String name, GdalFieldTypeDto type, Boolean nullable, Boolean uniqueConstraint) {
    this.name = name;
    this.type = type;
    this.nullable = nullable;
    this.uniqueConstraint = uniqueConstraint;
  }

  public GdalFieldDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   *
   * @return name
   */
  @NotNull
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  @JacksonXmlProperty(localName = "name")
  @XmlElement(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GdalFieldDto type(GdalFieldTypeDto type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   *
   * @return type
   */
  @NotNull
  @Valid
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  @JacksonXmlProperty(localName = "type")
  @XmlElement(name = "type")
  public GdalFieldTypeDto getType() {
    return type;
  }

  public void setType(GdalFieldTypeDto type) {
    this.type = type;
  }

  public GdalFieldDto subType(GdalFieldSubTypeDto subType) {
    this.subType = subType;
    return this;
  }

  /**
   * Get subType
   *
   * @return subType
   */
  @Valid
  @Schema(name = "subType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subType")
  @JacksonXmlProperty(localName = "subType")
  @XmlElement(name = "subType")
  public GdalFieldSubTypeDto getSubType() {
    return subType;
  }

  public void setSubType(GdalFieldSubTypeDto subType) {
    this.subType = subType;
  }

  public GdalFieldDto width(Integer width) {
    this.width = width;
    return this;
  }

  /**
   * Get width
   *
   * @return width
   */
  @Schema(name = "width", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("width")
  @JacksonXmlProperty(localName = "width")
  @XmlElement(name = "width")
  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public GdalFieldDto precision(Integer precision) {
    this.precision = precision;
    return this;
  }

  /**
   * Get precision
   *
   * @return precision
   */
  @Schema(name = "precision", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("precision")
  @JacksonXmlProperty(localName = "precision")
  @XmlElement(name = "precision")
  public Integer getPrecision() {
    return precision;
  }

  public void setPrecision(Integer precision) {
    this.precision = precision;
  }

  public GdalFieldDto nullable(Boolean nullable) {
    this.nullable = nullable;
    return this;
  }

  /**
   * Get nullable
   *
   * @return nullable
   */
  @NotNull
  @Schema(name = "nullable", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("nullable")
  @JacksonXmlProperty(localName = "nullable")
  @XmlElement(name = "nullable")
  public Boolean getNullable() {
    return nullable;
  }

  public void setNullable(Boolean nullable) {
    this.nullable = nullable;
  }

  public GdalFieldDto uniqueConstraint(Boolean uniqueConstraint) {
    this.uniqueConstraint = uniqueConstraint;
    return this;
  }

  /**
   * Get uniqueConstraint
   *
   * @return uniqueConstraint
   */
  @NotNull
  @Schema(name = "uniqueConstraint", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uniqueConstraint")
  @JacksonXmlProperty(localName = "uniqueConstraint")
  @XmlElement(name = "uniqueConstraint")
  public Boolean getUniqueConstraint() {
    return uniqueConstraint;
  }

  public void setUniqueConstraint(Boolean uniqueConstraint) {
    this.uniqueConstraint = uniqueConstraint;
  }

  public GdalFieldDto defaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  /**
   * Get defaultValue
   *
   * @return defaultValue
   */
  @Schema(name = "defaultValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defaultValue")
  @JacksonXmlProperty(localName = "defaultValue")
  @XmlElement(name = "defaultValue")
  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public GdalFieldDto alias(String alias) {
    this.alias = alias;
    return this;
  }

  /**
   * Get alias
   *
   * @return alias
   */
  @Schema(name = "alias", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alias")
  @JacksonXmlProperty(localName = "alias")
  @XmlElement(name = "alias")
  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public GdalFieldDto domainName(String domainName) {
    this.domainName = domainName;
    return this;
  }

  /**
   * Get domainName
   *
   * @return domainName
   */
  @Schema(name = "domainName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("domainName")
  @JacksonXmlProperty(localName = "domainName")
  @XmlElement(name = "domainName")
  public String getDomainName() {
    return domainName;
  }

  public void setDomainName(String domainName) {
    this.domainName = domainName;
  }

  public GdalFieldDto comment(String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   *
   * @return comment
   */
  @Schema(name = "comment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("comment")
  @JacksonXmlProperty(localName = "comment")
  @XmlElement(name = "comment")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public GdalFieldDto timezone(String timezone) {
    this.timezone = timezone;
    return this;
  }

  /**
   * Get timezone
   *
   * @return timezone
   */
  @Pattern(regexp = "^(localtime|(mixed timezones)|UTC|((\\+|-)[0-9][0-9]:[0-9][0-9]))$")
  @Schema(name = "timezone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timezone")
  @JacksonXmlProperty(localName = "timezone")
  @XmlElement(name = "timezone")
  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalFieldDto field = (GdalFieldDto) o;
    return Objects.equals(this.name, field.name)
        && Objects.equals(this.type, field.type)
        && Objects.equals(this.subType, field.subType)
        && Objects.equals(this.width, field.width)
        && Objects.equals(this.precision, field.precision)
        && Objects.equals(this.nullable, field.nullable)
        && Objects.equals(this.uniqueConstraint, field.uniqueConstraint)
        && Objects.equals(this.defaultValue, field.defaultValue)
        && Objects.equals(this.alias, field.alias)
        && Objects.equals(this.domainName, field.domainName)
        && Objects.equals(this.comment, field.comment)
        && Objects.equals(this.timezone, field.timezone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        type,
        subType,
        width,
        precision,
        nullable,
        uniqueConstraint,
        defaultValue,
        alias,
        domainName,
        comment,
        timezone);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalFieldDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    subType: ").append(toIndentedString(subType)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    precision: ").append(toIndentedString(precision)).append("\n");
    sb.append("    nullable: ").append(toIndentedString(nullable)).append("\n");
    sb.append("    uniqueConstraint: ").append(toIndentedString(uniqueConstraint)).append("\n");
    sb.append("    defaultValue: ").append(toIndentedString(defaultValue)).append("\n");
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
    sb.append("    domainName: ").append(toIndentedString(domainName)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
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

    private GdalFieldDto instance;

    public Builder() {
      this(new GdalFieldDto());
    }

    protected Builder(GdalFieldDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalFieldDto value) {
      this.instance.setName(value.name);
      this.instance.setType(value.type);
      this.instance.setSubType(value.subType);
      this.instance.setWidth(value.width);
      this.instance.setPrecision(value.precision);
      this.instance.setNullable(value.nullable);
      this.instance.setUniqueConstraint(value.uniqueConstraint);
      this.instance.setDefaultValue(value.defaultValue);
      this.instance.setAlias(value.alias);
      this.instance.setDomainName(value.domainName);
      this.instance.setComment(value.comment);
      this.instance.setTimezone(value.timezone);
      return this;
    }

    public Builder name(String name) {
      this.instance.name(name);
      return this;
    }

    public Builder type(GdalFieldTypeDto type) {
      this.instance.type(type);
      return this;
    }

    public Builder subType(GdalFieldSubTypeDto subType) {
      this.instance.subType(subType);
      return this;
    }

    public Builder width(Integer width) {
      this.instance.width(width);
      return this;
    }

    public Builder precision(Integer precision) {
      this.instance.precision(precision);
      return this;
    }

    public Builder nullable(Boolean nullable) {
      this.instance.nullable(nullable);
      return this;
    }

    public Builder uniqueConstraint(Boolean uniqueConstraint) {
      this.instance.uniqueConstraint(uniqueConstraint);
      return this;
    }

    public Builder defaultValue(String defaultValue) {
      this.instance.defaultValue(defaultValue);
      return this;
    }

    public Builder alias(String alias) {
      this.instance.alias(alias);
      return this;
    }

    public Builder domainName(String domainName) {
      this.instance.domainName(domainName);
      return this;
    }

    public Builder comment(String comment) {
      this.instance.comment(comment);
      return this;
    }

    public Builder timezone(String timezone) {
      this.instance.timezone(timezone);
      return this;
    }

    /**
     * returns a built GdalFieldDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalFieldDto build() {
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
