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
import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.Objects;

/**
 * Optional URIs for callbacks for this job. Support for this parameter is not required and the
 * parameter may be removed from the API definition, if conformance class **&#39;callback&#39;** is
 * not listed in the conformance declaration under &#x60;/conformance&#x60;.
 */
@Schema(
    name = "subscriber",
    description =
        "Optional URIs for callbacks for this job.  Support for this parameter is not required and"
            + " the parameter may be removed from the API definition, if conformance class"
            + " **'callback'** is not listed in the conformance declaration under `/conformance`.")
@JsonTypeName("subscriber")
@JacksonXmlRootElement(localName = "OgcApiProcessesSubscriberDto")
@XmlRootElement(name = "OgcApiProcessesSubscriberDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesSubscriberDto {

  private URI successUri;

  private URI inProgressUri;

  private URI failedUri;

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesSubscriberDto successUri(URI successUri) {
    this.successUri = successUri;
    return this;
  }

  /**
   * Get successUri
   *
   * @return successUri
   */
  @Valid
  @Schema(name = "successUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("successUri")
  @JacksonXmlProperty(localName = "successUri")
  @XmlElement(name = "successUri")
  public URI getSuccessUri() {
    return successUri;
  }

  public void setSuccessUri(URI successUri) {
    this.successUri = successUri;
  }

  public OgcApiProcessesSubscriberDto inProgressUri(URI inProgressUri) {
    this.inProgressUri = inProgressUri;
    return this;
  }

  /**
   * Get inProgressUri
   *
   * @return inProgressUri
   */
  @Valid
  @Schema(name = "inProgressUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inProgressUri")
  @JacksonXmlProperty(localName = "inProgressUri")
  @XmlElement(name = "inProgressUri")
  public URI getInProgressUri() {
    return inProgressUri;
  }

  public void setInProgressUri(URI inProgressUri) {
    this.inProgressUri = inProgressUri;
  }

  public OgcApiProcessesSubscriberDto failedUri(URI failedUri) {
    this.failedUri = failedUri;
    return this;
  }

  /**
   * Get failedUri
   *
   * @return failedUri
   */
  @Valid
  @Schema(name = "failedUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("failedUri")
  @JacksonXmlProperty(localName = "failedUri")
  @XmlElement(name = "failedUri")
  public URI getFailedUri() {
    return failedUri;
  }

  public void setFailedUri(URI failedUri) {
    this.failedUri = failedUri;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesSubscriberDto subscriber = (OgcApiProcessesSubscriberDto) o;
    return Objects.equals(this.successUri, subscriber.successUri)
        && Objects.equals(this.inProgressUri, subscriber.inProgressUri)
        && Objects.equals(this.failedUri, subscriber.failedUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(successUri, inProgressUri, failedUri);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesSubscriberDto {\n"
            + "    successUri: "
            + toIndentedString(successUri)
            + "\n"
            + "    inProgressUri: "
            + toIndentedString(inProgressUri)
            + "\n"
            + "    failedUri: "
            + toIndentedString(failedUri)
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

    private OgcApiProcessesSubscriberDto instance;

    public Builder() {
      this(new OgcApiProcessesSubscriberDto());
    }

    protected Builder(OgcApiProcessesSubscriberDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesSubscriberDto value) {
      this.instance.setSuccessUri(value.successUri);
      this.instance.setInProgressUri(value.inProgressUri);
      this.instance.setFailedUri(value.failedUri);
      return this;
    }

    public Builder successUri(URI successUri) {
      this.instance.successUri(successUri);
      return this;
    }

    public Builder inProgressUri(URI inProgressUri) {
      this.instance.inProgressUri(inProgressUri);
      return this;
    }

    public Builder failedUri(URI failedUri) {
      this.instance.failedUri(failedUri);
      return this;
    }

    /**
     * returns a built OgcApiProcessesSubscriberDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesSubscriberDto build() {
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
