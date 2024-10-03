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
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/** OgcApiProcessesLinkDto */
@JsonTypeName("link")
@JacksonXmlRootElement(localName = "OgcApiProcessesLinkDto")
@XmlRootElement(name = "OgcApiProcessesLinkDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesLinkDto implements OgcApiProcessesInlineOrRefDataDto {

  private String href;

  private String rel;

  private String type;

  private String hreflang;

  private String title;

  public OgcApiProcessesLinkDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiProcessesLinkDto(String href) {
    this.href = href;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesLinkDto href(String href) {
    this.href = href;
    return this;
  }

  /**
   * Get href
   *
   * @return href
   */
  @NotNull
  @Schema(name = "href", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("href")
  @JacksonXmlProperty(localName = "href")
  @XmlElement(name = "href")
  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public OgcApiProcessesLinkDto rel(String rel) {
    this.rel = rel;
    return this;
  }

  /**
   * Get rel
   *
   * @return rel
   */
  @Schema(name = "rel", example = "service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rel")
  @JacksonXmlProperty(localName = "rel")
  @XmlElement(name = "rel")
  public String getRel() {
    return rel;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }

  public OgcApiProcessesLinkDto type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   *
   * @return type
   */
  @Schema(
      name = "type",
      example = "application/json",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  @JacksonXmlProperty(localName = "type")
  @XmlElement(name = "type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public OgcApiProcessesLinkDto hreflang(String hreflang) {
    this.hreflang = hreflang;
    return this;
  }

  /**
   * Get hreflang
   *
   * @return hreflang
   */
  @Schema(name = "hreflang", example = "en", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hreflang")
  @JacksonXmlProperty(localName = "hreflang")
  @XmlElement(name = "hreflang")
  public String getHreflang() {
    return hreflang;
  }

  public void setHreflang(String hreflang) {
    this.hreflang = hreflang;
  }

  public OgcApiProcessesLinkDto title(String title) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesLinkDto link = (OgcApiProcessesLinkDto) o;
    return Objects.equals(this.href, link.href)
        && Objects.equals(this.rel, link.rel)
        && Objects.equals(this.type, link.type)
        && Objects.equals(this.hreflang, link.hreflang)
        && Objects.equals(this.title, link.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(href, rel, type, hreflang, title);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesLinkDto {\n"
            + "    href: "
            + toIndentedString(href)
            + "\n"
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

    private OgcApiProcessesLinkDto instance;

    public Builder() {
      this(new OgcApiProcessesLinkDto());
    }

    protected Builder(OgcApiProcessesLinkDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesLinkDto value) {
      this.instance.setHref(value.href);
      this.instance.setRel(value.rel);
      this.instance.setType(value.type);
      this.instance.setHreflang(value.hreflang);
      this.instance.setTitle(value.title);
      return this;
    }

    public Builder href(String href) {
      this.instance.href(href);
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

    /**
     * returns a built OgcApiProcessesLinkDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesLinkDto build() {
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
