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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** GetCollections200ResponseDto */
@JsonTypeName("getCollections_200_response")
@JacksonXmlRootElement(localName = "GetCollections200ResponseDto")
@XmlRootElement(name = "GetCollections200ResponseDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]",
    comments = "Generator version: 7.6.0")
public class GetCollections200ResponseDto {

  @Valid private List<@Valid LinkDto> links = new ArrayList<>();

  @Valid private List<@Valid Object> collections = new ArrayList<>();

  @Valid private List<LinkTemplateDto> linkTemplates = new ArrayList<>();

  public GetCollections200ResponseDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GetCollections200ResponseDto(List<@Valid LinkDto> links, List<@Valid Object> collections) {
    this.links = links;
    this.collections = collections;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public GetCollections200ResponseDto links(List<@Valid LinkDto> links) {
    this.links = links;
    return this;
  }

  public GetCollections200ResponseDto addLinksItem(LinkDto linksItem) {
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
  public List<@Valid LinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<@Valid LinkDto> links) {
    this.links = links;
  }

  public GetCollections200ResponseDto collections(List<@Valid Object> collections) {
    this.collections = collections;
    return this;
  }

  public GetCollections200ResponseDto addCollectionsItem(Object collectionsItem) {
    if (this.collections == null) {
      this.collections = new ArrayList<>();
    }
    this.collections.add(collectionsItem);
    return this;
  }

  /**
   * Get collections
   *
   * @return collections
   */
  @NotNull
  @Schema(name = "collections", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("collections")
  @JacksonXmlProperty(localName = "collections")
  public List<@Valid Object> getCollections() {
    return collections;
  }

  public void setCollections(List<@Valid Object> collections) {
    this.collections = collections;
  }

  public GetCollections200ResponseDto linkTemplates(List<LinkTemplateDto> linkTemplates) {
    this.linkTemplates = linkTemplates;
    return this;
  }

  public GetCollections200ResponseDto addLinkTemplatesItem(LinkTemplateDto linkTemplatesItem) {
    if (this.linkTemplates == null) {
      this.linkTemplates = new ArrayList<>();
    }
    this.linkTemplates.add(linkTemplatesItem);
    return this;
  }

  /**
   * Get linkTemplates
   *
   * @return linkTemplates
   */
  @Valid
  @Schema(name = "linkTemplates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("linkTemplates")
  @JacksonXmlProperty(localName = "linkTemplates")
  public List<LinkTemplateDto> getLinkTemplates() {
    return linkTemplates;
  }

  public void setLinkTemplates(List<LinkTemplateDto> linkTemplates) {
    this.linkTemplates = linkTemplates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCollections200ResponseDto getCollections200Response = (GetCollections200ResponseDto) o;
    return Objects.equals(this.links, getCollections200Response.links)
        && Objects.equals(this.collections, getCollections200Response.collections)
        && Objects.equals(this.linkTemplates, getCollections200Response.linkTemplates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(links, collections, linkTemplates);
  }

  @Override
  public String toString() {
    String sb =
        "class GetCollections200ResponseDto {\n"
            + "    links: "
            + toIndentedString(links)
            + "\n"
            + "    collections: "
            + toIndentedString(collections)
            + "\n"
            + "    linkTemplates: "
            + toIndentedString(linkTemplates)
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

    private GetCollections200ResponseDto instance;

    public Builder() {
      this(new GetCollections200ResponseDto());
    }

    protected Builder(GetCollections200ResponseDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GetCollections200ResponseDto value) {
      this.instance.setLinks(value.links);
      this.instance.setCollections(value.collections);
      this.instance.setLinkTemplates(value.linkTemplates);
      return this;
    }

    public Builder links(List<@Valid LinkDto> links) {
      this.instance.links(links);
      return this;
    }

    public Builder collections(List<@Valid Object> collections) {
      this.instance.collections(collections);
      return this;
    }

    public Builder linkTemplates(List<LinkTemplateDto> linkTemplates) {
      this.instance.linkTemplates(linkTemplates);
      return this;
    }

    /**
     * returns a built GetCollections200ResponseDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GetCollections200ResponseDto build() {
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
