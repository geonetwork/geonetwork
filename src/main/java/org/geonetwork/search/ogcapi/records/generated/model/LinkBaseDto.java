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
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

/** LinkBaseDto */
@JsonTypeName("linkBase")
@JacksonXmlRootElement(localName = "LinkBaseDto")
@XmlRootElement(name = "LinkBaseDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]",
    comments = "Generator version: 7.6.0")
public class LinkBaseDto {

  private String rel;

  private String type;

  private String hreflang;

  private String title;

  private Integer length;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime created;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updated;

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public LinkBaseDto rel(String rel) {
    this.rel = rel;
    return this;
  }

  /**
   * The type or semantics of the relation.
   *
   * @return rel
   */
  @Schema(
      name = "rel",
      example = "alternate",
      description = "The type or semantics of the relation.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rel")
  @JacksonXmlProperty(localName = "rel")
  public String getRel() {
    return rel;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }

  public LinkBaseDto type(String type) {
    this.type = type;
    return this;
  }

  /**
   * A hint indicating what the media type of the result of dereferencing the link should be.
   *
   * @return type
   */
  @Schema(
      name = "type",
      example = "application/geo+json",
      description =
          "A hint indicating what the media type of the result of dereferencing the link should"
              + " be.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  @JacksonXmlProperty(localName = "type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public LinkBaseDto hreflang(String hreflang) {
    this.hreflang = hreflang;
    return this;
  }

  /**
   * A hint indicating what the language of the result of dereferencing the link should be.
   *
   * @return hreflang
   */
  @Schema(
      name = "hreflang",
      example = "en",
      description =
          "A hint indicating what the language of the result of dereferencing the link should be.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hreflang")
  @JacksonXmlProperty(localName = "hreflang")
  public String getHreflang() {
    return hreflang;
  }

  public void setHreflang(String hreflang) {
    this.hreflang = hreflang;
  }

  public LinkBaseDto title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Used to label the destination of a link such that it can be used as a human-readable
   * identifier.
   *
   * @return title
   */
  @Schema(
      name = "title",
      example = "Trierer Strasse 70, 53115 Bonn",
      description =
          "Used to label the destination of a link such that it can be used as a human-readable"
              + " identifier.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  @JacksonXmlProperty(localName = "title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LinkBaseDto length(Integer length) {
    this.length = length;
    return this;
  }

  /**
   * Get length
   *
   * @return length
   */
  @Schema(name = "length", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("length")
  @JacksonXmlProperty(localName = "length")
  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public LinkBaseDto created(OffsetDateTime created) {
    this.created = created;
    return this;
  }

  /**
   * Date of creation of the resource pointed to by the link.
   *
   * @return created
   */
  @Valid
  @Schema(
      name = "created",
      description = "Date of creation of the resource pointed to by the link.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created")
  @JacksonXmlProperty(localName = "created")
  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  public LinkBaseDto updated(OffsetDateTime updated) {
    this.updated = updated;
    return this;
  }

  /**
   * Most recent date on which the resource pointed to by the link was changed.
   *
   * @return updated
   */
  @Valid
  @Schema(
      name = "updated",
      description = "Most recent date on which the resource pointed to by the link was changed.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updated")
  @JacksonXmlProperty(localName = "updated")
  public OffsetDateTime getUpdated() {
    return updated;
  }

  public void setUpdated(OffsetDateTime updated) {
    this.updated = updated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkBaseDto linkBase = (LinkBaseDto) o;
    return Objects.equals(this.rel, linkBase.rel)
        && Objects.equals(this.type, linkBase.type)
        && Objects.equals(this.hreflang, linkBase.hreflang)
        && Objects.equals(this.title, linkBase.title)
        && Objects.equals(this.length, linkBase.length)
        && Objects.equals(this.created, linkBase.created)
        && Objects.equals(this.updated, linkBase.updated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rel, type, hreflang, title, length, created, updated);
  }

  @Override
  public String toString() {
    String sb =
        "class LinkBaseDto {\n"
            + "    rel: "
            + toIndentedString(rel)
            + "\n"
            + "    type: "
            + toIndentedString(type)
            + "\n"
            + "    hreflang: "
            + toIndentedString(hreflang)
            + "\n"
            + "    title: "
            + toIndentedString(title)
            + "\n"
            + "    length: "
            + toIndentedString(length)
            + "\n"
            + "    created: "
            + toIndentedString(created)
            + "\n"
            + "    updated: "
            + toIndentedString(updated)
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

    private LinkBaseDto instance;

    public Builder() {
      this(new LinkBaseDto());
    }

    protected Builder(LinkBaseDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(LinkBaseDto value) {
      this.instance.setRel(value.rel);
      this.instance.setType(value.type);
      this.instance.setHreflang(value.hreflang);
      this.instance.setTitle(value.title);
      this.instance.setLength(value.length);
      this.instance.setCreated(value.created);
      this.instance.setUpdated(value.updated);
      return this;
    }

    public Builder rel(String rel) {
      this.instance.rel(rel);
      return this;
    }

    public Builder type(String type) {
      this.instance.type(type);
      return this;
    }

    public Builder hreflang(String hreflang) {
      this.instance.hreflang(hreflang);
      return this;
    }

    public Builder title(String title) {
      this.instance.title(title);
      return this;
    }

    public Builder length(Integer length) {
      this.instance.length(length);
      return this;
    }

    public Builder created(OffsetDateTime created) {
      this.instance.created(created);
      return this;
    }

    public Builder updated(OffsetDateTime updated) {
      this.instance.updated(updated);
      return this;
    }

    /**
     * returns a built LinkBaseDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public LinkBaseDto build() {
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
