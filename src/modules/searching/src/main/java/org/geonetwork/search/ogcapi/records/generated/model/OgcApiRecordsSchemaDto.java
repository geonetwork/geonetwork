/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The description of OpenAPI v3.0.x documents, as defined by https://spec.openapis.org/oas/v3.0.3
 */
@Schema(
    name = "schema",
    description =
        "The description of OpenAPI v3.0.x documents, as defined by"
            + " https://spec.openapis.org/oas/v3.0.3")
@JsonTypeName("schema")
@JacksonXmlRootElement(localName = "OgcApiRecordsSchemaDto")
@XmlRootElement(name = "OgcApiRecordsSchemaDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiRecordsSchemaDto {

  private String openapi;

  private OgcApiRecordsInfoDto info;

  private OgcApiRecordsExternalDocumentationDto externalDocs;

  @Valid private List<@Valid OgcApiRecordsServerDto> servers = new ArrayList<>();

  @Valid private List<OgcApiRecordsSecurityRequirementDto> security = new ArrayList<>();

  @Valid private Set<@Valid OgcApiRecordsTagDto> tags = new LinkedHashSet<>();

  private Object paths;

  private OgcApiRecordsComponentsDto components;

  public OgcApiRecordsSchemaDto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiRecordsSchemaDto(String openapi, OgcApiRecordsInfoDto info, Object paths) {
    this.openapi = openapi;
    this.info = info;
    this.paths = paths;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiRecordsSchemaDto openapi(String openapi) {
    this.openapi = openapi;
    return this;
  }

  /**
   * Get openapi
   *
   * @return openapi
   */
  @NotNull
  @Pattern(regexp = "^3\\.0\\.\\d(-.+)?$")
  @Schema(name = "openapi", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("openapi")
  @JacksonXmlProperty(localName = "openapi")
  @XmlElement(name = "openapi")
  public String getOpenapi() {
    return openapi;
  }

  public void setOpenapi(String openapi) {
    this.openapi = openapi;
  }

  public OgcApiRecordsSchemaDto info(OgcApiRecordsInfoDto info) {
    this.info = info;
    return this;
  }

  /**
   * Get info
   *
   * @return info
   */
  @NotNull
  @Valid
  @Schema(name = "info", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("info")
  @JacksonXmlProperty(localName = "info")
  @XmlElement(name = "info")
  public OgcApiRecordsInfoDto getInfo() {
    return info;
  }

  public void setInfo(OgcApiRecordsInfoDto info) {
    this.info = info;
  }

  public OgcApiRecordsSchemaDto externalDocs(OgcApiRecordsExternalDocumentationDto externalDocs) {
    this.externalDocs = externalDocs;
    return this;
  }

  /**
   * Get externalDocs
   *
   * @return externalDocs
   */
  @Valid
  @Schema(name = "externalDocs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("externalDocs")
  @JacksonXmlProperty(localName = "externalDocs")
  @XmlElement(name = "externalDocs")
  public OgcApiRecordsExternalDocumentationDto getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(OgcApiRecordsExternalDocumentationDto externalDocs) {
    this.externalDocs = externalDocs;
  }

  public OgcApiRecordsSchemaDto servers(List<@Valid OgcApiRecordsServerDto> servers) {
    this.servers = servers;
    return this;
  }

  public OgcApiRecordsSchemaDto addServersItem(OgcApiRecordsServerDto serversItem) {
    if (this.servers == null) {
      this.servers = new ArrayList<>();
    }
    this.servers.add(serversItem);
    return this;
  }

  /**
   * Get servers
   *
   * @return servers
   */
  @Valid
  @Schema(name = "servers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("servers")
  @JacksonXmlProperty(localName = "servers")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "servers")
  public List<@Valid OgcApiRecordsServerDto> getServers() {
    return servers;
  }

  public void setServers(List<@Valid OgcApiRecordsServerDto> servers) {
    this.servers = servers;
  }

  public OgcApiRecordsSchemaDto security(List<OgcApiRecordsSecurityRequirementDto> security) {
    this.security = security;
    return this;
  }

  public OgcApiRecordsSchemaDto addSecurityItem(OgcApiRecordsSecurityRequirementDto securityItem) {
    if (this.security == null) {
      this.security = new ArrayList<>();
    }
    this.security.add(securityItem);
    return this;
  }

  /**
   * Get security
   *
   * @return security
   */
  @Valid
  @Schema(name = "security", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("security")
  @JacksonXmlProperty(localName = "security")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "security")
  public List<OgcApiRecordsSecurityRequirementDto> getSecurity() {
    return security;
  }

  public void setSecurity(List<OgcApiRecordsSecurityRequirementDto> security) {
    this.security = security;
  }

  public OgcApiRecordsSchemaDto tags(Set<@Valid OgcApiRecordsTagDto> tags) {
    this.tags = tags;
    return this;
  }

  public OgcApiRecordsSchemaDto addTagsItem(OgcApiRecordsTagDto tagsItem) {
    if (this.tags == null) {
      this.tags = new LinkedHashSet<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   *
   * @return tags
   */
  @Valid
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  @JacksonXmlProperty(localName = "tags")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "tags")
  public Set<@Valid OgcApiRecordsTagDto> getTags() {
    return tags;
  }

  @JsonDeserialize(as = LinkedHashSet.class)
  public void setTags(Set<@Valid OgcApiRecordsTagDto> tags) {
    this.tags = tags;
  }

  public OgcApiRecordsSchemaDto paths(Object paths) {
    this.paths = paths;
    return this;
  }

  /**
   * Get paths
   *
   * @return paths
   */
  @NotNull
  @Schema(name = "paths", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("paths")
  @JacksonXmlProperty(localName = "paths")
  @XmlElement(name = "paths")
  public Object getPaths() {
    return paths;
  }

  public void setPaths(Object paths) {
    this.paths = paths;
  }

  public OgcApiRecordsSchemaDto components(OgcApiRecordsComponentsDto components) {
    this.components = components;
    return this;
  }

  /**
   * Get components
   *
   * @return components
   */
  @Valid
  @Schema(name = "components", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("components")
  @JacksonXmlProperty(localName = "components")
  @XmlElement(name = "components")
  public OgcApiRecordsComponentsDto getComponents() {
    return components;
  }

  public void setComponents(OgcApiRecordsComponentsDto components) {
    this.components = components;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiRecordsSchemaDto schema = (OgcApiRecordsSchemaDto) o;
    return Objects.equals(this.openapi, schema.openapi)
        && Objects.equals(this.info, schema.info)
        && Objects.equals(this.externalDocs, schema.externalDocs)
        && Objects.equals(this.servers, schema.servers)
        && Objects.equals(this.security, schema.security)
        && Objects.equals(this.tags, schema.tags)
        && Objects.equals(this.paths, schema.paths)
        && Objects.equals(this.components, schema.components);
  }

  @Override
  public int hashCode() {
    return Objects.hash(openapi, info, externalDocs, servers, security, tags, paths, components);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiRecordsSchemaDto {\n"
            + "    openapi: "
            + toIndentedString(openapi)
            + "\n"
            + "    info: "
            + toIndentedString(info)
            + "\n"
            + "    externalDocs: "
            + toIndentedString(externalDocs)
            + "\n"
            + "    servers: "
            + toIndentedString(servers)
            + "\n"
            + "    security: "
            + toIndentedString(security)
            + "\n"
            + "    tags: "
            + toIndentedString(tags)
            + "\n"
            + "    paths: "
            + toIndentedString(paths)
            + "\n"
            + "    components: "
            + toIndentedString(components)
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

    private OgcApiRecordsSchemaDto instance;

    public Builder() {
      this(new OgcApiRecordsSchemaDto());
    }

    protected Builder(OgcApiRecordsSchemaDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiRecordsSchemaDto value) {
      this.instance.setOpenapi(value.openapi);
      this.instance.setInfo(value.info);
      this.instance.setExternalDocs(value.externalDocs);
      this.instance.setServers(value.servers);
      this.instance.setSecurity(value.security);
      this.instance.setTags(value.tags);
      this.instance.setPaths(value.paths);
      this.instance.setComponents(value.components);
      return this;
    }

    public Builder openapi(String openapi) {
      this.instance.openapi(openapi);
      return this;
    }

    public Builder info(OgcApiRecordsInfoDto info) {
      this.instance.info(info);
      return this;
    }

    public Builder externalDocs(OgcApiRecordsExternalDocumentationDto externalDocs) {
      this.instance.externalDocs(externalDocs);
      return this;
    }

    public Builder servers(List<@Valid OgcApiRecordsServerDto> servers) {
      this.instance.servers(servers);
      return this;
    }

    public Builder security(List<OgcApiRecordsSecurityRequirementDto> security) {
      this.instance.security(security);
      return this;
    }

    public Builder tags(Set<@Valid OgcApiRecordsTagDto> tags) {
      this.instance.tags(tags);
      return this;
    }

    public Builder paths(Object paths) {
      this.instance.paths(paths);
      return this;
    }

    public Builder components(OgcApiRecordsComponentsDto components) {
      this.instance.components(components);
      return this;
    }

    /**
     * returns a built OgcApiRecordsSchemaDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiRecordsSchemaDto build() {
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
