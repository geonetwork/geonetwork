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
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** GdalLayerDto */
@JsonTypeName("layer")
@JacksonXmlRootElement(localName = "GdalLayerDto")
@XmlRootElement(name = "GdalLayerDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalLayerDto {

  private String name;

  private GdalMetadataDto metadata = new GdalMetadataDto();

  private String fidColumnName;

  private BigDecimal featureCount;

  @Valid private List<@Valid GdalFeatureDto> features = new ArrayList<>();

  @Valid private List<@Valid GdalFieldDto> fields = new ArrayList<>();

  @Valid private List<@Valid GdalGeometryFieldDto> geometryFields = new ArrayList<>();

  public GdalLayerDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GdalLayerDto(
      String name,
      GdalMetadataDto metadata,
      List<@Valid GdalFieldDto> fields,
      List<@Valid GdalGeometryFieldDto> geometryFields) {
    this.name = name;
    this.metadata = metadata;
    this.fields = fields;
    this.geometryFields = geometryFields;
  }

  public GdalLayerDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   *
   * @return name
   */
  @NotNull
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  @JacksonXmlProperty(localName = "name")
  @XmlElement(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GdalLayerDto metadata(GdalMetadataDto metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get metadata
   *
   * @return metadata
   */
  @NotNull
  @Valid
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metadata")
  @JacksonXmlProperty(localName = "metadata")
  @XmlElement(name = "metadata")
  public GdalMetadataDto getMetadata() {
    return metadata;
  }

  public void setMetadata(GdalMetadataDto metadata) {
    this.metadata = metadata;
  }

  public GdalLayerDto fidColumnName(String fidColumnName) {
    this.fidColumnName = fidColumnName;
    return this;
  }

  /**
   * Get fidColumnName
   *
   * @return fidColumnName
   */
  @Schema(name = "fidColumnName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fidColumnName")
  @JacksonXmlProperty(localName = "fidColumnName")
  @XmlElement(name = "fidColumnName")
  public String getFidColumnName() {
    return fidColumnName;
  }

  public void setFidColumnName(String fidColumnName) {
    this.fidColumnName = fidColumnName;
  }

  public GdalLayerDto featureCount(BigDecimal featureCount) {
    this.featureCount = featureCount;
    return this;
  }

  /**
   * Get featureCount
   *
   * @return featureCount
   */
  @Valid
  @Schema(name = "featureCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("featureCount")
  @JacksonXmlProperty(localName = "featureCount")
  @XmlElement(name = "featureCount")
  public BigDecimal getFeatureCount() {
    return featureCount;
  }

  public void setFeatureCount(BigDecimal featureCount) {
    this.featureCount = featureCount;
  }

  public GdalLayerDto features(List<@Valid GdalFeatureDto> features) {
    this.features = features;
    return this;
  }

  public GdalLayerDto addFeaturesItem(GdalFeatureDto featuresItem) {
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
  @Valid
  @Schema(name = "features", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("features")
  @JacksonXmlProperty(localName = "features")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "features")
  public List<@Valid GdalFeatureDto> getFeatures() {
    return features;
  }

  public void setFeatures(List<@Valid GdalFeatureDto> features) {
    this.features = features;
  }

  public GdalLayerDto fields(List<@Valid GdalFieldDto> fields) {
    this.fields = fields;
    return this;
  }

  public GdalLayerDto addFieldsItem(GdalFieldDto fieldsItem) {
    if (this.fields == null) {
      this.fields = new ArrayList<>();
    }
    this.fields.add(fieldsItem);
    return this;
  }

  /**
   * Get fields
   *
   * @return fields
   */
  @NotNull
  @Valid
  @Schema(name = "fields", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fields")
  @JacksonXmlProperty(localName = "fields")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "fields")
  public List<@Valid GdalFieldDto> getFields() {
    return fields;
  }

  public void setFields(List<@Valid GdalFieldDto> fields) {
    this.fields = fields;
  }

  public GdalLayerDto geometryFields(List<@Valid GdalGeometryFieldDto> geometryFields) {
    this.geometryFields = geometryFields;
    return this;
  }

  public GdalLayerDto addGeometryFieldsItem(GdalGeometryFieldDto geometryFieldsItem) {
    if (this.geometryFields == null) {
      this.geometryFields = new ArrayList<>();
    }
    this.geometryFields.add(geometryFieldsItem);
    return this;
  }

  /**
   * Get geometryFields
   *
   * @return geometryFields
   */
  @NotNull
  @Valid
  @Schema(name = "geometryFields", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("geometryFields")
  @JacksonXmlProperty(localName = "geometryFields")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "geometryFields")
  public List<@Valid GdalGeometryFieldDto> getGeometryFields() {
    return geometryFields;
  }

  public void setGeometryFields(List<@Valid GdalGeometryFieldDto> geometryFields) {
    this.geometryFields = geometryFields;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalLayerDto layer = (GdalLayerDto) o;
    return Objects.equals(this.name, layer.name)
        && Objects.equals(this.metadata, layer.metadata)
        && Objects.equals(this.fidColumnName, layer.fidColumnName)
        && Objects.equals(this.featureCount, layer.featureCount)
        && Objects.equals(this.features, layer.features)
        && Objects.equals(this.fields, layer.fields)
        && Objects.equals(this.geometryFields, layer.geometryFields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name, metadata, fidColumnName, featureCount, features, fields, geometryFields);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalLayerDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    fidColumnName: ").append(toIndentedString(fidColumnName)).append("\n");
    sb.append("    featureCount: ").append(toIndentedString(featureCount)).append("\n");
    sb.append("    features: ").append(toIndentedString(features)).append("\n");
    sb.append("    fields: ").append(toIndentedString(fields)).append("\n");
    sb.append("    geometryFields: ").append(toIndentedString(geometryFields)).append("\n");
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

    private GdalLayerDto instance;

    public Builder() {
      this(new GdalLayerDto());
    }

    protected Builder(GdalLayerDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalLayerDto value) {
      this.instance.setName(value.name);
      this.instance.setMetadata(value.metadata);
      this.instance.setFidColumnName(value.fidColumnName);
      this.instance.setFeatureCount(value.featureCount);
      this.instance.setFeatures(value.features);
      this.instance.setFields(value.fields);
      this.instance.setGeometryFields(value.geometryFields);
      return this;
    }

    public Builder name(String name) {
      this.instance.name(name);
      return this;
    }

    public Builder metadata(GdalMetadataDto metadata) {
      this.instance.metadata(metadata);
      return this;
    }

    public Builder fidColumnName(String fidColumnName) {
      this.instance.fidColumnName(fidColumnName);
      return this;
    }

    public Builder featureCount(BigDecimal featureCount) {
      this.instance.featureCount(featureCount);
      return this;
    }

    public Builder features(List<@Valid GdalFeatureDto> features) {
      this.instance.features(features);
      return this;
    }

    public Builder fields(List<@Valid GdalFieldDto> fields) {
      this.instance.fields(fields);
      return this;
    }

    public Builder geometryFields(List<@Valid GdalGeometryFieldDto> geometryFields) {
      this.instance.geometryFields(geometryFields);
      return this;
    }

    /**
     * returns a built GdalLayerDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalLayerDto build() {
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
