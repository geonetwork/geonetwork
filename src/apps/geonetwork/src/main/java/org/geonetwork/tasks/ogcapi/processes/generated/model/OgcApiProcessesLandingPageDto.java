/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.tasks.ogcapi.processes.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** OgcApiProcessesLandingPageDto */
@JsonTypeName("landingPage")
@JacksonXmlRootElement(localName = "OgcApiProcessesLandingPageDto")
@XmlRootElement(name = "OgcApiProcessesLandingPageDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesLandingPageDto {

  private String title;

  private String description;

  @Valid private List<@Valid OgcApiProcessesLinkDto> links = new ArrayList<>();

  public OgcApiProcessesLandingPageDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiProcessesLandingPageDto(List<@Valid OgcApiProcessesLinkDto> links) {
    this.links = links;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesLandingPageDto title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   *
   * @return title
   */
  @Schema(
      name = "title",
      example = "Example processing server",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  @JacksonXmlProperty(localName = "title")
  @XmlElement(name = "title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public OgcApiProcessesLandingPageDto description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   *
   * @return description
   */
  @Schema(
      name = "description",
      example = "Example server implementing the OGC API - Processes 1.0 Standard",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  @JacksonXmlProperty(localName = "description")
  @XmlElement(name = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public OgcApiProcessesLandingPageDto links(List<@Valid OgcApiProcessesLinkDto> links) {
    this.links = links;
    return this;
  }

  public OgcApiProcessesLandingPageDto addLinksItem(OgcApiProcessesLinkDto linksItem) {
    if (this.links == null) {
      this.links = new ArrayList<>();
    }
    this.links.add(linksItem);
    return this;
  }

  /**
   * Get links
   *
   * @return links
   */
  @NotNull
  @Valid
  @Schema(name = "links", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("links")
  @JacksonXmlProperty(localName = "links")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "links")
  public List<@Valid OgcApiProcessesLinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<@Valid OgcApiProcessesLinkDto> links) {
    this.links = links;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesLandingPageDto landingPage = (OgcApiProcessesLandingPageDto) o;
    return Objects.equals(this.title, landingPage.title)
        && Objects.equals(this.description, landingPage.description)
        && Objects.equals(this.links, landingPage.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, links);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesLandingPageDto {\n"
            + "    title: "
            + toIndentedString(title)
            + "\n"
            + "    description: "
            + toIndentedString(description)
            + "\n"
            + "    links: "
            + toIndentedString(links)
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

    private OgcApiProcessesLandingPageDto instance;

    public Builder() {
      this(new OgcApiProcessesLandingPageDto());
    }

    protected Builder(OgcApiProcessesLandingPageDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesLandingPageDto value) {
      this.instance.setTitle(value.title);
      this.instance.setDescription(value.description);
      this.instance.setLinks(value.links);
      return this;
    }

    public Builder title(String title) {
      this.instance.title(title);
      return this;
    }

    public Builder description(String description) {
      this.instance.description(description);
      return this;
    }

    public Builder links(List<@Valid OgcApiProcessesLinkDto> links) {
      this.instance.links(links);
      return this;
    }

    /**
     * returns a built OgcApiProcessesLandingPageDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesLandingPageDto build() {
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
