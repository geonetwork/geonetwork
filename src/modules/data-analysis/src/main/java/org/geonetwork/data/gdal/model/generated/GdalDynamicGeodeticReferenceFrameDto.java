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
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import org.openapitools.jackson.nullable.JsonNullable;

/** GdalDynamicGeodeticReferenceFrameDto */
@JsonTypeName("dynamic_geodetic_reference_frame")
@JacksonXmlRootElement(localName = "GdalDynamicGeodeticReferenceFrameDto")
@XmlRootElement(name = "GdalDynamicGeodeticReferenceFrameDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalDynamicGeodeticReferenceFrameDto implements GdalDatumDto, GdalGdalGeodeticCrsDatumDtoOneOfDto {

    /** Gets or Sets type */
    public enum TypeEnum {
        DYNAMIC_GEODETIC_REFERENCE_FRAME("DynamicGeodeticReferenceFrame");

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

    private JsonNullable<Object> name = JsonNullable.<Object>undefined();

    private JsonNullable<Object> anchor = JsonNullable.<Object>undefined();

    private JsonNullable<Object> ellipsoid = JsonNullable.<Object>undefined();

    private JsonNullable<Object> primeMeridian = JsonNullable.<Object>undefined();

    private BigDecimal frameReferenceEpoch;

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

    public GdalDynamicGeodeticReferenceFrameDto() {
        super();
    }

    /** Constructor with only required parameters */
    public GdalDynamicGeodeticReferenceFrameDto(Object name, Object ellipsoid, BigDecimal frameReferenceEpoch) {
        this.name = JsonNullable.of(name);
        this.ellipsoid = JsonNullable.of(ellipsoid);
        this.frameReferenceEpoch = frameReferenceEpoch;
    }

    public GdalDynamicGeodeticReferenceFrameDto type(TypeEnum type) {
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

    public GdalDynamicGeodeticReferenceFrameDto name(Object name) {
        this.name = JsonNullable.of(name);
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
    public JsonNullable<Object> getName() {
        return name;
    }

    public void setName(JsonNullable<Object> name) {
        this.name = name;
    }

    public GdalDynamicGeodeticReferenceFrameDto anchor(Object anchor) {
        this.anchor = JsonNullable.of(anchor);
        return this;
    }

    /**
     * Get anchor
     *
     * @return anchor
     */
    @Schema(name = "anchor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("anchor")
    @JacksonXmlProperty(localName = "anchor")
    @XmlElement(name = "anchor")
    public JsonNullable<Object> getAnchor() {
        return anchor;
    }

    public void setAnchor(JsonNullable<Object> anchor) {
        this.anchor = anchor;
    }

    public GdalDynamicGeodeticReferenceFrameDto ellipsoid(Object ellipsoid) {
        this.ellipsoid = JsonNullable.of(ellipsoid);
        return this;
    }

    /**
     * Get ellipsoid
     *
     * @return ellipsoid
     */
    @NotNull
    @Schema(name = "ellipsoid", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("ellipsoid")
    @JacksonXmlProperty(localName = "ellipsoid")
    @XmlElement(name = "ellipsoid")
    public JsonNullable<Object> getEllipsoid() {
        return ellipsoid;
    }

    public void setEllipsoid(JsonNullable<Object> ellipsoid) {
        this.ellipsoid = ellipsoid;
    }

    public GdalDynamicGeodeticReferenceFrameDto primeMeridian(Object primeMeridian) {
        this.primeMeridian = JsonNullable.of(primeMeridian);
        return this;
    }

    /**
     * Get primeMeridian
     *
     * @return primeMeridian
     */
    @Schema(name = "prime_meridian", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("prime_meridian")
    @JacksonXmlProperty(localName = "prime_meridian")
    @XmlElement(name = "prime_meridian")
    public JsonNullable<Object> getPrimeMeridian() {
        return primeMeridian;
    }

    public void setPrimeMeridian(JsonNullable<Object> primeMeridian) {
        this.primeMeridian = primeMeridian;
    }

    public GdalDynamicGeodeticReferenceFrameDto frameReferenceEpoch(BigDecimal frameReferenceEpoch) {
        this.frameReferenceEpoch = frameReferenceEpoch;
        return this;
    }

    /**
     * Get frameReferenceEpoch
     *
     * @return frameReferenceEpoch
     */
    @NotNull
    @Valid
    @Schema(name = "frame_reference_epoch", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("frame_reference_epoch")
    @JacksonXmlProperty(localName = "frame_reference_epoch")
    @XmlElement(name = "frame_reference_epoch")
    public BigDecimal getFrameReferenceEpoch() {
        return frameReferenceEpoch;
    }

    public void setFrameReferenceEpoch(BigDecimal frameReferenceEpoch) {
        this.frameReferenceEpoch = frameReferenceEpoch;
    }

    public GdalDynamicGeodeticReferenceFrameDto $schema(Object $schema) {
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

    public GdalDynamicGeodeticReferenceFrameDto scope(Object scope) {
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

    public GdalDynamicGeodeticReferenceFrameDto area(Object area) {
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

    public GdalDynamicGeodeticReferenceFrameDto bbox(Object bbox) {
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

    public GdalDynamicGeodeticReferenceFrameDto verticalExtent(Object verticalExtent) {
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

    public GdalDynamicGeodeticReferenceFrameDto temporalExtent(Object temporalExtent) {
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

    public GdalDynamicGeodeticReferenceFrameDto usages(Object usages) {
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

    public GdalDynamicGeodeticReferenceFrameDto remarks(Object remarks) {
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

    public GdalDynamicGeodeticReferenceFrameDto id(Object id) {
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

    public GdalDynamicGeodeticReferenceFrameDto ids(Object ids) {
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
        GdalDynamicGeodeticReferenceFrameDto dynamicGeodeticReferenceFrame = (GdalDynamicGeodeticReferenceFrameDto) o;
        return Objects.equals(this.type, dynamicGeodeticReferenceFrame.type)
                && Objects.equals(this.name, dynamicGeodeticReferenceFrame.name)
                && equalsNullable(this.anchor, dynamicGeodeticReferenceFrame.anchor)
                && Objects.equals(this.ellipsoid, dynamicGeodeticReferenceFrame.ellipsoid)
                && equalsNullable(this.primeMeridian, dynamicGeodeticReferenceFrame.primeMeridian)
                && Objects.equals(this.frameReferenceEpoch, dynamicGeodeticReferenceFrame.frameReferenceEpoch)
                && equalsNullable(this.$schema, dynamicGeodeticReferenceFrame.$schema)
                && equalsNullable(this.scope, dynamicGeodeticReferenceFrame.scope)
                && equalsNullable(this.area, dynamicGeodeticReferenceFrame.area)
                && equalsNullable(this.bbox, dynamicGeodeticReferenceFrame.bbox)
                && equalsNullable(this.verticalExtent, dynamicGeodeticReferenceFrame.verticalExtent)
                && equalsNullable(this.temporalExtent, dynamicGeodeticReferenceFrame.temporalExtent)
                && equalsNullable(this.usages, dynamicGeodeticReferenceFrame.usages)
                && equalsNullable(this.remarks, dynamicGeodeticReferenceFrame.remarks)
                && equalsNullable(this.id, dynamicGeodeticReferenceFrame.id)
                && equalsNullable(this.ids, dynamicGeodeticReferenceFrame.ids);
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
                hashCodeNullable(anchor),
                ellipsoid,
                hashCodeNullable(primeMeridian),
                frameReferenceEpoch,
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
        sb.append("class GdalDynamicGeodeticReferenceFrameDto {\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    anchor: ").append(toIndentedString(anchor)).append("\n");
        sb.append("    ellipsoid: ").append(toIndentedString(ellipsoid)).append("\n");
        sb.append("    primeMeridian: ").append(toIndentedString(primeMeridian)).append("\n");
        sb.append("    frameReferenceEpoch: ")
                .append(toIndentedString(frameReferenceEpoch))
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

        private GdalDynamicGeodeticReferenceFrameDto instance;

        public Builder() {
            this(new GdalDynamicGeodeticReferenceFrameDto());
        }

        protected Builder(GdalDynamicGeodeticReferenceFrameDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalDynamicGeodeticReferenceFrameDto value) {
            this.instance.setType(value.type);
            this.instance.setName(value.name);
            this.instance.setAnchor(value.anchor);
            this.instance.setEllipsoid(value.ellipsoid);
            this.instance.setPrimeMeridian(value.primeMeridian);
            this.instance.setFrameReferenceEpoch(value.frameReferenceEpoch);
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

        public Builder name(Object name) {
            this.instance.name(name);
            return this;
        }

        public Builder name(JsonNullable<Object> name) {
            this.instance.name = name;
            return this;
        }

        public Builder anchor(Object anchor) {
            this.instance.anchor(anchor);
            return this;
        }

        public Builder anchor(JsonNullable<Object> anchor) {
            this.instance.anchor = anchor;
            return this;
        }

        public Builder ellipsoid(Object ellipsoid) {
            this.instance.ellipsoid(ellipsoid);
            return this;
        }

        public Builder ellipsoid(JsonNullable<Object> ellipsoid) {
            this.instance.ellipsoid = ellipsoid;
            return this;
        }

        public Builder primeMeridian(Object primeMeridian) {
            this.instance.primeMeridian(primeMeridian);
            return this;
        }

        public Builder primeMeridian(JsonNullable<Object> primeMeridian) {
            this.instance.primeMeridian = primeMeridian;
            return this;
        }

        public Builder frameReferenceEpoch(BigDecimal frameReferenceEpoch) {
            this.instance.frameReferenceEpoch(frameReferenceEpoch);
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
         * returns a built GdalDynamicGeodeticReferenceFrameDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalDynamicGeodeticReferenceFrameDto build() {
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
