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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** OgcApiRecordsTimeDto */
@JsonTypeName("time")
@JacksonXmlRootElement(localName = "OgcApiRecordsTimeDto")
@XmlRootElement(name = "OgcApiRecordsTimeDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiRecordsTimeDto implements OgcApiRecordsRecordGeoJSONTimeDto {

  private String date;

  private String timestamp;

  @Valid private List<OgcApiRecordsTimeIntervalInnerDto> interval = new ArrayList<>();

  private String resolution;

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiRecordsTimeDto date(String date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   *
   * @return date
   */
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
  @Schema(name = "date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("date")
  @JacksonXmlProperty(localName = "date")
  @XmlElement(name = "date")
  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public OgcApiRecordsTimeDto timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   *
   * @return timestamp
   */
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?Z$")
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  @JacksonXmlProperty(localName = "timestamp")
  @XmlElement(name = "timestamp")
  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public OgcApiRecordsTimeDto interval(List<OgcApiRecordsTimeIntervalInnerDto> interval) {
    this.interval = interval;
    return this;
  }

  public OgcApiRecordsTimeDto addIntervalItem(OgcApiRecordsTimeIntervalInnerDto intervalItem) {
    if (this.interval == null) {
      this.interval = new ArrayList<>();
    }
    this.interval.add(intervalItem);
    return this;
  }

  /**
   * Get interval
   *
   * @return interval
   */
  @Valid
  @Size(min = 2, max = 2)
  @Schema(name = "interval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("interval")
  @JacksonXmlProperty(localName = "interval")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "interval")
  public List<OgcApiRecordsTimeIntervalInnerDto> getInterval() {
    return interval;
  }

  public void setInterval(List<OgcApiRecordsTimeIntervalInnerDto> interval) {
    this.interval = interval;
  }

  public OgcApiRecordsTimeDto resolution(String resolution) {
    this.resolution = resolution;
    return this;
  }

  /**
   * Minimum time period resolvable in the dataset, as an ISO 8601 duration
   *
   * @return resolution
   */
  @Schema(
      name = "resolution",
      example = "[P1D]",
      description = "Minimum time period resolvable in the dataset, as an ISO 8601 duration",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolution")
  @JacksonXmlProperty(localName = "resolution")
  @XmlElement(name = "resolution")
  public String getResolution() {
    return resolution;
  }

  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiRecordsTimeDto time = (OgcApiRecordsTimeDto) o;
    return Objects.equals(this.date, time.date)
        && Objects.equals(this.timestamp, time.timestamp)
        && Objects.equals(this.interval, time.interval)
        && Objects.equals(this.resolution, time.resolution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, timestamp, interval, resolution);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiRecordsTimeDto {\n"
            + "    date: "
            + toIndentedString(date)
            + "\n"
            + "    timestamp: "
            + toIndentedString(timestamp)
            + "\n"
            + "    interval: "
            + toIndentedString(interval)
            + "\n"
            + "    resolution: "
            + toIndentedString(resolution)
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

    private OgcApiRecordsTimeDto instance;

    public Builder() {
      this(new OgcApiRecordsTimeDto());
    }

    protected Builder(OgcApiRecordsTimeDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiRecordsTimeDto value) {
      this.instance.setDate(value.date);
      this.instance.setTimestamp(value.timestamp);
      this.instance.setInterval(value.interval);
      this.instance.setResolution(value.resolution);
      return this;
    }

    public Builder date(String date) {
      this.instance.date(date);
      return this;
    }

    public Builder timestamp(String timestamp) {
      this.instance.timestamp(timestamp);
      return this;
    }

    public Builder interval(List<OgcApiRecordsTimeIntervalInnerDto> interval) {
      this.instance.interval(interval);
      return this;
    }

    public Builder resolution(String resolution) {
      this.instance.resolution(resolution);
      return this;
    }

    /**
     * returns a built OgcApiRecordsTimeDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiRecordsTimeDto build() {
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
