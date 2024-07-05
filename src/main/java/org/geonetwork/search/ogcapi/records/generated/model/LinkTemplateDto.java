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
import java.time.OffsetDateTime;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

/** LinkTemplateDto */
@JsonTypeName("linkTemplate")
@JacksonXmlRootElement(localName = "LinkTemplateDto")
@XmlRootElement(name = "LinkTemplateDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]",
    comments = "Generator version: 7.6.0")
public class LinkTemplateDto {

  private String rel;

  private String type;

  private String hreflang;

  private String title;

  private Integer length;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime created;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updated;

  private String uriTemplate;

  private String varBase;

  private Object variables;

  public LinkTemplateDto() {
    super();
  }

  /** Constructor with only required parameters */
  public LinkTemplateDto(String uriTemplate) {
    this.uriTemplate = uriTemplate;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public LinkTemplateDto rel(String rel) {
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

  public LinkTemplateDto type(String type) {
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

  public LinkTemplateDto hreflang(String hreflang) {
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

  public LinkTemplateDto title(String title) {
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

  public LinkTemplateDto length(Integer length) {
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

  public LinkTemplateDto created(OffsetDateTime created) {
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

  public LinkTemplateDto updated(OffsetDateTime updated) {
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

  public LinkTemplateDto uriTemplate(String uriTemplate) {
    this.uriTemplate = uriTemplate;
    return this;
  }

  /**
   * Supplies a resolvable URI to a remote resource (or resource fragment).
   *
   * @return uriTemplate
   */
  @NotNull
  @Schema(
      name = "uriTemplate",
      example = "http://data.example.com/buildings/(building-id}",
      description = "Supplies a resolvable URI to a remote resource (or resource fragment).",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uriTemplate")
  @JacksonXmlProperty(localName = "uriTemplate")
  public String getUriTemplate() {
    return uriTemplate;
  }

  public void setUriTemplate(String uriTemplate) {
    this.uriTemplate = uriTemplate;
  }

  public LinkTemplateDto varBase(String varBase) {
    this.varBase = varBase;
    return this;
  }

  /**
   * The base URI to which the variable name can be appended to retrieve the definition of the
   * variable as a JSON Schema fragment.
   *
   * @return varBase
   */
  @Schema(
      name = "varBase",
      description =
          "The base URI to which the variable name can be appended to retrieve the definition of"
              + " the variable as a JSON Schema fragment.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("varBase")
  @JacksonXmlProperty(localName = "varBase")
  public String getVarBase() {
    return varBase;
  }

  public void setVarBase(String varBase) {
    this.varBase = varBase;
  }

  public LinkTemplateDto variables(Object variables) {
    this.variables = variables;
    return this;
  }

  /**
   * This object contains one key per substitution variable in the templated URL. Each key defines
   * the schema of one substitution variable using a JSON Schema fragment and can thus include
   * things like the data type of the variable, enumerations, minimum values, maximum values, etc.
   *
   * @return variables
   */
  @Schema(
      name = "variables",
      description =
          "This object contains one key per substitution variable in the templated URL.  Each key"
              + " defines the schema of one substitution variable using a JSON Schema fragment and"
              + " can thus include things like the data type of the variable, enumerations, minimum"
              + " values, maximum values, etc.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("variables")
  @JacksonXmlProperty(localName = "variables")
  public Object getVariables() {
    return variables;
  }

  public void setVariables(Object variables) {
    this.variables = variables;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkTemplateDto linkTemplate = (LinkTemplateDto) o;
    return Objects.equals(this.rel, linkTemplate.rel)
        && Objects.equals(this.type, linkTemplate.type)
        && Objects.equals(this.hreflang, linkTemplate.hreflang)
        && Objects.equals(this.title, linkTemplate.title)
        && Objects.equals(this.length, linkTemplate.length)
        && Objects.equals(this.created, linkTemplate.created)
        && Objects.equals(this.updated, linkTemplate.updated)
        && Objects.equals(this.uriTemplate, linkTemplate.uriTemplate)
        && Objects.equals(this.varBase, linkTemplate.varBase)
        && Objects.equals(this.variables, linkTemplate.variables);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        rel, type, hreflang, title, length, created, updated, uriTemplate, varBase, variables);
  }

  @Override
  public String toString() {
    String sb =
        "class LinkTemplateDto {\n"
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
            + "    uriTemplate: "
            + toIndentedString(uriTemplate)
            + "\n"
            + "    varBase: "
            + toIndentedString(varBase)
            + "\n"
            + "    variables: "
            + toIndentedString(variables)
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

    private LinkTemplateDto instance;

    public Builder() {
      this(new LinkTemplateDto());
    }

    protected Builder(LinkTemplateDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(LinkTemplateDto value) {
      this.instance.setRel(value.rel);
      this.instance.setType(value.type);
      this.instance.setHreflang(value.hreflang);
      this.instance.setTitle(value.title);
      this.instance.setLength(value.length);
      this.instance.setCreated(value.created);
      this.instance.setUpdated(value.updated);
      this.instance.setUriTemplate(value.uriTemplate);
      this.instance.setVarBase(value.varBase);
      this.instance.setVariables(value.variables);
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

    public Builder uriTemplate(String uriTemplate) {
      this.instance.uriTemplate(uriTemplate);
      return this;
    }

    public Builder varBase(String varBase) {
      this.instance.varBase(varBase);
      return this;
    }

    public Builder variables(Object variables) {
      this.instance.variables(variables);
      return this;
    }

    /**
     * returns a built LinkTemplateDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public LinkTemplateDto build() {
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
