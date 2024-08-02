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

/** OgcApiRecordsConfClassesDto */
@JsonTypeName("confClasses")
@JacksonXmlRootElement(localName = "OgcApiRecordsConfClassesDto")
@XmlRootElement(name = "OgcApiRecordsConfClassesDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiRecordsConfClassesDto {

  @Valid private List<String> conformsTo = new ArrayList<>();

  public OgcApiRecordsConfClassesDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiRecordsConfClassesDto(List<String> conformsTo) {
    this.conformsTo = conformsTo;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiRecordsConfClassesDto conformsTo(List<String> conformsTo) {
    this.conformsTo = conformsTo;
    return this;
  }

  public OgcApiRecordsConfClassesDto addConformsToItem(String conformsToItem) {
    if (this.conformsTo == null) {
      this.conformsTo = new ArrayList<>();
    }
    this.conformsTo.add(conformsToItem);
    return this;
  }

  /**
   * Get conformsTo
   *
   * @return conformsTo
   */
  @NotNull
  @Schema(name = "conformsTo", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("conformsTo")
  @JacksonXmlProperty(localName = "conformsTo")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "conformsTo")
  public List<String> getConformsTo() {
    return conformsTo;
  }

  public void setConformsTo(List<String> conformsTo) {
    this.conformsTo = conformsTo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiRecordsConfClassesDto confClasses = (OgcApiRecordsConfClassesDto) o;
    return Objects.equals(this.conformsTo, confClasses.conformsTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conformsTo);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiRecordsConfClassesDto {\n"
            + "    conformsTo: "
            + toIndentedString(conformsTo)
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

    private OgcApiRecordsConfClassesDto instance;

    public Builder() {
      this(new OgcApiRecordsConfClassesDto());
    }

    protected Builder(OgcApiRecordsConfClassesDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiRecordsConfClassesDto value) {
      this.instance.setConformsTo(value.conformsTo);
      return this;
    }

    public Builder conformsTo(List<String> conformsTo) {
      this.instance.conformsTo(conformsTo);
      return this;
    }

    /**
     * returns a built OgcApiRecordsConfClassesDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiRecordsConfClassesDto build() {
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
