/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.util.Arrays;
import java.util.Objects;
import org.openapitools.jackson.nullable.JsonNullable;

/** GdalProjectedCrsDto */
@JsonTypeName("projected_crs")
@JacksonXmlRootElement(localName = "GdalProjectedCrsDto")
@XmlRootElement(name = "GdalProjectedCrsDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalProjectedCrsDto implements GdalCrsDto {

    /** Gets or Sets type */
    public enum TypeEnum {
        PROJECTED_CRS("ProjectedCRS");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
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
    }

    private TypeEnum type;

    private String name;

    private Object baseCrs = null;

    private Object conversion = null;

    private Object coordinateSystem = null;

    private JsonNullable<Object> $schema = JsonNullable.<Object>undefined();

    private JsonNullable<Object> scope = JsonNullable.<Object>undefined();

    private JsonNullable<Object> area = JsonNullable.<Object>undefined();

    private JsonNullable<Object> bbox = JsonNullable.<Object>undefined();

    private JsonNullable<Object> verticalExtent = JsonNullable.<Object>undefined();

    private JsonNullable<Object> temporalExtent = JsonNullable.<Object>undefined();

    private JsonNullable<Object> usages = JsonNullable.<Object>undefined();

    private JsonNullable<Object> remarks = JsonNullable.<Object>undefined();

    private JsonNullable<Object> id = JsonNullable.<Object>undefined();

    private JsonNullable<Object> ids = JsonNullable.<Object>undefined();

    public GdalProjectedCrsDto() {
        super();
    }

    /** Constructor with only required parameters */
    public GdalProjectedCrsDto(String name, Object baseCrs, Object conversion, Object coordinateSystem) {
        this.name = name;
        this.baseCrs = baseCrs;
        this.conversion = conversion;
        this.coordinateSystem = coordinateSystem;
    }

    public GdalProjectedCrsDto type(TypeEnum type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     */
    @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("type")
    @JacksonXmlProperty(localName = "type")
    @XmlElement(name = "type")
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public GdalProjectedCrsDto name(String name) {
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

    public GdalProjectedCrsDto baseCrs(Object baseCrs) {
        this.baseCrs = baseCrs;
        return this;
    }

    /**
     * Get baseCrs
     *
     * @return baseCrs
     */
    @NotNull
    @Schema(name = "base_crs", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("base_crs")
    @JacksonXmlProperty(localName = "base_crs")
    @XmlElement(name = "base_crs")
    public Object getBaseCrs() {
        return baseCrs;
    }

    public void setBaseCrs(Object baseCrs) {
        this.baseCrs = baseCrs;
    }

    public GdalProjectedCrsDto conversion(Object conversion) {
        this.conversion = conversion;
        return this;
    }

    /**
     * Get conversion
     *
     * @return conversion
     */
    @NotNull
    @Schema(name = "conversion", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("conversion")
    @JacksonXmlProperty(localName = "conversion")
    @XmlElement(name = "conversion")
    public Object getConversion() {
        return conversion;
    }

    public void setConversion(Object conversion) {
        this.conversion = conversion;
    }

    public GdalProjectedCrsDto coordinateSystem(Object coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
        return this;
    }

    /**
     * Get coordinateSystem
     *
     * @return coordinateSystem
     */
    @NotNull
    @Schema(name = "coordinate_system", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("coordinate_system")
    @JacksonXmlProperty(localName = "coordinate_system")
    @XmlElement(name = "coordinate_system")
    public Object getCoordinateSystem() {
        return coordinateSystem;
    }

    public void setCoordinateSystem(Object coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    public GdalProjectedCrsDto $schema(Object $schema) {
        this.$schema = JsonNullable.of($schema);
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
    public JsonNullable<Object> get$Schema() {
        return $schema;
    }

    public void set$Schema(JsonNullable<Object> $schema) {
        this.$schema = $schema;
    }

    public GdalProjectedCrsDto scope(Object scope) {
        this.scope = JsonNullable.of(scope);
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
    public JsonNullable<Object> getScope() {
        return scope;
    }

    public void setScope(JsonNullable<Object> scope) {
        this.scope = scope;
    }

    public GdalProjectedCrsDto area(Object area) {
        this.area = JsonNullable.of(area);
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
    public JsonNullable<Object> getArea() {
        return area;
    }

    public void setArea(JsonNullable<Object> area) {
        this.area = area;
    }

    public GdalProjectedCrsDto bbox(Object bbox) {
        this.bbox = JsonNullable.of(bbox);
        return this;
    }

    /**
     * Get bbox
     *
     * @return bbox
     */
    @Schema(name = "bbox", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("bbox")
    @JacksonXmlProperty(localName = "bbox")
    @XmlElement(name = "bbox")
    public JsonNullable<Object> getBbox() {
        return bbox;
    }

    public void setBbox(JsonNullable<Object> bbox) {
        this.bbox = bbox;
    }

    public GdalProjectedCrsDto verticalExtent(Object verticalExtent) {
        this.verticalExtent = JsonNullable.of(verticalExtent);
        return this;
    }

    /**
     * Get verticalExtent
     *
     * @return verticalExtent
     */
    @Schema(name = "vertical_extent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("vertical_extent")
    @JacksonXmlProperty(localName = "vertical_extent")
    @XmlElement(name = "vertical_extent")
    public JsonNullable<Object> getVerticalExtent() {
        return verticalExtent;
    }

    public void setVerticalExtent(JsonNullable<Object> verticalExtent) {
        this.verticalExtent = verticalExtent;
    }

    public GdalProjectedCrsDto temporalExtent(Object temporalExtent) {
        this.temporalExtent = JsonNullable.of(temporalExtent);
        return this;
    }

    /**
     * Get temporalExtent
     *
     * @return temporalExtent
     */
    @Schema(name = "temporal_extent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("temporal_extent")
    @JacksonXmlProperty(localName = "temporal_extent")
    @XmlElement(name = "temporal_extent")
    public JsonNullable<Object> getTemporalExtent() {
        return temporalExtent;
    }

    public void setTemporalExtent(JsonNullable<Object> temporalExtent) {
        this.temporalExtent = temporalExtent;
    }

    public GdalProjectedCrsDto usages(Object usages) {
        this.usages = JsonNullable.of(usages);
        return this;
    }

    /**
     * Get usages
     *
     * @return usages
     */
    @Schema(name = "usages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("usages")
    @JacksonXmlProperty(localName = "usages")
    @XmlElement(name = "usages")
    public JsonNullable<Object> getUsages() {
        return usages;
    }

    public void setUsages(JsonNullable<Object> usages) {
        this.usages = usages;
    }

    public GdalProjectedCrsDto remarks(Object remarks) {
        this.remarks = JsonNullable.of(remarks);
        return this;
    }

    /**
     * Get remarks
     *
     * @return remarks
     */
    @Schema(name = "remarks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("remarks")
    @JacksonXmlProperty(localName = "remarks")
    @XmlElement(name = "remarks")
    public JsonNullable<Object> getRemarks() {
        return remarks;
    }

    public void setRemarks(JsonNullable<Object> remarks) {
        this.remarks = remarks;
    }

    public GdalProjectedCrsDto id(Object id) {
        this.id = JsonNullable.of(id);
        return this;
    }

    /**
     * Get id
     *
     * @return id
     */
    @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    @XmlElement(name = "id")
    public JsonNullable<Object> getId() {
        return id;
    }

    public void setId(JsonNullable<Object> id) {
        this.id = id;
    }

    public GdalProjectedCrsDto ids(Object ids) {
        this.ids = JsonNullable.of(ids);
        return this;
    }

    /**
     * Get ids
     *
     * @return ids
     */
    @Schema(name = "ids", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("ids")
    @JacksonXmlProperty(localName = "ids")
    @XmlElement(name = "ids")
    public JsonNullable<Object> getIds() {
        return ids;
    }

    public void setIds(JsonNullable<Object> ids) {
        this.ids = ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalProjectedCrsDto projectedCrs = (GdalProjectedCrsDto) o;
        return Objects.equals(this.type, projectedCrs.type)
                && Objects.equals(this.name, projectedCrs.name)
                && Objects.equals(this.baseCrs, projectedCrs.baseCrs)
                && Objects.equals(this.conversion, projectedCrs.conversion)
                && Objects.equals(this.coordinateSystem, projectedCrs.coordinateSystem)
                && equalsNullable(this.$schema, projectedCrs.$schema)
                && equalsNullable(this.scope, projectedCrs.scope)
                && equalsNullable(this.area, projectedCrs.area)
                && equalsNullable(this.bbox, projectedCrs.bbox)
                && equalsNullable(this.verticalExtent, projectedCrs.verticalExtent)
                && equalsNullable(this.temporalExtent, projectedCrs.temporalExtent)
                && equalsNullable(this.usages, projectedCrs.usages)
                && equalsNullable(this.remarks, projectedCrs.remarks)
                && equalsNullable(this.id, projectedCrs.id)
                && equalsNullable(this.ids, projectedCrs.ids);
    }

    private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
        return a == b
                || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                type,
                name,
                baseCrs,
                conversion,
                coordinateSystem,
                hashCodeNullable($schema),
                hashCodeNullable(scope),
                hashCodeNullable(area),
                hashCodeNullable(bbox),
                hashCodeNullable(verticalExtent),
                hashCodeNullable(temporalExtent),
                hashCodeNullable(usages),
                hashCodeNullable(remarks),
                hashCodeNullable(id),
                hashCodeNullable(ids));
    }

    private static <T> int hashCodeNullable(JsonNullable<T> a) {
        if (a == null) {
            return 1;
        }
        return a.isPresent() ? Arrays.deepHashCode(new Object[] {a.get()}) : 31;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalProjectedCrsDto {\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    baseCrs: ").append(toIndentedString(baseCrs)).append("\n");
        sb.append("    conversion: ").append(toIndentedString(conversion)).append("\n");
        sb.append("    coordinateSystem: ")
                .append(toIndentedString(coordinateSystem))
                .append("\n");
        sb.append("    $schema: ").append(toIndentedString($schema)).append("\n");
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        sb.append("    area: ").append(toIndentedString(area)).append("\n");
        sb.append("    bbox: ").append(toIndentedString(bbox)).append("\n");
        sb.append("    verticalExtent: ")
                .append(toIndentedString(verticalExtent))
                .append("\n");
        sb.append("    temporalExtent: ")
                .append(toIndentedString(temporalExtent))
                .append("\n");
        sb.append("    usages: ").append(toIndentedString(usages)).append("\n");
        sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    ids: ").append(toIndentedString(ids)).append("\n");
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

        private GdalProjectedCrsDto instance;

        public Builder() {
            this(new GdalProjectedCrsDto());
        }

        protected Builder(GdalProjectedCrsDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalProjectedCrsDto value) {
            this.instance.setType(value.type);
            this.instance.setName(value.name);
            this.instance.setBaseCrs(value.baseCrs);
            this.instance.setConversion(value.conversion);
            this.instance.setCoordinateSystem(value.coordinateSystem);
            this.instance.set$Schema(value.$schema);
            this.instance.setScope(value.scope);
            this.instance.setArea(value.area);
            this.instance.setBbox(value.bbox);
            this.instance.setVerticalExtent(value.verticalExtent);
            this.instance.setTemporalExtent(value.temporalExtent);
            this.instance.setUsages(value.usages);
            this.instance.setRemarks(value.remarks);
            this.instance.setId(value.id);
            this.instance.setIds(value.ids);
            return this;
        }

        public Builder type(TypeEnum type) {
            this.instance.type(type);
            return this;
        }

        public Builder name(String name) {
            this.instance.name(name);
            return this;
        }

        public Builder baseCrs(Object baseCrs) {
            this.instance.baseCrs(baseCrs);
            return this;
        }

        public Builder conversion(Object conversion) {
            this.instance.conversion(conversion);
            return this;
        }

        public Builder coordinateSystem(Object coordinateSystem) {
            this.instance.coordinateSystem(coordinateSystem);
            return this;
        }

        public Builder $schema(Object $schema) {
            this.instance.$schema($schema);
            return this;
        }

        public Builder $schema(JsonNullable<Object> $schema) {
            this.instance.$schema = $schema;
            return this;
        }

        public Builder scope(Object scope) {
            this.instance.scope(scope);
            return this;
        }

        public Builder scope(JsonNullable<Object> scope) {
            this.instance.scope = scope;
            return this;
        }

        public Builder area(Object area) {
            this.instance.area(area);
            return this;
        }

        public Builder area(JsonNullable<Object> area) {
            this.instance.area = area;
            return this;
        }

        public Builder bbox(Object bbox) {
            this.instance.bbox(bbox);
            return this;
        }

        public Builder bbox(JsonNullable<Object> bbox) {
            this.instance.bbox = bbox;
            return this;
        }

        public Builder verticalExtent(Object verticalExtent) {
            this.instance.verticalExtent(verticalExtent);
            return this;
        }

        public Builder verticalExtent(JsonNullable<Object> verticalExtent) {
            this.instance.verticalExtent = verticalExtent;
            return this;
        }

        public Builder temporalExtent(Object temporalExtent) {
            this.instance.temporalExtent(temporalExtent);
            return this;
        }

        public Builder temporalExtent(JsonNullable<Object> temporalExtent) {
            this.instance.temporalExtent = temporalExtent;
            return this;
        }

        public Builder usages(Object usages) {
            this.instance.usages(usages);
            return this;
        }

        public Builder usages(JsonNullable<Object> usages) {
            this.instance.usages = usages;
            return this;
        }

        public Builder remarks(Object remarks) {
            this.instance.remarks(remarks);
            return this;
        }

        public Builder remarks(JsonNullable<Object> remarks) {
            this.instance.remarks = remarks;
            return this;
        }

        public Builder id(Object id) {
            this.instance.id(id);
            return this;
        }

        public Builder id(JsonNullable<Object> id) {
            this.instance.id = id;
            return this;
        }

        public Builder ids(Object ids) {
            this.instance.ids(ids);
            return this;
        }

        public Builder ids(JsonNullable<Object> ids) {
            this.instance.ids = ids;
            return this;
        }

        /**
         * returns a built GdalProjectedCrsDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalProjectedCrsDto build() {
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
