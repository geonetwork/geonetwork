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

/** OgcApiProcessesReferenceDto */
@JsonTypeName("reference")
@JacksonXmlRootElement(localName = "OgcApiProcessesReferenceDto")
@XmlRootElement(name = "OgcApiProcessesReferenceDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesReferenceDto implements OgcApiProcessesSchemaDto {

  private String $ref;

  public OgcApiProcessesReferenceDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiProcessesReferenceDto(String $ref) {
    this.$ref = $ref;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesReferenceDto $ref(String $ref) {
    this.$ref = $ref;
    return this;
  }

  /**
   * Get $ref
   *
   * @return $ref
   */
  @NotNull
  @Schema(name = "$ref", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("$ref")
  @JacksonXmlProperty(localName = "$ref")
  @XmlElement(name = "$ref")
  public String get$Ref() {
    return $ref;
  }

  public void set$Ref(String $ref) {
    this.$ref = $ref;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesReferenceDto reference = (OgcApiProcessesReferenceDto) o;
    return Objects.equals(this.$ref, reference.$ref);
  }

  @Override
  public int hashCode() {
    return Objects.hash($ref);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesReferenceDto {\n"
            + "    $ref: "
            + toIndentedString($ref)
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

    private OgcApiProcessesReferenceDto instance;

    public Builder() {
      this(new OgcApiProcessesReferenceDto());
    }

    protected Builder(OgcApiProcessesReferenceDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesReferenceDto value) {
      this.instance.set$Ref(value.$ref);
      return this;
    }

    public Builder $ref(String $ref) {
      this.instance.$ref($ref);
      return this;
    }

    /**
     * returns a built OgcApiProcessesReferenceDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesReferenceDto build() {
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
