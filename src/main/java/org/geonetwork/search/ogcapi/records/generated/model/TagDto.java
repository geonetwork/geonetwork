/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.search.ogcapi.records.generated.model;

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
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/** TagDto */
@JsonTypeName("Tag")
@JacksonXmlRootElement(localName = "TagDto")
@XmlRootElement(name = "TagDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]",
    comments = "Generator version: 7.6.0")
public class TagDto {

  private String name;

  private String description;

  private ExternalDocumentationDto externalDocs;

  public TagDto() {
    super();
  }

  /** Constructor with only required parameters */
  public TagDto(String name) {
    this.name = name;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public TagDto name(String name) {
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
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TagDto description(String description) {
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
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TagDto externalDocs(ExternalDocumentationDto externalDocs) {
    this.externalDocs = externalDocs;
    return this;
  }

  /**
   * Get externalDocs
   *
   * @return externalDocs
   */
  @Valid
  @Schema(name = "externalDocs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("externalDocs")
  @JacksonXmlProperty(localName = "externalDocs")
  public ExternalDocumentationDto getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(ExternalDocumentationDto externalDocs) {
    this.externalDocs = externalDocs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TagDto tag = (TagDto) o;
    return Objects.equals(this.name, tag.name)
        && Objects.equals(this.description, tag.description)
        && Objects.equals(this.externalDocs, tag.externalDocs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, externalDocs);
  }

  @Override
  public String toString() {
    String sb =
        "class TagDto {\n"
            + "    name: "
            + toIndentedString(name)
            + "\n"
            + "    description: "
            + toIndentedString(description)
            + "\n"
            + "    externalDocs: "
            + toIndentedString(externalDocs)
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

    private TagDto instance;

    public Builder() {
      this(new TagDto());
    }

    protected Builder(TagDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(TagDto value) {
      this.instance.setName(value.name);
      this.instance.setDescription(value.description);
      this.instance.setExternalDocs(value.externalDocs);
      return this;
    }

    public Builder name(String name) {
      this.instance.name(name);
      return this;
    }

    public Builder description(String description) {
      this.instance.description(description);
      return this;
    }

    public Builder externalDocs(ExternalDocumentationDto externalDocs) {
      this.instance.externalDocs(externalDocs);
      return this;
    }

    /**
     * returns a built TagDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public TagDto build() {
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
