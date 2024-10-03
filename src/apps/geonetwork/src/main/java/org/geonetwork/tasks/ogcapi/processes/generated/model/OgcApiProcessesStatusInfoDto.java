/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.tasks.ogcapi.processes.generated.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

/** OgcApiProcessesStatusInfoDto */
@JsonTypeName("statusInfo")
@JacksonXmlRootElement(localName = "OgcApiProcessesStatusInfoDto")
@XmlRootElement(name = "OgcApiProcessesStatusInfoDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesStatusInfoDto {

  private String processID;
  private TypeEnum type;
  private String jobID;
  private OgcApiProcessesStatusCodeDto status;
  private String message;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime created;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime started;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime finished;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updated;

  private Integer progress;
  @Valid private List<@Valid OgcApiProcessesLinkDto> links = new ArrayList<>();

  public OgcApiProcessesStatusInfoDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiProcessesStatusInfoDto(
      TypeEnum type, String jobID, OgcApiProcessesStatusCodeDto status) {
    this.type = type;
    this.jobID = jobID;
    this.status = status;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesStatusInfoDto processID(String processID) {
    this.processID = processID;
    return this;
  }

  /**
   * Get processID
   *
   * @return processID
   */
  @Schema(name = "processID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("processID")
  @JacksonXmlProperty(localName = "processID")
  @XmlElement(name = "processID")
  public String getProcessID() {
    return processID;
  }

  public void setProcessID(String processID) {
    this.processID = processID;
  }

  public OgcApiProcessesStatusInfoDto type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   *
   * @return type
   */
  @NotNull
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  @JacksonXmlProperty(localName = "type")
  @XmlElement(name = "type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public OgcApiProcessesStatusInfoDto jobID(String jobID) {
    this.jobID = jobID;
    return this;
  }

  /**
   * Get jobID
   *
   * @return jobID
   */
  @NotNull
  @Schema(name = "jobID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("jobID")
  @JacksonXmlProperty(localName = "jobID")
  @XmlElement(name = "jobID")
  public String getJobID() {
    return jobID;
  }

  public void setJobID(String jobID) {
    this.jobID = jobID;
  }

  public OgcApiProcessesStatusInfoDto status(OgcApiProcessesStatusCodeDto status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   *
   * @return status
   */
  @NotNull
  @Valid
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  @JacksonXmlProperty(localName = "status")
  @XmlElement(name = "status")
  public OgcApiProcessesStatusCodeDto getStatus() {
    return status;
  }

  public void setStatus(OgcApiProcessesStatusCodeDto status) {
    this.status = status;
  }

  public OgcApiProcessesStatusInfoDto message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   *
   * @return message
   */
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  @JacksonXmlProperty(localName = "message")
  @XmlElement(name = "message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public OgcApiProcessesStatusInfoDto created(OffsetDateTime created) {
    this.created = created;
    return this;
  }

  /**
   * Get created
   *
   * @return created
   */
  @Valid
  @Schema(name = "created", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created")
  @JacksonXmlProperty(localName = "created")
  @XmlElement(name = "created")
  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  public OgcApiProcessesStatusInfoDto started(OffsetDateTime started) {
    this.started = started;
    return this;
  }

  /**
   * Get started
   *
   * @return started
   */
  @Valid
  @Schema(name = "started", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("started")
  @JacksonXmlProperty(localName = "started")
  @XmlElement(name = "started")
  public OffsetDateTime getStarted() {
    return started;
  }

  public void setStarted(OffsetDateTime started) {
    this.started = started;
  }

  public OgcApiProcessesStatusInfoDto finished(OffsetDateTime finished) {
    this.finished = finished;
    return this;
  }

  /**
   * Get finished
   *
   * @return finished
   */
  @Valid
  @Schema(name = "finished", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("finished")
  @JacksonXmlProperty(localName = "finished")
  @XmlElement(name = "finished")
  public OffsetDateTime getFinished() {
    return finished;
  }

  public void setFinished(OffsetDateTime finished) {
    this.finished = finished;
  }

  public OgcApiProcessesStatusInfoDto updated(OffsetDateTime updated) {
    this.updated = updated;
    return this;
  }

  /**
   * Get updated
   *
   * @return updated
   */
  @Valid
  @Schema(name = "updated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updated")
  @JacksonXmlProperty(localName = "updated")
  @XmlElement(name = "updated")
  public OffsetDateTime getUpdated() {
    return updated;
  }

  public void setUpdated(OffsetDateTime updated) {
    this.updated = updated;
  }

  public OgcApiProcessesStatusInfoDto progress(Integer progress) {
    this.progress = progress;
    return this;
  }

  /**
   * Get progress minimum: 0 maximum: 100
   *
   * @return progress
   */
  @Min(0)
  @Max(100)
  @Schema(name = "progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress")
  @JacksonXmlProperty(localName = "progress")
  @XmlElement(name = "progress")
  public Integer getProgress() {
    return progress;
  }

  public void setProgress(Integer progress) {
    this.progress = progress;
  }

  public OgcApiProcessesStatusInfoDto links(List<@Valid OgcApiProcessesLinkDto> links) {
    this.links = links;
    return this;
  }

  public OgcApiProcessesStatusInfoDto addLinksItem(OgcApiProcessesLinkDto linksItem) {
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
    OgcApiProcessesStatusInfoDto statusInfo = (OgcApiProcessesStatusInfoDto) o;
    return Objects.equals(this.processID, statusInfo.processID)
        && Objects.equals(this.type, statusInfo.type)
        && Objects.equals(this.jobID, statusInfo.jobID)
        && Objects.equals(this.status, statusInfo.status)
        && Objects.equals(this.message, statusInfo.message)
        && Objects.equals(this.created, statusInfo.created)
        && Objects.equals(this.started, statusInfo.started)
        && Objects.equals(this.finished, statusInfo.finished)
        && Objects.equals(this.updated, statusInfo.updated)
        && Objects.equals(this.progress, statusInfo.progress)
        && Objects.equals(this.links, statusInfo.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        processID, type, jobID, status, message, created, started, finished, updated, progress,
        links);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesStatusInfoDto {\n"
            + "    processID: "
            + toIndentedString(processID)
            + "\n"
            + "    type: "
            + toIndentedString(type)
            + "\n"
            + "    jobID: "
            + toIndentedString(jobID)
            + "\n"
            + "    status: "
            + toIndentedString(status)
            + "\n"
            + "    message: "
            + toIndentedString(message)
            + "\n"
            + "    created: "
            + toIndentedString(created)
            + "\n"
            + "    started: "
            + toIndentedString(started)
            + "\n"
            + "    finished: "
            + toIndentedString(finished)
            + "\n"
            + "    updated: "
            + toIndentedString(updated)
            + "\n"
            + "    progress: "
            + toIndentedString(progress)
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

  /** Gets or Sets type */
  public enum TypeEnum {
    PROCESS("process");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class Builder {

    private OgcApiProcessesStatusInfoDto instance;

    public Builder() {
      this(new OgcApiProcessesStatusInfoDto());
    }

    protected Builder(OgcApiProcessesStatusInfoDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesStatusInfoDto value) {
      this.instance.setProcessID(value.processID);
      this.instance.setType(value.type);
      this.instance.setJobID(value.jobID);
      this.instance.setStatus(value.status);
      this.instance.setMessage(value.message);
      this.instance.setCreated(value.created);
      this.instance.setStarted(value.started);
      this.instance.setFinished(value.finished);
      this.instance.setUpdated(value.updated);
      this.instance.setProgress(value.progress);
      this.instance.setLinks(value.links);
      return this;
    }

    public Builder processID(String processID) {
      this.instance.processID(processID);
      return this;
    }

    public Builder type(TypeEnum type) {
      this.instance.type(type);
      return this;
    }

    public Builder jobID(String jobID) {
      this.instance.jobID(jobID);
      return this;
    }

    public Builder status(OgcApiProcessesStatusCodeDto status) {
      this.instance.status(status);
      return this;
    }

    public Builder message(String message) {
      this.instance.message(message);
      return this;
    }

    public Builder created(OffsetDateTime created) {
      this.instance.created(created);
      return this;
    }

    public Builder started(OffsetDateTime started) {
      this.instance.started(started);
      return this;
    }

    public Builder finished(OffsetDateTime finished) {
      this.instance.finished(finished);
      return this;
    }

    public Builder updated(OffsetDateTime updated) {
      this.instance.updated(updated);
      return this;
    }

    public Builder progress(Integer progress) {
      this.instance.progress(progress);
      return this;
    }

    public Builder links(List<@Valid OgcApiProcessesLinkDto> links) {
      this.instance.links(links);
      return this;
    }

    /**
     * returns a built OgcApiProcessesStatusInfoDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesStatusInfoDto build() {
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
