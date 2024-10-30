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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.openapitools.jackson.nullable.JsonNullable;

/** GdalGeometryFieldDto */
@JsonTypeName("geometryField")
@JacksonXmlRootElement(localName = "GdalGeometryFieldDto")
@XmlRootElement(name = "GdalGeometryFieldDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalGeometryFieldDto {

    private String name;

    private JsonNullable<
                    @Pattern(
                            regexp =
                                    "^(Geometry|((Multi)?Point)|((Multi)?LineString)|((Multi)?Polygon)|GeometryCollection|((Multi)?Curve)|((Multi)?Surface)|CircularString|CompoundCurve|CurvePolygon|Tin|PolyhedralSurface|Triangle)[Z]?[M]?$")
                    Object>
            type = JsonNullable.<Object>undefined();

    private Boolean nullable;

    @Valid
    private List<BigDecimal> extent = new ArrayList<>();

    @Valid
    private List<GdalGeometryFieldExtent3DInnerDto> extent3D = new ArrayList<>();

    private JsonNullable<GdalCoordinateSystemDto> coordinateSystem = JsonNullable.<GdalCoordinateSystemDto>undefined();

    @Valid
    private List<GdalGeometryFieldSupportedSRSListInnerDto> supportedSRSList = new ArrayList<>();

    //  private List<GdalGeometryFieldSupportedSRSListInnerOneOfDto> supportedSRSList = new
    // ArrayList<>();

    public GdalGeometryFieldDto() {
        super();
    }

    /** Constructor with only required parameters */
    public GdalGeometryFieldDto(String name, Object type) {
        this.name = name;
        this.type = JsonNullable.of(type);
    }

    public GdalGeometryFieldDto name(String name) {
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

    public GdalGeometryFieldDto type(Object type) {
        this.type = JsonNullable.of(type);
        return this;
    }

    /**
     * Get type
     *
     * @return type
     */
    @NotNull
    @Pattern(
            regexp =
                    "^(Geometry|((Multi)?Point)|((Multi)?LineString)|((Multi)?Polygon)|GeometryCollection|((Multi)?Curve)|((Multi)?Surface)|CircularString|CompoundCurve|CurvePolygon|Tin|PolyhedralSurface|Triangle)[Z]?[M]?$")
    @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("type")
    @JacksonXmlProperty(localName = "type")
    @XmlElement(name = "type")
    public JsonNullable<
                    @Pattern(
                            regexp =
                                    "^(Geometry|((Multi)?Point)|((Multi)?LineString)|((Multi)?Polygon)|GeometryCollection|((Multi)?Curve)|((Multi)?Surface)|CircularString|CompoundCurve|CurvePolygon|Tin|PolyhedralSurface|Triangle)[Z]?[M]?$")
                    Object>
            getType() {
        return type;
    }

    public void setType(JsonNullable<Object> type) {
        this.type = type;
    }

    public GdalGeometryFieldDto nullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    /**
     * Get nullable
     *
     * @return nullable
     */
    @Schema(name = "nullable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("nullable")
    @JacksonXmlProperty(localName = "nullable")
    @XmlElement(name = "nullable")
    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public GdalGeometryFieldDto extent(List<BigDecimal> extent) {
        this.extent = extent;
        return this;
    }

    public GdalGeometryFieldDto addExtentItem(BigDecimal extentItem) {
        if (this.extent == null) {
            this.extent = new ArrayList<>();
        }
        this.extent.add(extentItem);
        return this;
    }

    /**
     * Get extent
     *
     * @return extent
     */
    @Valid
    @Schema(name = "extent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("extent")
    @JacksonXmlProperty(localName = "extent")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "extent")
    public List<BigDecimal> getExtent() {
        return extent;
    }

    public void setExtent(List<BigDecimal> extent) {
        this.extent = extent;
    }

    public GdalGeometryFieldDto extent3D(List<GdalGeometryFieldExtent3DInnerDto> extent3D) {
        this.extent3D = extent3D;
        return this;
    }

    public GdalGeometryFieldDto addExtent3DItem(GdalGeometryFieldExtent3DInnerDto extent3DItem) {
        if (this.extent3D == null) {
            this.extent3D = new ArrayList<>();
        }
        this.extent3D.add(extent3DItem);
        return this;
    }

    /**
     * Get extent3D
     *
     * @return extent3D
     */
    @Valid
    @Schema(name = "extent3D", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("extent3D")
    @JacksonXmlProperty(localName = "extent3D")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "extent3D")
    public List<GdalGeometryFieldExtent3DInnerDto> getExtent3D() {
        return extent3D;
    }

    public void setExtent3D(List<GdalGeometryFieldExtent3DInnerDto> extent3D) {
        this.extent3D = extent3D;
    }

    public GdalGeometryFieldDto coordinateSystem(GdalCoordinateSystemDto coordinateSystem) {
        this.coordinateSystem = JsonNullable.of(coordinateSystem);
        return this;
    }

    /**
     * Get coordinateSystem
     *
     * @return coordinateSystem
     */
    @Valid
    @Schema(name = "coordinateSystem", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("coordinateSystem")
    @JacksonXmlProperty(localName = "coordinateSystem")
    @XmlElement(name = "coordinateSystem")
    public JsonNullable<GdalCoordinateSystemDto> getCoordinateSystem() {
        return coordinateSystem;
    }

    public void setCoordinateSystem(JsonNullable<GdalCoordinateSystemDto> coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    public GdalGeometryFieldDto supportedSRSList(List<GdalGeometryFieldSupportedSRSListInnerDto> supportedSRSList) {
        this.supportedSRSList = supportedSRSList;
        return this;
    }

    public GdalGeometryFieldDto addSupportedSRSListItem(
            GdalGeometryFieldSupportedSRSListInnerDto supportedSRSListItem) {
        if (this.supportedSRSList == null) {
            this.supportedSRSList = new ArrayList<>();
        }
        this.supportedSRSList.add(supportedSRSListItem);
        return this;
    }

    /**
     * Get supportedSRSList
     *
     * @return supportedSRSList
     */
    @Valid
    @Schema(name = "supportedSRSList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("supportedSRSList")
    @JacksonXmlProperty(localName = "supportedSRSList")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "supportedSRSList")
    public List<GdalGeometryFieldSupportedSRSListInnerDto> getSupportedSRSList() {
        return supportedSRSList;
    }

    public void setSupportedSRSList(List<GdalGeometryFieldSupportedSRSListInnerDto> supportedSRSList) {
        this.supportedSRSList = supportedSRSList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalGeometryFieldDto geometryField = (GdalGeometryFieldDto) o;
        return Objects.equals(this.name, geometryField.name)
                && Objects.equals(this.type, geometryField.type)
                && Objects.equals(this.nullable, geometryField.nullable)
                && Objects.equals(this.extent, geometryField.extent)
                && Objects.equals(this.extent3D, geometryField.extent3D)
                && equalsNullable(this.coordinateSystem, geometryField.coordinateSystem)
                && Objects.equals(this.supportedSRSList, geometryField.supportedSRSList);
    }

    private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
        return a == b
                || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name, type, nullable, extent, extent3D, hashCodeNullable(coordinateSystem), supportedSRSList);
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
        sb.append("class GdalGeometryFieldDto {\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    nullable: ").append(toIndentedString(nullable)).append("\n");
        sb.append("    extent: ").append(toIndentedString(extent)).append("\n");
        sb.append("    extent3D: ").append(toIndentedString(extent3D)).append("\n");
        sb.append("    coordinateSystem: ")
                .append(toIndentedString(coordinateSystem))
                .append("\n");
        sb.append("    supportedSRSList: ")
                .append(toIndentedString(supportedSRSList))
                .append("\n");
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

        private GdalGeometryFieldDto instance;

        public Builder() {
            this(new GdalGeometryFieldDto());
        }

        protected Builder(GdalGeometryFieldDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalGeometryFieldDto value) {
            this.instance.setName(value.name);
            this.instance.setType(value.type);
            this.instance.setNullable(value.nullable);
            this.instance.setExtent(value.extent);
            this.instance.setExtent3D(value.extent3D);
            this.instance.setCoordinateSystem(value.coordinateSystem);
            this.instance.setSupportedSRSList(value.supportedSRSList);
            return this;
        }

        public Builder name(String name) {
            this.instance.name(name);
            return this;
        }

        public Builder type(Object type) {
            this.instance.type(type);
            return this;
        }

        public Builder type(JsonNullable<Object> type) {
            this.instance.type = type;
            return this;
        }

        public Builder nullable(Boolean nullable) {
            this.instance.nullable(nullable);
            return this;
        }

        public Builder extent(List<BigDecimal> extent) {
            this.instance.extent(extent);
            return this;
        }

        public Builder extent3D(List<GdalGeometryFieldExtent3DInnerDto> extent3D) {
            this.instance.extent3D(extent3D);
            return this;
        }

        public Builder coordinateSystem(GdalCoordinateSystemDto coordinateSystem) {
            this.instance.coordinateSystem(coordinateSystem);
            return this;
        }

        public Builder coordinateSystem(JsonNullable<GdalCoordinateSystemDto> coordinateSystem) {
            this.instance.coordinateSystem = coordinateSystem;
            return this;
        }

        public Builder supportedSRSList(List<GdalGeometryFieldSupportedSRSListInnerDto> supportedSRSList) {
            this.instance.supportedSRSList(supportedSRSList);
            return this;
        }

        /**
         * returns a built GdalGeometryFieldDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalGeometryFieldDto build() {
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
