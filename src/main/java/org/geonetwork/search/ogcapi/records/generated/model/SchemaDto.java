/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
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
@JacksonXmlRootElement(localName = "SchemaDto")
@XmlRootElement(name = "SchemaDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]",
    comments = "Generator version: 7.6.0")
public class SchemaDto {

  private String openapi;

  private InfoDto info;

  private ExternalDocumentationDto externalDocs;

  @Valid private List<@Valid ServerDto> servers = new ArrayList<>();

  @Valid private List<SecurityRequirementDto> security = new ArrayList<>();

  @Valid private Set<@Valid TagDto> tags = new LinkedHashSet<>();

  private Object paths;

  private ComponentsDto components;

  public SchemaDto() {
    super();
  }

  /** Constructor with only required parameters */
  public SchemaDto(String openapi, InfoDto info, Object paths) {
    this.openapi = openapi;
    this.info = info;
    this.paths = paths;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public SchemaDto openapi(String openapi) {
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
  public String getOpenapi() {
    return openapi;
  }

  public void setOpenapi(String openapi) {
    this.openapi = openapi;
  }

  public SchemaDto info(InfoDto info) {
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
  public InfoDto getInfo() {
    return info;
  }

  public void setInfo(InfoDto info) {
    this.info = info;
  }

  public SchemaDto externalDocs(ExternalDocumentationDto externalDocs) {
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
  public ExternalDocumentationDto getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(ExternalDocumentationDto externalDocs) {
    this.externalDocs = externalDocs;
  }

  public SchemaDto servers(List<@Valid ServerDto> servers) {
    this.servers = servers;
    return this;
  }

  public SchemaDto addServersItem(ServerDto serversItem) {
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
  public List<@Valid ServerDto> getServers() {
    return servers;
  }

  public void setServers(List<@Valid ServerDto> servers) {
    this.servers = servers;
  }

  public SchemaDto security(List<SecurityRequirementDto> security) {
    this.security = security;
    return this;
  }

  public SchemaDto addSecurityItem(SecurityRequirementDto securityItem) {
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
  public List<SecurityRequirementDto> getSecurity() {
    return security;
  }

  public void setSecurity(List<SecurityRequirementDto> security) {
    this.security = security;
  }

  public SchemaDto tags(Set<@Valid TagDto> tags) {
    this.tags = tags;
    return this;
  }

  public SchemaDto addTagsItem(TagDto tagsItem) {
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
  public Set<@Valid TagDto> getTags() {
    return tags;
  }

  @JsonDeserialize(as = LinkedHashSet.class)
  public void setTags(Set<@Valid TagDto> tags) {
    this.tags = tags;
  }

  public SchemaDto paths(Object paths) {
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
  public Object getPaths() {
    return paths;
  }

  public void setPaths(Object paths) {
    this.paths = paths;
  }

  public SchemaDto components(ComponentsDto components) {
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
  public ComponentsDto getComponents() {
    return components;
  }

  public void setComponents(ComponentsDto components) {
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
    SchemaDto schema = (SchemaDto) o;
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
        "class SchemaDto {\n"
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

    private SchemaDto instance;

    public Builder() {
      this(new SchemaDto());
    }

    protected Builder(SchemaDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(SchemaDto value) {
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

    public Builder info(InfoDto info) {
      this.instance.info(info);
      return this;
    }

    public Builder externalDocs(ExternalDocumentationDto externalDocs) {
      this.instance.externalDocs(externalDocs);
      return this;
    }

    public Builder servers(List<@Valid ServerDto> servers) {
      this.instance.servers(servers);
      return this;
    }

    public Builder security(List<SecurityRequirementDto> security) {
      this.instance.security(security);
      return this;
    }

    public Builder tags(Set<@Valid TagDto> tags) {
      this.instance.tags(tags);
      return this;
    }

    public Builder paths(Object paths) {
      this.instance.paths(paths);
      return this;
    }

    public Builder components(ComponentsDto components) {
      this.instance.components(components);
      return this;
    }

    /**
     * returns a built SchemaDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public SchemaDto build() {
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
