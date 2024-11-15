/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.*;
import java.util.Objects;

/** GdalObjectUsageDto */
@JsonTypeName("object_usage")
@JacksonXmlRootElement(localName = "GdalObjectUsageDto")
@XmlRootElement(name = "GdalObjectUsageDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalObjectUsageDto {

    private String $schema;

    private String scope;

    private String area;

    //  FIXME? private GdalBboxDto bbox = null;
    //
    //  private GdalVerticalExtentDto verticalExtent = null;
    //
    //  private GdalTemporalExtentDto temporalExtent = null;

    private String remarks;

    private GdalIdDto id;

    private GdalIdsDto ids = new GdalIdsDto();

    //  private GdalUsagesDto usages = null;

    public GdalObjectUsageDto $schema(String $schema) {
        this.$schema = $schema;
        return this;
    }

    /**
     * Get $schema
     *
     * @return $schema
     */
    @Schema(name = "$schema", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("$schema")
    @JacksonXmlProperty(localName = "$schema")
    @XmlElement(name = "$schema")
    public String get$Schema() {
        return $schema;
    }

    public void set$Schema(String $schema) {
        this.$schema = $schema;
    }

    public GdalObjectUsageDto scope(String scope) {
        this.scope = scope;
        return this;
    }

    /**
     * Get scope
     *
     * @return scope
     */
    @Schema(name = "scope", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("scope")
    @JacksonXmlProperty(localName = "scope")
    @XmlElement(name = "scope")
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public GdalObjectUsageDto area(String area) {
        this.area = area;
        return this;
    }

    /**
     * Get area
     *
     * @return area
     */
    @Schema(name = "area", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("area")
    @JacksonXmlProperty(localName = "area")
    @XmlElement(name = "area")
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    //  public GdalObjectUsageDto bbox(GdalBboxDto bbox) {
    //    this.bbox = bbox;
    //    return this;
    //  }
    //
    //  /**
    //   * Get bbox
    //   *
    //   * @return bbox
    //   */
    //  @Valid
    //  @Schema(name = "bbox", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //  @JsonProperty("bbox")
    //  @JacksonXmlProperty(localName = "bbox")
    //  @XmlElement(name = "bbox")
    //  public GdalBboxDto getBbox() {
    //    return bbox;
    //  }
    //
    //  public void setBbox(GdalBboxDto bbox) {
    //    this.bbox = bbox;
    //  }
    //
    //  public GdalObjectUsageDto verticalExtent(GdalVerticalExtentDto verticalExtent) {
    //    this.verticalExtent = verticalExtent;
    //    return this;
    //  }
    //
    //  /**
    //   * Get verticalExtent
    //   *
    //   * @return verticalExtent
    //   */
    //  @Valid
    //  @Schema(name = "vertical_extent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //  @JsonProperty("vertical_extent")
    //  @JacksonXmlProperty(localName = "vertical_extent")
    //  @XmlElement(name = "vertical_extent")
    //  public GdalVerticalExtentDto getVerticalExtent() {
    //    return verticalExtent;
    //  }
    //
    //  public void setVerticalExtent(GdalVerticalExtentDto verticalExtent) {
    //    this.verticalExtent = verticalExtent;
    //  }
    //
    //  public GdalObjectUsageDto temporalExtent(GdalTemporalExtentDto temporalExtent) {
    //    this.temporalExtent = temporalExtent;
    //    return this;
    //  }
    //
    //  /**
    //   * Get temporalExtent
    //   *
    //   * @return temporalExtent
    //   */
    //  @Valid
    //  @Schema(name = "temporal_extent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //  @JsonProperty("temporal_extent")
    //  @JacksonXmlProperty(localName = "temporal_extent")
    //  @XmlElement(name = "temporal_extent")
    //  public GdalTemporalExtentDto getTemporalExtent() {
    //    return temporalExtent;
    //  }
    //
    //  public void setTemporalExtent(GdalTemporalExtentDto temporalExtent) {
    //    this.temporalExtent = temporalExtent;
    //  }
    //
    //  public GdalObjectUsageDto remarks(String remarks) {
    //    this.remarks = remarks;
    //    return this;
    //  }

    /**
     * Get remarks
     *
     * @return remarks
     */
    @Schema(name = "remarks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("remarks")
    @JacksonXmlProperty(localName = "remarks")
    @XmlElement(name = "remarks")
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public GdalObjectUsageDto id(GdalIdDto id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     */
    @Valid
    @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    @XmlElement(name = "id")
    public GdalIdDto getId() {
        return id;
    }

    public void setId(GdalIdDto id) {
        this.id = id;
    }

    public GdalObjectUsageDto ids(GdalIdsDto ids) {
        this.ids = ids;
        return this;
    }

    /**
     * Get ids
     *
     * @return ids
     */
    @Valid
    @Schema(name = "ids", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("ids")
    @JacksonXmlProperty(localName = "ids")
    @XmlElement(name = "ids")
    public GdalIdsDto getIds() {
        return ids;
    }

    public void setIds(GdalIdsDto ids) {
        this.ids = ids;
    }

    //  public GdalObjectUsageDto usages(GdalUsagesDto usages) {
    //    this.usages = usages;
    //    return this;
    //  }
    //
    //  /**
    //   * Get usages
    //   *
    //   * @return usages
    //   */
    //  @Valid
    //  @Schema(name = "usages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //  @JsonProperty("usages")
    //  @JacksonXmlProperty(localName = "usages")
    //  @XmlElement(name = "usages")
    //  public GdalUsagesDto getUsages() {
    //    return usages;
    //  }
    //
    //  public void setUsages(GdalUsagesDto usages) {
    //    this.usages = usages;
    //  }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalObjectUsageDto objectUsage = (GdalObjectUsageDto) o;
        return Objects.equals(this.$schema, objectUsage.$schema)
                && Objects.equals(this.scope, objectUsage.scope)
                && Objects.equals(this.area, objectUsage.area)
                //        && Objects.equals(this.bbox, objectUsage.bbox)
                //        && Objects.equals(this.verticalExtent, objectUsage.verticalExtent)
                //        && Objects.equals(this.temporalExtent, objectUsage.temporalExtent)
                && Objects.equals(this.remarks, objectUsage.remarks)
                && Objects.equals(this.id, objectUsage.id)
                && Objects.equals(this.ids, objectUsage.ids);
        //        && Objects.equals(this.usages, objectUsage.usages);
    }

    @Override
    public int hashCode() {
        return Objects.hash($schema, scope, area, remarks, id, ids);
        //        $schema, scope, area, bbox, verticalExtent, temporalExtent, remarks, id, ids,
        // usages);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalObjectUsageDto {\n");
        sb.append("    $schema: ").append(toIndentedString($schema)).append("\n");
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        sb.append("    area: ").append(toIndentedString(area)).append("\n");
        //    sb.append("    bbox: ").append(toIndentedString(bbox)).append("\n");
        //    sb.append("    verticalExtent:
        // ").append(toIndentedString(verticalExtent)).append("\n");
        //    sb.append("    temporalExtent:
        // ").append(toIndentedString(temporalExtent)).append("\n");
        sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    ids: ").append(toIndentedString(ids)).append("\n");
        //    sb.append("    usages: ").append(toIndentedString(usages)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /** Convert the given object to string with each line indented by 4 spaces (except the first line). */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    public static class Builder {

        private GdalObjectUsageDto instance;

        public Builder() {
            this(new GdalObjectUsageDto());
        }

        protected Builder(GdalObjectUsageDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalObjectUsageDto value) {
            this.instance.set$Schema(value.$schema);
            this.instance.setScope(value.scope);
            this.instance.setArea(value.area);
            //      this.instance.setBbox(value.bbox);
            //      this.instance.setVerticalExtent(value.verticalExtent);
            //      this.instance.setTemporalExtent(value.temporalExtent);
            this.instance.setRemarks(value.remarks);
            this.instance.setId(value.id);
            this.instance.setIds(value.ids);
            //      this.instance.setUsages(value.usages);
            return this;
        }

        public Builder $schema(String $schema) {
            this.instance.$schema($schema);
            return this;
        }

        public Builder scope(String scope) {
            this.instance.scope(scope);
            return this;
        }

        public Builder area(String area) {
            this.instance.area(area);
            return this;
        }

        //    public Builder bbox(GdalBboxDto bbox) {
        //      this.instance.bbox(bbox);
        //      return this;
        //    }
        //
        //    public Builder verticalExtent(GdalVerticalExtentDto verticalExtent) {
        //      this.instance.verticalExtent(verticalExtent);
        //      return this;
        //    }
        //
        //    public Builder temporalExtent(GdalTemporalExtentDto temporalExtent) {
        //      this.instance.temporalExtent(temporalExtent);
        //      return this;
        //    }

        public Builder remarks(String remarks) {
            this.instance.setRemarks(remarks);
            return this;
        }

        public Builder id(GdalIdDto id) {
            this.instance.id(id);
            return this;
        }

        public Builder ids(GdalIdsDto ids) {
            this.instance.ids(ids);
            return this;
        }

        //    public Builder usages(GdalUsagesDto usages) {
        //      this.instance.usages(usages);
        //      return this;
        //    }

        /**
         * returns a built GdalObjectUsageDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalObjectUsageDto build() {
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
