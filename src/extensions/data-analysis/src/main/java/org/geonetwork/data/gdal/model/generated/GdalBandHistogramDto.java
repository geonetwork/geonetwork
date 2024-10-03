/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** GdalBandHistogramDto */
@JsonTypeName("band_histogram")
@JacksonXmlRootElement(localName = "GdalBandHistogramDto")
@XmlRootElement(name = "GdalBandHistogramDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalBandHistogramDto {

  @Valid private List<Integer> buckets = new ArrayList<>();

  private Integer count;

  private BigDecimal min;

  private BigDecimal max;

  public GdalBandHistogramDto buckets(List<Integer> buckets) {
    this.buckets = buckets;
    return this;
  }

  public GdalBandHistogramDto addBucketsItem(Integer bucketsItem) {
    if (this.buckets == null) {
      this.buckets = new ArrayList<>();
    }
    this.buckets.add(bucketsItem);
    return this;
  }

  /**
   * Get buckets
   *
   * @return buckets
   */
  @Schema(name = "buckets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buckets")
  @JacksonXmlProperty(localName = "buckets")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "buckets")
  public List<Integer> getBuckets() {
    return buckets;
  }

  public void setBuckets(List<Integer> buckets) {
    this.buckets = buckets;
  }

  public GdalBandHistogramDto count(Integer count) {
    this.count = count;
    return this;
  }

  /**
   * Get count
   *
   * @return count
   */
  @Schema(name = "count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("count")
  @JacksonXmlProperty(localName = "count")
  @XmlElement(name = "count")
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public GdalBandHistogramDto min(BigDecimal min) {
    this.min = min;
    return this;
  }

  /**
   * Get min
   *
   * @return min
   */
  @Valid
  @Schema(name = "min", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min")
  @JacksonXmlProperty(localName = "min")
  @XmlElement(name = "min")
  public BigDecimal getMin() {
    return min;
  }

  public void setMin(BigDecimal min) {
    this.min = min;
  }

  public GdalBandHistogramDto max(BigDecimal max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   *
   * @return max
   */
  @Valid
  @Schema(name = "max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max")
  @JacksonXmlProperty(localName = "max")
  @XmlElement(name = "max")
  public BigDecimal getMax() {
    return max;
  }

  public void setMax(BigDecimal max) {
    this.max = max;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalBandHistogramDto bandHistogram = (GdalBandHistogramDto) o;
    return Objects.equals(this.buckets, bandHistogram.buckets)
        && Objects.equals(this.count, bandHistogram.count)
        && Objects.equals(this.min, bandHistogram.min)
        && Objects.equals(this.max, bandHistogram.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(buckets, count, min, max);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalBandHistogramDto {\n");
    sb.append("    buckets: ").append(toIndentedString(buckets)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    min: ").append(toIndentedString(min)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
    sb.append("}");
    return sb.toString();
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

  public static class Builder {

    private GdalBandHistogramDto instance;

    public Builder() {
      this(new GdalBandHistogramDto());
    }

    protected Builder(GdalBandHistogramDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalBandHistogramDto value) {
      this.instance.setBuckets(value.buckets);
      this.instance.setCount(value.count);
      this.instance.setMin(value.min);
      this.instance.setMax(value.max);
      return this;
    }

    public Builder buckets(List<Integer> buckets) {
      this.instance.buckets(buckets);
      return this;
    }

    public Builder count(Integer count) {
      this.instance.count(count);
      return this;
    }

    public Builder min(BigDecimal min) {
      this.instance.min(min);
      return this;
    }

    public Builder max(BigDecimal max) {
      this.instance.max(max);
      return this;
    }

    /**
     * returns a built GdalBandHistogramDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalBandHistogramDto build() {
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

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  /** Create a builder with a shallow copy of this instance. */
  public Builder toBuilder() {
    Builder builder = new Builder();
    return builder.copyOf(this);
  }
}
