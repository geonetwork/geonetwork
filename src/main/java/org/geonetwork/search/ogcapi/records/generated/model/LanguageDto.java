/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/** The language used for textual values in this record. */
@Schema(name = "language", description = "The language used for textual values in this record.")
@JsonTypeName("language")
@JacksonXmlRootElement(localName = "LanguageDto")
@XmlRootElement(name = "LanguageDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]",
    comments = "Generator version: 7.6.0")
public class LanguageDto {

  private String code;

  private String name;

  private String alternate;
  private DirEnum dir;

  public LanguageDto() {
    super();
  }

  /** Constructor with only required parameters */
  public LanguageDto(String code) {
    this.code = code;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public LanguageDto code(String code) {
    this.code = code;
    return this;
  }

  /**
   * The language tag as per RFC-5646.
   *
   * @return code
   */
  @NotNull
  @Schema(
      name = "code",
      example = "el",
      description = "The language tag as per RFC-5646.",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  @JacksonXmlProperty(localName = "code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public LanguageDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The untranslated name of of the language.
   *
   * @return name
   */
  @Size(min = 1)
  @Schema(
      name = "name",
      example = "Ελληνικά",
      description = "The untranslated name of of the language.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  @JacksonXmlProperty(localName = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LanguageDto alternate(String alternate) {
    this.alternate = alternate;
    return this;
  }

  /**
   * The name of the language in another well-understood language, usually English.
   *
   * @return alternate
   */
  @Schema(
      name = "alternate",
      example = "Greek",
      description =
          "The name of the language in another well-understood language, usually English.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alternate")
  @JacksonXmlProperty(localName = "alternate")
  public String getAlternate() {
    return alternate;
  }

  public void setAlternate(String alternate) {
    this.alternate = alternate;
  }

  public LanguageDto dir(DirEnum dir) {
    this.dir = dir;
    return this;
  }

  /**
   * The direction for text in this language. The default, `ltr` (left-to-right), represents the
   * most common situation. However, care should be taken to set the value of `dir` appropriately if
   * the language direction is not `ltr`. Other values supported are `rtl` (right-to-left), `ttb`
   * (top-to-bottom), and `btt` (bottom-to-top).
   *
   * @return dir
   */
  @Schema(
      name = "dir",
      description =
          "The direction for text in this language. The default, `ltr` (left-to-right), represents"
              + " the most common situation. However, care should be taken to set the value of"
              + " `dir` appropriately if the language direction is not `ltr`. Other values"
              + " supported are `rtl` (right-to-left), `ttb` (top-to-bottom), and `btt`"
              + " (bottom-to-top).",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dir")
  @JacksonXmlProperty(localName = "dir")
  public DirEnum getDir() {
    return dir;
  }

  public void setDir(DirEnum dir) {
    this.dir = dir;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LanguageDto language = (LanguageDto) o;
    return Objects.equals(this.code, language.code)
        && Objects.equals(this.name, language.name)
        && Objects.equals(this.alternate, language.alternate)
        && Objects.equals(this.dir, language.dir);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, name, alternate, dir);
  }

  @Override
  public String toString() {
    String sb =
        "class LanguageDto {\n"
            + "    code: "
            + toIndentedString(code)
            + "\n"
            + "    name: "
            + toIndentedString(name)
            + "\n"
            + "    alternate: "
            + toIndentedString(alternate)
            + "\n"
            + "    dir: "
            + toIndentedString(dir)
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

  /**
   * The direction for text in this language. The default, `ltr` (left-to-right), represents the
   * most common situation. However, care should be taken to set the value of `dir` appropriately if
   * the language direction is not `ltr`. Other values supported are `rtl` (right-to-left), `ttb`
   * (top-to-bottom), and `btt` (bottom-to-top).
   */
  public enum DirEnum {
    LTR("ltr"),

    RTL("rtl"),

    TTB("ttb"),

    BTT("btt");

    private final String value;

    DirEnum(String value) {
      this.value = value;
    }

    @JsonCreator
    public static DirEnum fromValue(String value) {
      for (DirEnum b : DirEnum.values()) {
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

    private LanguageDto instance;

    public Builder() {
      this(new LanguageDto());
    }

    protected Builder(LanguageDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(LanguageDto value) {
      this.instance.setCode(value.code);
      this.instance.setName(value.name);
      this.instance.setAlternate(value.alternate);
      this.instance.setDir(value.dir);
      return this;
    }

    public Builder code(String code) {
      this.instance.code(code);
      return this;
    }

    public Builder name(String name) {
      this.instance.name(name);
      return this;
    }

    public Builder alternate(String alternate) {
      this.instance.alternate(alternate);
      return this;
    }

    public Builder dir(DirEnum dir) {
      this.instance.dir(dir);
      return this;
    }

    /**
     * returns a built LanguageDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public LanguageDto build() {
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
