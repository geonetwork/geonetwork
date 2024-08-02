/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

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

/** OgcApiRecordsFeatureCollectionGeoJSONDto */
@JsonTypeName("featureCollectionGeoJSON")
@JacksonXmlRootElement(localName = "OgcApiRecordsFeatureCollectionGeoJSONDto")
@XmlRootElement(name = "OgcApiRecordsFeatureCollectionGeoJSONDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiRecordsFeatureCollectionGeoJSONDto {

  private TypeEnum type;
  @Valid private List<@Valid OgcApiRecordsFeatureGeoJSONDto> features = new ArrayList<>();
  @Valid private List<@Valid OgcApiRecordsLinkDto> links = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timeStamp;

  private Integer numberMatched;
  private Integer numberReturned;

  public OgcApiRecordsFeatureCollectionGeoJSONDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiRecordsFeatureCollectionGeoJSONDto(
      TypeEnum type, List<@Valid OgcApiRecordsFeatureGeoJSONDto> features) {
    this.type = type;
    this.features = features;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiRecordsFeatureCollectionGeoJSONDto type(TypeEnum type) {
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

  public OgcApiRecordsFeatureCollectionGeoJSONDto features(
      List<@Valid OgcApiRecordsFeatureGeoJSONDto> features) {
    this.features = features;
    return this;
  }

  public OgcApiRecordsFeatureCollectionGeoJSONDto addFeaturesItem(
      OgcApiRecordsFeatureGeoJSONDto featuresItem) {
    if (this.features == null) {
      this.features = new ArrayList<>();
    }
    this.features.add(featuresItem);
    return this;
  }

  /**
   * Get features
   *
   * @return features
   */
  @NotNull
  @Valid
  @Schema(name = "features", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("features")
  @JacksonXmlProperty(localName = "features")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "features")
  public List<@Valid OgcApiRecordsFeatureGeoJSONDto> getFeatures() {
    return features;
  }

  public void setFeatures(List<@Valid OgcApiRecordsFeatureGeoJSONDto> features) {
    this.features = features;
  }

  public OgcApiRecordsFeatureCollectionGeoJSONDto links(List<@Valid OgcApiRecordsLinkDto> links) {
    this.links = links;
    return this;
  }

  public OgcApiRecordsFeatureCollectionGeoJSONDto addLinksItem(OgcApiRecordsLinkDto linksItem) {
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
  public List<@Valid OgcApiRecordsLinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<@Valid OgcApiRecordsLinkDto> links) {
    this.links = links;
  }

  public OgcApiRecordsFeatureCollectionGeoJSONDto timeStamp(OffsetDateTime timeStamp) {
    this.timeStamp = timeStamp;
    return this;
  }

  /**
   * Get timeStamp
   *
   * @return timeStamp
   */
  @Valid
  @Schema(name = "timeStamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeStamp")
  @JacksonXmlProperty(localName = "timeStamp")
  @XmlElement(name = "timeStamp")
  public OffsetDateTime getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(OffsetDateTime timeStamp) {
    this.timeStamp = timeStamp;
  }

  public OgcApiRecordsFeatureCollectionGeoJSONDto numberMatched(Integer numberMatched) {
    this.numberMatched = numberMatched;
    return this;
  }

  /**
   * Get numberMatched minimum: 0
   *
   * @return numberMatched
   */
  @Min(0)
  @Schema(name = "numberMatched", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("numberMatched")
  @JacksonXmlProperty(localName = "numberMatched")
  @XmlElement(name = "numberMatched")
  public Integer getNumberMatched() {
    return numberMatched;
  }

  public void setNumberMatched(Integer numberMatched) {
    this.numberMatched = numberMatched;
  }

  public OgcApiRecordsFeatureCollectionGeoJSONDto numberReturned(Integer numberReturned) {
    this.numberReturned = numberReturned;
    return this;
  }

  /**
   * Get numberReturned minimum: 0
   *
   * @return numberReturned
   */
  @Min(0)
  @Schema(name = "numberReturned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("numberReturned")
  @JacksonXmlProperty(localName = "numberReturned")
  @XmlElement(name = "numberReturned")
  public Integer getNumberReturned() {
    return numberReturned;
  }

  public void setNumberReturned(Integer numberReturned) {
    this.numberReturned = numberReturned;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiRecordsFeatureCollectionGeoJSONDto featureCollectionGeoJSON =
        (OgcApiRecordsFeatureCollectionGeoJSONDto) o;
    return Objects.equals(this.type, featureCollectionGeoJSON.type)
        && Objects.equals(this.features, featureCollectionGeoJSON.features)
        && Objects.equals(this.links, featureCollectionGeoJSON.links)
        && Objects.equals(this.timeStamp, featureCollectionGeoJSON.timeStamp)
        && Objects.equals(this.numberMatched, featureCollectionGeoJSON.numberMatched)
        && Objects.equals(this.numberReturned, featureCollectionGeoJSON.numberReturned);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, features, links, timeStamp, numberMatched, numberReturned);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiRecordsFeatureCollectionGeoJSONDto {\n"
            + "    type: "
            + toIndentedString(type)
            + "\n"
            + "    features: "
            + toIndentedString(features)
            + "\n"
            + "    links: "
            + toIndentedString(links)
            + "\n"
            + "    timeStamp: "
            + toIndentedString(timeStamp)
            + "\n"
            + "    numberMatched: "
            + toIndentedString(numberMatched)
            + "\n"
            + "    numberReturned: "
            + toIndentedString(numberReturned)
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
    FEATURE_COLLECTION("FeatureCollection");

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

    private OgcApiRecordsFeatureCollectionGeoJSONDto instance;

    public Builder() {
      this(new OgcApiRecordsFeatureCollectionGeoJSONDto());
    }

    protected Builder(OgcApiRecordsFeatureCollectionGeoJSONDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiRecordsFeatureCollectionGeoJSONDto value) {
      this.instance.setType(value.type);
      this.instance.setFeatures(value.features);
      this.instance.setLinks(value.links);
      this.instance.setTimeStamp(value.timeStamp);
      this.instance.setNumberMatched(value.numberMatched);
      this.instance.setNumberReturned(value.numberReturned);
      return this;
    }

    public Builder type(TypeEnum type) {
      this.instance.type(type);
      return this;
    }

    public Builder features(List<@Valid OgcApiRecordsFeatureGeoJSONDto> features) {
      this.instance.features(features);
      return this;
    }

    public Builder links(List<@Valid OgcApiRecordsLinkDto> links) {
      this.instance.links(links);
      return this;
    }

    public Builder timeStamp(OffsetDateTime timeStamp) {
      this.instance.timeStamp(timeStamp);
      return this;
    }

    public Builder numberMatched(Integer numberMatched) {
      this.instance.numberMatched(numberMatched);
      return this;
    }

    public Builder numberReturned(Integer numberReturned) {
      this.instance.numberReturned(numberReturned);
      return this;
    }

    /**
     * returns a built OgcApiRecordsFeatureCollectionGeoJSONDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiRecordsFeatureCollectionGeoJSONDto build() {
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
