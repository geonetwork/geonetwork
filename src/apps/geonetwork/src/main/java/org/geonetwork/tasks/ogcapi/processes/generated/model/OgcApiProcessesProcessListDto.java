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

/** OgcApiProcessesProcessListDto */
@JsonTypeName("processList")
@JacksonXmlRootElement(localName = "OgcApiProcessesProcessListDto")
@XmlRootElement(name = "OgcApiProcessesProcessListDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesProcessListDto {

  @Valid private List<OgcApiProcessesProcessSummaryDto> processes = new ArrayList<>();

  @Valid private List<@Valid OgcApiProcessesLinkDto> links = new ArrayList<>();

  public OgcApiProcessesProcessListDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiProcessesProcessListDto(
      List<OgcApiProcessesProcessSummaryDto> processes, List<@Valid OgcApiProcessesLinkDto> links) {
    this.processes = processes;
    this.links = links;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesProcessListDto processes(List<OgcApiProcessesProcessSummaryDto> processes) {
    this.processes = processes;
    return this;
  }

  public OgcApiProcessesProcessListDto addProcessesItem(
      OgcApiProcessesProcessSummaryDto processesItem) {
    if (this.processes == null) {
      this.processes = new ArrayList<>();
    }
    this.processes.add(processesItem);
    return this;
  }

  /**
   * Get processes
   *
   * @return processes
   */
  @NotNull
  @Valid
  @Schema(name = "processes", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("processes")
  @JacksonXmlProperty(localName = "processes")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "processes")
  public List<OgcApiProcessesProcessSummaryDto> getProcesses() {
    return processes;
  }

  public void setProcesses(List<OgcApiProcessesProcessSummaryDto> processes) {
    this.processes = processes;
  }

  public OgcApiProcessesProcessListDto links(List<@Valid OgcApiProcessesLinkDto> links) {
    this.links = links;
    return this;
  }

  public OgcApiProcessesProcessListDto addLinksItem(OgcApiProcessesLinkDto linksItem) {
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
    OgcApiProcessesProcessListDto processList = (OgcApiProcessesProcessListDto) o;
    return Objects.equals(this.processes, processList.processes)
        && Objects.equals(this.links, processList.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(processes, links);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesProcessListDto {\n"
            + "    processes: "
            + toIndentedString(processes)
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

    private OgcApiProcessesProcessListDto instance;

    public Builder() {
      this(new OgcApiProcessesProcessListDto());
    }

    protected Builder(OgcApiProcessesProcessListDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesProcessListDto value) {
      this.instance.setProcesses(value.processes);
      this.instance.setLinks(value.links);
      return this;
    }

    public Builder processes(List<OgcApiProcessesProcessSummaryDto> processes) {
      this.instance.processes(processes);
      return this;
    }

    public Builder links(List<@Valid OgcApiProcessesLinkDto> links) {
      this.instance.links(links);
      return this;
    }

    /**
     * returns a built OgcApiProcessesProcessListDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesProcessListDto build() {
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
