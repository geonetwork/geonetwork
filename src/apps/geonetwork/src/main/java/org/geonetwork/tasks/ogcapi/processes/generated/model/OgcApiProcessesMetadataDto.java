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
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/** OgcApiProcessesMetadataDto */
@JsonTypeName("metadata")
@JacksonXmlRootElement(localName = "OgcApiProcessesMetadataDto")
@XmlRootElement(name = "OgcApiProcessesMetadataDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesMetadataDto {

  private String title;

  private String role;

  private String href;

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesMetadataDto title(String title) {
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

  public OgcApiProcessesMetadataDto role(String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   *
   * @return role
   */
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  @JacksonXmlProperty(localName = "role")
  @XmlElement(name = "role")
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public OgcApiProcessesMetadataDto href(String href) {
    this.href = href;
    return this;
  }

  /**
   * Get href
   *
   * @return href
   */
  @Schema(name = "href", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("href")
  @JacksonXmlProperty(localName = "href")
  @XmlElement(name = "href")
  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesMetadataDto metadata = (OgcApiProcessesMetadataDto) o;
    return Objects.equals(this.title, metadata.title)
        && Objects.equals(this.role, metadata.role)
        && Objects.equals(this.href, metadata.href);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, role, href);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesMetadataDto {\n"
            + "    title: "
            + toIndentedString(title)
            + "\n"
            + "    role: "
            + toIndentedString(role)
            + "\n"
            + "    href: "
            + toIndentedString(href)
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

    private OgcApiProcessesMetadataDto instance;

    public Builder() {
      this(new OgcApiProcessesMetadataDto());
    }

    protected Builder(OgcApiProcessesMetadataDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesMetadataDto value) {
      this.instance.setTitle(value.title);
      this.instance.setRole(value.role);
      this.instance.setHref(value.href);
      return this;
    }

    public Builder title(String title) {
      this.instance.title(title);
      return this;
    }

    public Builder role(String role) {
      this.instance.role(role);
      return this;
    }

    public Builder href(String href) {
      this.instance.href(href);
      return this;
    }

    /**
     * returns a built OgcApiProcessesMetadataDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesMetadataDto build() {
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
