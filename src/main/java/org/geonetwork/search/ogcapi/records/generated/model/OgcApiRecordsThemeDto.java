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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** OgcApiRecordsThemeDto */
@JsonTypeName("theme")
@JacksonXmlRootElement(localName = "OgcApiRecordsThemeDto")
@XmlRootElement(name = "OgcApiRecordsThemeDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiRecordsThemeDto {

  @Valid private List<@Valid OgcApiRecordsThemeConceptsInnerDto> concepts = new ArrayList<>();

  private String scheme;

  public OgcApiRecordsThemeDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiRecordsThemeDto(
      List<@Valid OgcApiRecordsThemeConceptsInnerDto> concepts, String scheme) {
    this.concepts = concepts;
    this.scheme = scheme;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiRecordsThemeDto concepts(List<@Valid OgcApiRecordsThemeConceptsInnerDto> concepts) {
    this.concepts = concepts;
    return this;
  }

  public OgcApiRecordsThemeDto addConceptsItem(OgcApiRecordsThemeConceptsInnerDto conceptsItem) {
    if (this.concepts == null) {
      this.concepts = new ArrayList<>();
    }
    this.concepts.add(conceptsItem);
    return this;
  }

  /**
   * One or more entity/concept identifiers from this knowledge system. it is recommended that a
   * resolvable URI be used for each entity/concept identifier.
   *
   * @return concepts
   */
  @NotNull
  @Valid
  @Size(min = 1)
  @Schema(
      name = "concepts",
      description =
          "One or more entity/concept identifiers from this knowledge system. it is recommended"
              + " that a resolvable URI be used for each entity/concept identifier.",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("concepts")
  @JacksonXmlProperty(localName = "concepts")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "concepts")
  public List<@Valid OgcApiRecordsThemeConceptsInnerDto> getConcepts() {
    return concepts;
  }

  public void setConcepts(List<@Valid OgcApiRecordsThemeConceptsInnerDto> concepts) {
    this.concepts = concepts;
  }

  public OgcApiRecordsThemeDto scheme(String scheme) {
    this.scheme = scheme;
    return this;
  }

  /**
   * An identifier for the knowledge organization system used to classify the resource. It is
   * recommended that the identifier be a resolvable URI. The list of schemes used in a searchable
   * catalog can be determined by inspecting the server's OpenAPI document or, if the server
   * implements CQL2, by exposing a queryable (e.g. named `scheme`) and enumerating the list of
   * schemes in the queryable's schema definition.
   *
   * @return scheme
   */
  @NotNull
  @Schema(
      name = "scheme",
      description =
          "An identifier for the knowledge organization system used to classify the resource.  It"
              + " is recommended that the identifier be a resolvable URI.  The list of schemes used"
              + " in a searchable catalog can be determined by inspecting the server's OpenAPI"
              + " document or, if the server implements CQL2, by exposing a queryable (e.g. named"
              + " `scheme`) and enumerating the list of schemes in the queryable's schema"
              + " definition.",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("scheme")
  @JacksonXmlProperty(localName = "scheme")
  @XmlElement(name = "scheme")
  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiRecordsThemeDto theme = (OgcApiRecordsThemeDto) o;
    return Objects.equals(this.concepts, theme.concepts)
        && Objects.equals(this.scheme, theme.scheme);
  }

  @Override
  public int hashCode() {
    return Objects.hash(concepts, scheme);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiRecordsThemeDto {\n"
            + "    concepts: "
            + toIndentedString(concepts)
            + "\n"
            + "    scheme: "
            + toIndentedString(scheme)
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

    private OgcApiRecordsThemeDto instance;

    public Builder() {
      this(new OgcApiRecordsThemeDto());
    }

    protected Builder(OgcApiRecordsThemeDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiRecordsThemeDto value) {
      this.instance.setConcepts(value.concepts);
      this.instance.setScheme(value.scheme);
      return this;
    }

    public Builder concepts(List<@Valid OgcApiRecordsThemeConceptsInnerDto> concepts) {
      this.instance.concepts(concepts);
      return this;
    }

    public Builder scheme(String scheme) {
      this.instance.scheme(scheme);
      return this;
    }

    /**
     * returns a built OgcApiRecordsThemeDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiRecordsThemeDto build() {
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
