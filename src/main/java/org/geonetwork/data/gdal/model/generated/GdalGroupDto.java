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
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** GdalGroupDto */
@JsonTypeName("group")
@JacksonXmlRootElement(localName = "GdalGroupDto")
@XmlRootElement(name = "GdalGroupDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalGroupDto {

  private String name;

  @Valid private List<String> layerNames = new ArrayList<>();

  @Valid private List<@Valid GdalGroupDto> groups = new ArrayList<>();

  public GdalGroupDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GdalGroupDto(List<String> layerNames, List<@Valid GdalGroupDto> groups) {
    this.layerNames = layerNames;
    this.groups = groups;
  }

  public GdalGroupDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   *
   * @return name
   */
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  @JacksonXmlProperty(localName = "name")
  @XmlElement(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GdalGroupDto layerNames(List<String> layerNames) {
    this.layerNames = layerNames;
    return this;
  }

  public GdalGroupDto addLayerNamesItem(String layerNamesItem) {
    if (this.layerNames == null) {
      this.layerNames = new ArrayList<>();
    }
    this.layerNames.add(layerNamesItem);
    return this;
  }

  /**
   * Get layerNames
   *
   * @return layerNames
   */
  @NotNull
  @Schema(name = "layerNames", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("layerNames")
  @JacksonXmlProperty(localName = "layerNames")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "layerNames")
  public List<String> getLayerNames() {
    return layerNames;
  }

  public void setLayerNames(List<String> layerNames) {
    this.layerNames = layerNames;
  }

  public GdalGroupDto groups(List<@Valid GdalGroupDto> groups) {
    this.groups = groups;
    return this;
  }

  public GdalGroupDto addGroupsItem(GdalGroupDto groupsItem) {
    if (this.groups == null) {
      this.groups = new ArrayList<>();
    }
    this.groups.add(groupsItem);
    return this;
  }

  /**
   * Get groups
   *
   * @return groups
   */
  @NotNull
  @Valid
  @Schema(name = "groups", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("groups")
  @JacksonXmlProperty(localName = "groups")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "groups")
  public List<@Valid GdalGroupDto> getGroups() {
    return groups;
  }

  public void setGroups(List<@Valid GdalGroupDto> groups) {
    this.groups = groups;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalGroupDto group = (GdalGroupDto) o;
    return Objects.equals(this.name, group.name)
        && Objects.equals(this.layerNames, group.layerNames)
        && Objects.equals(this.groups, group.groups);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, layerNames, groups);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalGroupDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    layerNames: ").append(toIndentedString(layerNames)).append("\n");
    sb.append("    groups: ").append(toIndentedString(groups)).append("\n");
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

    private GdalGroupDto instance;

    public Builder() {
      this(new GdalGroupDto());
    }

    protected Builder(GdalGroupDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalGroupDto value) {
      this.instance.setName(value.name);
      this.instance.setLayerNames(value.layerNames);
      this.instance.setGroups(value.groups);
      return this;
    }

    public Builder name(String name) {
      this.instance.name(name);
      return this;
    }

    public Builder layerNames(List<String> layerNames) {
      this.instance.layerNames(layerNames);
      return this;
    }

    public Builder groups(List<@Valid GdalGroupDto> groups) {
      this.instance.groups(groups);
      return this;
    }

    /**
     * returns a built GdalGroupDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalGroupDto build() {
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
