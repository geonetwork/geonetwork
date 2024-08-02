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
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/** OgcApiRecordsTagDto */
@JsonTypeName("Tag")
@JacksonXmlRootElement(localName = "OgcApiRecordsTagDto")
@XmlRootElement(name = "OgcApiRecordsTagDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiRecordsTagDto {

  private String name;

  private String description;

  private OgcApiRecordsExternalDocumentationDto externalDocs;

  public OgcApiRecordsTagDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiRecordsTagDto(String name) {
    this.name = name;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiRecordsTagDto name(String name) {
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

  public OgcApiRecordsTagDto description(String description) {
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

  public OgcApiRecordsTagDto externalDocs(OgcApiRecordsExternalDocumentationDto externalDocs) {
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
  @XmlElement(name = "externalDocs")
  public OgcApiRecordsExternalDocumentationDto getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(OgcApiRecordsExternalDocumentationDto externalDocs) {
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
    OgcApiRecordsTagDto tag = (OgcApiRecordsTagDto) o;
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
        "class OgcApiRecordsTagDto {\n"
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

    private OgcApiRecordsTagDto instance;

    public Builder() {
      this(new OgcApiRecordsTagDto());
    }

    protected Builder(OgcApiRecordsTagDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiRecordsTagDto value) {
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

    public Builder externalDocs(OgcApiRecordsExternalDocumentationDto externalDocs) {
      this.instance.externalDocs(externalDocs);
      return this;
    }

    /**
     * returns a built OgcApiRecordsTagDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiRecordsTagDto build() {
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
