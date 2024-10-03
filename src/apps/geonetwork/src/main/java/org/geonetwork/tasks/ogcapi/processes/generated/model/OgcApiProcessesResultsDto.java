/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.tasks.ogcapi.processes.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.annotation.Generated;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** OgcApiProcessesResultsDto */
@JsonTypeName("results")
@JacksonXmlRootElement(localName = "OgcApiProcessesResultsDto")
@XmlRootElement(name = "OgcApiProcessesResultsDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesResultsDto implements OgcApiProcessesExecute200ResponseDto {
  /**
   * A container for additional, undeclared properties. This is a holder for any undeclared
   * properties as specified with the 'additionalProperties' keyword in the OAS document.
   */
  private Map<String, OgcApiProcessesInlineOrRefDataDto> additionalProperties;

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Set the additional (undeclared) property with the specified name and value. If the property
   * does not already exist, create it otherwise replace it.
   */
  @JsonAnySetter
  public OgcApiProcessesResultsDto putAdditionalProperty(
      String key, OgcApiProcessesInlineOrRefDataDto value) {
    if (this.additionalProperties == null) {
      this.additionalProperties = new HashMap<String, OgcApiProcessesInlineOrRefDataDto>();
    }
    this.additionalProperties.put(key, value);
    return this;
  }

  /** Return the additional (undeclared) property. */
  @JsonAnyGetter
  public Map<String, OgcApiProcessesInlineOrRefDataDto> getAdditionalProperties() {
    return additionalProperties;
  }

  /** Return the additional (undeclared) property with the specified name. */
  public OgcApiProcessesInlineOrRefDataDto getAdditionalProperty(String key) {
    if (this.additionalProperties == null) {
      return null;
    }
    return this.additionalProperties.get(key);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    return o != null && getClass() == o.getClass();
  }

  @Override
  public int hashCode() {
    return Objects.hash(additionalProperties);
  }

  @Override
  public String toString() {

    String sb =
        "class OgcApiProcessesResultsDto {\n"
            + "    additionalProperties: "
            + toIndentedString(additionalProperties)
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

    private OgcApiProcessesResultsDto instance;

    public Builder() {
      this(new OgcApiProcessesResultsDto());
    }

    protected Builder(OgcApiProcessesResultsDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesResultsDto value) {
      return this;
    }

    public Builder additionalProperties(
        Map<String, OgcApiProcessesInlineOrRefDataDto> additionalProperties) {
      this.instance.additionalProperties = additionalProperties;
      return this;
    }

    /**
     * returns a built OgcApiProcessesResultsDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesResultsDto build() {
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
