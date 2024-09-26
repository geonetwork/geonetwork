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

/** OgcApiProcessesJobListDto */
@JsonTypeName("jobList")
@JacksonXmlRootElement(localName = "OgcApiProcessesJobListDto")
@XmlRootElement(name = "OgcApiProcessesJobListDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesJobListDto {

  @Valid private List<@Valid OgcApiProcessesStatusInfoDto> jobs = new ArrayList<>();

  @Valid private List<@Valid OgcApiProcessesLinkDto> links = new ArrayList<>();

  public OgcApiProcessesJobListDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiProcessesJobListDto(
      List<@Valid OgcApiProcessesStatusInfoDto> jobs, List<@Valid OgcApiProcessesLinkDto> links) {
    this.jobs = jobs;
    this.links = links;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesJobListDto jobs(List<@Valid OgcApiProcessesStatusInfoDto> jobs) {
    this.jobs = jobs;
    return this;
  }

  public OgcApiProcessesJobListDto addJobsItem(OgcApiProcessesStatusInfoDto jobsItem) {
    if (this.jobs == null) {
      this.jobs = new ArrayList<>();
    }
    this.jobs.add(jobsItem);
    return this;
  }

  /**
   * Get jobs
   *
   * @return jobs
   */
  @NotNull
  @Valid
  @Schema(name = "jobs", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("jobs")
  @JacksonXmlProperty(localName = "jobs")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "jobs")
  public List<@Valid OgcApiProcessesStatusInfoDto> getJobs() {
    return jobs;
  }

  public void setJobs(List<@Valid OgcApiProcessesStatusInfoDto> jobs) {
    this.jobs = jobs;
  }

  public OgcApiProcessesJobListDto links(List<@Valid OgcApiProcessesLinkDto> links) {
    this.links = links;
    return this;
  }

  public OgcApiProcessesJobListDto addLinksItem(OgcApiProcessesLinkDto linksItem) {
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
  @NotNull
  @Valid
  @Schema(name = "links", requiredMode = Schema.RequiredMode.REQUIRED)
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
    OgcApiProcessesJobListDto jobList = (OgcApiProcessesJobListDto) o;
    return Objects.equals(this.jobs, jobList.jobs) && Objects.equals(this.links, jobList.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobs, links);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesJobListDto {\n"
            + "    jobs: "
            + toIndentedString(jobs)
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

    private OgcApiProcessesJobListDto instance;

    public Builder() {
      this(new OgcApiProcessesJobListDto());
    }

    protected Builder(OgcApiProcessesJobListDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesJobListDto value) {
      this.instance.setJobs(value.jobs);
      this.instance.setLinks(value.links);
      return this;
    }

    public Builder jobs(List<@Valid OgcApiProcessesStatusInfoDto> jobs) {
      this.instance.jobs(jobs);
      return this;
    }

    public Builder links(List<@Valid OgcApiProcessesLinkDto> links) {
      this.instance.links(links);
      return this;
    }

    /**
     * returns a built OgcApiProcessesJobListDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesJobListDto build() {
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
