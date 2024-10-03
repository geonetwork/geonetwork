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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** GdalOgrinfoDatasetDto */
@JsonTypeName("ogrinfo_dataset")
@JacksonXmlRootElement(localName = "GdalOgrinfoDatasetDto")
@XmlRootElement(name = "GdalOgrinfoDatasetDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalOgrinfoDatasetDto {

  private String description;

  private String driverShortName;

  private String driverLongName;

  @Valid private List<@Valid GdalLayerDto> layers = new ArrayList<>();

  private GdalMetadataDto metadata = new GdalMetadataDto();

  private GdalDomainsDto domains = new GdalDomainsDto();

  private GdalRelationshipsDto relationships = new GdalRelationshipsDto();

  private GdalGroupDto rootGroup;

  public GdalOgrinfoDatasetDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GdalOgrinfoDatasetDto(
      List<@Valid GdalLayerDto> layers, GdalMetadataDto metadata, GdalDomainsDto domains) {
    this.layers = layers;
    this.metadata = metadata;
    this.domains = domains;
  }

  public GdalOgrinfoDatasetDto description(String description) {
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

  public GdalOgrinfoDatasetDto driverShortName(String driverShortName) {
    this.driverShortName = driverShortName;
    return this;
  }

  /**
   * Get driverShortName
   *
   * @return driverShortName
   */
  @Schema(name = "driverShortName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("driverShortName")
  @JacksonXmlProperty(localName = "driverShortName")
  @XmlElement(name = "driverShortName")
  public String getDriverShortName() {
    return driverShortName;
  }

  public void setDriverShortName(String driverShortName) {
    this.driverShortName = driverShortName;
  }

  public GdalOgrinfoDatasetDto driverLongName(String driverLongName) {
    this.driverLongName = driverLongName;
    return this;
  }

  /**
   * Get driverLongName
   *
   * @return driverLongName
   */
  @Schema(name = "driverLongName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("driverLongName")
  @JacksonXmlProperty(localName = "driverLongName")
  @XmlElement(name = "driverLongName")
  public String getDriverLongName() {
    return driverLongName;
  }

  public void setDriverLongName(String driverLongName) {
    this.driverLongName = driverLongName;
  }

  public GdalOgrinfoDatasetDto layers(List<@Valid GdalLayerDto> layers) {
    this.layers = layers;
    return this;
  }

  public GdalOgrinfoDatasetDto addLayersItem(GdalLayerDto layersItem) {
    if (this.layers == null) {
      this.layers = new ArrayList<>();
    }
    this.layers.add(layersItem);
    return this;
  }

  /**
   * Get layers
   *
   * @return layers
   */
  @NotNull
  @Valid
  @Schema(name = "layers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("layers")
  @JacksonXmlProperty(localName = "layers")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "layers")
  public List<@Valid GdalLayerDto> getLayers() {
    return layers;
  }

  public void setLayers(List<@Valid GdalLayerDto> layers) {
    this.layers = layers;
  }

  public GdalOgrinfoDatasetDto metadata(GdalMetadataDto metadata) {
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

  public GdalOgrinfoDatasetDto domains(GdalDomainsDto domains) {
    this.domains = domains;
    return this;
  }

  /**
   * Get domains
   *
   * @return domains
   */
  @NotNull
  @Valid
  @Schema(name = "domains", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("domains")
  @JacksonXmlProperty(localName = "domains")
  @XmlElement(name = "domains")
  public GdalDomainsDto getDomains() {
    return domains;
  }

  public void setDomains(GdalDomainsDto domains) {
    this.domains = domains;
  }

  public GdalOgrinfoDatasetDto relationships(GdalRelationshipsDto relationships) {
    this.relationships = relationships;
    return this;
  }

  /**
   * Get relationships
   *
   * @return relationships
   */
  @Valid
  @Schema(name = "relationships", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationships")
  @JacksonXmlProperty(localName = "relationships")
  @XmlElement(name = "relationships")
  public GdalRelationshipsDto getRelationships() {
    return relationships;
  }

  public void setRelationships(GdalRelationshipsDto relationships) {
    this.relationships = relationships;
  }

  public GdalOgrinfoDatasetDto rootGroup(GdalGroupDto rootGroup) {
    this.rootGroup = rootGroup;
    return this;
  }

  /**
   * Get rootGroup
   *
   * @return rootGroup
   */
  @Valid
  @Schema(name = "rootGroup", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rootGroup")
  @JacksonXmlProperty(localName = "rootGroup")
  @XmlElement(name = "rootGroup")
  public GdalGroupDto getRootGroup() {
    return rootGroup;
  }

  public void setRootGroup(GdalGroupDto rootGroup) {
    this.rootGroup = rootGroup;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalOgrinfoDatasetDto ogrinfoDataset = (GdalOgrinfoDatasetDto) o;
    return Objects.equals(this.description, ogrinfoDataset.description)
        && Objects.equals(this.driverShortName, ogrinfoDataset.driverShortName)
        && Objects.equals(this.driverLongName, ogrinfoDataset.driverLongName)
        && Objects.equals(this.layers, ogrinfoDataset.layers)
        && Objects.equals(this.metadata, ogrinfoDataset.metadata)
        && Objects.equals(this.domains, ogrinfoDataset.domains)
        && Objects.equals(this.relationships, ogrinfoDataset.relationships)
        && Objects.equals(this.rootGroup, ogrinfoDataset.rootGroup);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        description,
        driverShortName,
        driverLongName,
        layers,
        metadata,
        domains,
        relationships,
        rootGroup);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalOgrinfoDatasetDto {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    driverShortName: ").append(toIndentedString(driverShortName)).append("\n");
    sb.append("    driverLongName: ").append(toIndentedString(driverLongName)).append("\n");
    sb.append("    layers: ").append(toIndentedString(layers)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    domains: ").append(toIndentedString(domains)).append("\n");
    sb.append("    relationships: ").append(toIndentedString(relationships)).append("\n");
    sb.append("    rootGroup: ").append(toIndentedString(rootGroup)).append("\n");
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

    private GdalOgrinfoDatasetDto instance;

    public Builder() {
      this(new GdalOgrinfoDatasetDto());
    }

    protected Builder(GdalOgrinfoDatasetDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalOgrinfoDatasetDto value) {
      this.instance.setDescription(value.description);
      this.instance.setDriverShortName(value.driverShortName);
      this.instance.setDriverLongName(value.driverLongName);
      this.instance.setLayers(value.layers);
      this.instance.setMetadata(value.metadata);
      this.instance.setDomains(value.domains);
      this.instance.setRelationships(value.relationships);
      this.instance.setRootGroup(value.rootGroup);
      return this;
    }

    public Builder description(String description) {
      this.instance.description(description);
      return this;
    }

    public Builder driverShortName(String driverShortName) {
      this.instance.driverShortName(driverShortName);
      return this;
    }

    public Builder driverLongName(String driverLongName) {
      this.instance.driverLongName(driverLongName);
      return this;
    }

    public Builder layers(List<@Valid GdalLayerDto> layers) {
      this.instance.layers(layers);
      return this;
    }

    public Builder metadata(GdalMetadataDto metadata) {
      this.instance.metadata(metadata);
      return this;
    }

    public Builder domains(GdalDomainsDto domains) {
      this.instance.domains(domains);
      return this;
    }

    public Builder relationships(GdalRelationshipsDto relationships) {
      this.instance.relationships(relationships);
      return this;
    }

    public Builder rootGroup(GdalGroupDto rootGroup) {
      this.instance.rootGroup(rootGroup);
      return this;
    }

    /**
     * returns a built GdalOgrinfoDatasetDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalOgrinfoDatasetDto build() {
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
