/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.tasks.ogcapi.processes.generated.model;

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

/** OgcApiProcessesProcessSummaryDto */
@JsonTypeName("processSummary")
@JacksonXmlRootElement(localName = "OgcApiProcessesProcessSummaryDto")
@XmlRootElement(name = "OgcApiProcessesProcessSummaryDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesProcessSummaryDto {

  private String title;

  private String description;

  @Valid private List<String> keywords = new ArrayList<>();

  @Valid private List<@Valid OgcApiProcessesMetadataDto> metadata = new ArrayList<>();

  private OgcApiProcessesDescriptionTypeAdditionalParametersDto additionalParameters;

  private String id;

  private String version;

  @Valid private List<OgcApiProcessesJobControlOptionsDto> jobControlOptions = new ArrayList<>();

  @Valid private List<OgcApiProcessesTransmissionModeDto> outputTransmission = new ArrayList<>();

  @Valid private List<@Valid OgcApiProcessesLinkDto> links = new ArrayList<>();

  public OgcApiProcessesProcessSummaryDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiProcessesProcessSummaryDto(String id, String version) {
    this.id = id;
    this.version = version;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesProcessSummaryDto title(String title) {
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

  public OgcApiProcessesProcessSummaryDto description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   *
   * @return description
   */
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  @JacksonXmlProperty(localName = "description")
  @XmlElement(name = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public OgcApiProcessesProcessSummaryDto keywords(List<String> keywords) {
    this.keywords = keywords;
    return this;
  }

  public OgcApiProcessesProcessSummaryDto addKeywordsItem(String keywordsItem) {
    if (this.keywords == null) {
      this.keywords = new ArrayList<>();
    }
    this.keywords.add(keywordsItem);
    return this;
  }

  /**
   * Get keywords
   *
   * @return keywords
   */
  @Schema(name = "keywords", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("keywords")
  @JacksonXmlProperty(localName = "keywords")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "keywords")
  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public OgcApiProcessesProcessSummaryDto metadata(
      List<@Valid OgcApiProcessesMetadataDto> metadata) {
    this.metadata = metadata;
    return this;
  }

  public OgcApiProcessesProcessSummaryDto addMetadataItem(OgcApiProcessesMetadataDto metadataItem) {
    if (this.metadata == null) {
      this.metadata = new ArrayList<>();
    }
    this.metadata.add(metadataItem);
    return this;
  }

  /**
   * Get metadata
   *
   * @return metadata
   */
  @Valid
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  @JacksonXmlProperty(localName = "metadata")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "metadata")
  public List<@Valid OgcApiProcessesMetadataDto> getMetadata() {
    return metadata;
  }

  public void setMetadata(List<@Valid OgcApiProcessesMetadataDto> metadata) {
    this.metadata = metadata;
  }

  public OgcApiProcessesProcessSummaryDto additionalParameters(
      OgcApiProcessesDescriptionTypeAdditionalParametersDto additionalParameters) {
    this.additionalParameters = additionalParameters;
    return this;
  }

  /**
   * Get additionalParameters
   *
   * @return additionalParameters
   */
  @Valid
  @Schema(name = "additionalParameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("additionalParameters")
  @JacksonXmlProperty(localName = "additionalParameters")
  @XmlElement(name = "additionalParameters")
  public OgcApiProcessesDescriptionTypeAdditionalParametersDto getAdditionalParameters() {
    return additionalParameters;
  }

  public void setAdditionalParameters(
      OgcApiProcessesDescriptionTypeAdditionalParametersDto additionalParameters) {
    this.additionalParameters = additionalParameters;
  }

  public OgcApiProcessesProcessSummaryDto id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   *
   * @return id
   */
  @NotNull
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  @JacksonXmlProperty(localName = "id")
  @XmlElement(name = "id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public OgcApiProcessesProcessSummaryDto version(String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   *
   * @return version
   */
  @NotNull
  @Schema(name = "version", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("version")
  @JacksonXmlProperty(localName = "version")
  @XmlElement(name = "version")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public OgcApiProcessesProcessSummaryDto jobControlOptions(
      List<OgcApiProcessesJobControlOptionsDto> jobControlOptions) {
    this.jobControlOptions = jobControlOptions;
    return this;
  }

  public OgcApiProcessesProcessSummaryDto addJobControlOptionsItem(
      OgcApiProcessesJobControlOptionsDto jobControlOptionsItem) {
    if (this.jobControlOptions == null) {
      this.jobControlOptions = new ArrayList<>();
    }
    this.jobControlOptions.add(jobControlOptionsItem);
    return this;
  }

  /**
   * Get jobControlOptions
   *
   * @return jobControlOptions
   */
  @Valid
  @Schema(name = "jobControlOptions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("jobControlOptions")
  @JacksonXmlProperty(localName = "jobControlOptions")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "jobControlOptions")
  public List<OgcApiProcessesJobControlOptionsDto> getJobControlOptions() {
    return jobControlOptions;
  }

  public void setJobControlOptions(List<OgcApiProcessesJobControlOptionsDto> jobControlOptions) {
    this.jobControlOptions = jobControlOptions;
  }

  public OgcApiProcessesProcessSummaryDto outputTransmission(
      List<OgcApiProcessesTransmissionModeDto> outputTransmission) {
    this.outputTransmission = outputTransmission;
    return this;
  }

  public OgcApiProcessesProcessSummaryDto addOutputTransmissionItem(
      OgcApiProcessesTransmissionModeDto outputTransmissionItem) {
    if (this.outputTransmission == null) {
      this.outputTransmission = new ArrayList<>();
    }
    this.outputTransmission.add(outputTransmissionItem);
    return this;
  }

  /**
   * Get outputTransmission
   *
   * @return outputTransmission
   */
  @Valid
  @Schema(name = "outputTransmission", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outputTransmission")
  @JacksonXmlProperty(localName = "outputTransmission")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "outputTransmission")
  public List<OgcApiProcessesTransmissionModeDto> getOutputTransmission() {
    return outputTransmission;
  }

  public void setOutputTransmission(List<OgcApiProcessesTransmissionModeDto> outputTransmission) {
    this.outputTransmission = outputTransmission;
  }

  public OgcApiProcessesProcessSummaryDto links(List<@Valid OgcApiProcessesLinkDto> links) {
    this.links = links;
    return this;
  }

  public OgcApiProcessesProcessSummaryDto addLinksItem(OgcApiProcessesLinkDto linksItem) {
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
  @Valid
  @Schema(name = "links", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("links")
  @JacksonXmlProperty(localName = "links")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "links")
  public List<@Valid OgcApiProcessesLinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<@Valid OgcApiProcessesLinkDto> links) {
    this.links = links;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesProcessSummaryDto processSummary = (OgcApiProcessesProcessSummaryDto) o;
    return Objects.equals(this.title, processSummary.title)
        && Objects.equals(this.description, processSummary.description)
        && Objects.equals(this.keywords, processSummary.keywords)
        && Objects.equals(this.metadata, processSummary.metadata)
        && Objects.equals(this.additionalParameters, processSummary.additionalParameters)
        && Objects.equals(this.id, processSummary.id)
        && Objects.equals(this.version, processSummary.version)
        && Objects.equals(this.jobControlOptions, processSummary.jobControlOptions)
        && Objects.equals(this.outputTransmission, processSummary.outputTransmission)
        && Objects.equals(this.links, processSummary.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        title,
        description,
        keywords,
        metadata,
        additionalParameters,
        id,
        version,
        jobControlOptions,
        outputTransmission,
        links);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesProcessSummaryDto {\n"
            + "    title: "
            + toIndentedString(title)
            + "\n"
            + "    description: "
            + toIndentedString(description)
            + "\n"
            + "    keywords: "
            + toIndentedString(keywords)
            + "\n"
            + "    metadata: "
            + toIndentedString(metadata)
            + "\n"
            + "    additionalParameters: "
            + toIndentedString(additionalParameters)
            + "\n"
            + "    id: "
            + toIndentedString(id)
            + "\n"
            + "    version: "
            + toIndentedString(version)
            + "\n"
            + "    jobControlOptions: "
            + toIndentedString(jobControlOptions)
            + "\n"
            + "    outputTransmission: "
            + toIndentedString(outputTransmission)
            + "\n"
            + "    links: "
            + toIndentedString(links)
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

    private OgcApiProcessesProcessSummaryDto instance;

    public Builder() {
      this(new OgcApiProcessesProcessSummaryDto());
    }

    protected Builder(OgcApiProcessesProcessSummaryDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesProcessSummaryDto value) {
      this.instance.setTitle(value.title);
      this.instance.setDescription(value.description);
      this.instance.setKeywords(value.keywords);
      this.instance.setMetadata(value.metadata);
      this.instance.setAdditionalParameters(value.additionalParameters);
      this.instance.setId(value.id);
      this.instance.setVersion(value.version);
      this.instance.setJobControlOptions(value.jobControlOptions);
      this.instance.setOutputTransmission(value.outputTransmission);
      this.instance.setLinks(value.links);
      return this;
    }

    public Builder title(String title) {
      this.instance.title(title);
      return this;
    }

    public Builder description(String description) {
      this.instance.description(description);
      return this;
    }

    public Builder keywords(List<String> keywords) {
      this.instance.keywords(keywords);
      return this;
    }

    public Builder metadata(List<@Valid OgcApiProcessesMetadataDto> metadata) {
      this.instance.metadata(metadata);
      return this;
    }

    public Builder additionalParameters(
        OgcApiProcessesDescriptionTypeAdditionalParametersDto additionalParameters) {
      this.instance.additionalParameters(additionalParameters);
      return this;
    }

    public Builder id(String id) {
      this.instance.id(id);
      return this;
    }

    public Builder version(String version) {
      this.instance.version(version);
      return this;
    }

    public Builder jobControlOptions(List<OgcApiProcessesJobControlOptionsDto> jobControlOptions) {
      this.instance.jobControlOptions(jobControlOptions);
      return this;
    }

    public Builder outputTransmission(List<OgcApiProcessesTransmissionModeDto> outputTransmission) {
      this.instance.outputTransmission(outputTransmission);
      return this;
    }

    public Builder links(List<@Valid OgcApiProcessesLinkDto> links) {
      this.instance.links(links);
      return this;
    }

    /**
     * returns a built OgcApiProcessesProcessSummaryDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesProcessSummaryDto build() {
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
