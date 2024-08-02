/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

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

/** OgcApiRecordsCollectionsDto */
@JsonTypeName("collections")
@JacksonXmlRootElement(localName = "OgcApiRecordsCollectionsDto")
@XmlRootElement(name = "OgcApiRecordsCollectionsDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiRecordsCollectionsDto {

  @Valid private List<@Valid OgcApiRecordsLinkDto> links = new ArrayList<>();

  @Valid private List<@Valid OgcApiRecordsCollectionDto> collections = new ArrayList<>();

  public OgcApiRecordsCollectionsDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiRecordsCollectionsDto(
      List<@Valid OgcApiRecordsLinkDto> links,
      List<@Valid OgcApiRecordsCollectionDto> collections) {
    this.links = links;
    this.collections = collections;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiRecordsCollectionsDto links(List<@Valid OgcApiRecordsLinkDto> links) {
    this.links = links;
    return this;
  }

  public OgcApiRecordsCollectionsDto addLinksItem(OgcApiRecordsLinkDto linksItem) {
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
  public List<@Valid OgcApiRecordsLinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<@Valid OgcApiRecordsLinkDto> links) {
    this.links = links;
  }

  public OgcApiRecordsCollectionsDto collections(
      List<@Valid OgcApiRecordsCollectionDto> collections) {
    this.collections = collections;
    return this;
  }

  public OgcApiRecordsCollectionsDto addCollectionsItem(
      OgcApiRecordsCollectionDto collectionsItem) {
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
  @Valid
  @Schema(name = "collections", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("collections")
  @JacksonXmlProperty(localName = "collections")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "collections")
  public List<@Valid OgcApiRecordsCollectionDto> getCollections() {
    return collections;
  }

  public void setCollections(List<@Valid OgcApiRecordsCollectionDto> collections) {
    this.collections = collections;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiRecordsCollectionsDto collections = (OgcApiRecordsCollectionsDto) o;
    return Objects.equals(this.links, collections.links)
        && Objects.equals(this.collections, collections.collections);
  }

  @Override
  public int hashCode() {
    return Objects.hash(links, collections);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiRecordsCollectionsDto {\n"
            + "    links: "
            + toIndentedString(links)
            + "\n"
            + "    collections: "
            + toIndentedString(collections)
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

    private OgcApiRecordsCollectionsDto instance;

    public Builder() {
      this(new OgcApiRecordsCollectionsDto());
    }

    protected Builder(OgcApiRecordsCollectionsDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiRecordsCollectionsDto value) {
      this.instance.setLinks(value.links);
      this.instance.setCollections(value.collections);
      return this;
    }

    public Builder links(List<@Valid OgcApiRecordsLinkDto> links) {
      this.instance.links(links);
      return this;
    }

    public Builder collections(List<@Valid OgcApiRecordsCollectionDto> collections) {
      this.instance.collections(collections);
      return this;
    }

    /**
     * returns a built OgcApiRecordsCollectionsDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiRecordsCollectionsDto build() {
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
