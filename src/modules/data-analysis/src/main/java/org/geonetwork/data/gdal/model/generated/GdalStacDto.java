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

/**
 * Derived from
 * https://raw.githubusercontent.com/stac-extensions/projection/main/json-schema/schema.json#/definitions/fields,
 * https://raw.githubusercontent.com/stac-extensions/eo/v1.1.0/json-schema/schema.json#/definitions/bands and
 * https://raw.githubusercontent.com/stac-extensions/eo/v1.1.0/json-schema/schema.json#/definitions/bands
 */
@Schema(
        name = "stac",
        description =
                "Derived from"
                        + " https://raw.githubusercontent.com/stac-extensions/projection/main/json-schema/schema.json#/definitions/fields,"
                        + " https://raw.githubusercontent.com/stac-extensions/eo/v1.1.0/json-schema/schema.json#/definitions/bands"
                        + " and https://raw.githubusercontent.com/stac-extensions/eo/v1.1.0/json-schema/schema.json#/definitions/bands")
@JsonTypeName("stac")
@JacksonXmlRootElement(localName = "GdalStacDto")
@XmlRootElement(name = "GdalStacDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalStacDto {

    private JsonNullable<Integer> projColonEpsg = JsonNullable.<Integer>undefined();

    private JsonNullable<String> projColonWkt2 = JsonNullable.<String>undefined();

    private JsonNullable<GdalProjjsonschemaDto> projColonProjjson = JsonNullable.<GdalProjjsonschemaDto>undefined();

    @Valid
    private List<Integer> projColonShape = new ArrayList<>();

    @Valid
    private List<BigDecimal> projColonTransform = new ArrayList<>();

    private GdalBandsDto eoColonBands = new GdalBandsDto();

    private GdalBandsDto rasterColonBands = new GdalBandsDto();

    public GdalStacDto projColonEpsg(Integer projColonEpsg) {
        this.projColonEpsg = JsonNullable.of(projColonEpsg);
        return this;
    }

    /**
     * Get projColonEpsg
     *
     * @return projColonEpsg
     */
    @Schema(name = "proj:epsg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("proj:epsg")
    @JacksonXmlProperty(localName = "proj:epsg")
    @XmlElement(name = "proj:epsg")
    public JsonNullable<Integer> getProjColonEpsg() {
        return projColonEpsg;
    }

    public void setProjColonEpsg(JsonNullable<Integer> projColonEpsg) {
        this.projColonEpsg = projColonEpsg;
    }

    public GdalStacDto projColonWkt2(String projColonWkt2) {
        this.projColonWkt2 = JsonNullable.of(projColonWkt2);
        return this;
    }

    /**
     * Get projColonWkt2
     *
     * @return projColonWkt2
     */
    @Schema(name = "proj:wkt2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("proj:wkt2")
    @JacksonXmlProperty(localName = "proj:wkt2")
    @XmlElement(name = "proj:wkt2")
    public JsonNullable<String> getProjColonWkt2() {
        return projColonWkt2;
    }

    public void setProjColonWkt2(JsonNullable<String> projColonWkt2) {
        this.projColonWkt2 = projColonWkt2;
    }

    public GdalStacDto projColonProjjson(GdalProjjsonschemaDto projColonProjjson) {
        this.projColonProjjson = JsonNullable.of(projColonProjjson);
        return this;
    }

    /**
     * Get projColonProjjson
     *
     * @return projColonProjjson
     */
    //  FIXME? java.lang.RuntimeException:
    // com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of
    // `org.geonetwork.data.gdal.model.generated.GdalProjjsonschemaDto` (no Creators, like default
    // constructor, exist): abstract types either need to be mapped to concrete types, have custom
    // deserializer, or contain additional type information
    //   @Valid
    //  @Schema(name = "proj:projjson", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //  @JsonProperty("proj:projjson")
    //  @JacksonXmlProperty(localName = "proj:projjson")
    //  @XmlElement(name = "proj:projjson")
    //  public JsonNullable<GdalProjjsonschemaDto> getProjColonProjjson() {
    //    return projColonProjjson;
    //  }
    //
    //  public void setProjColonProjjson(JsonNullable<GdalProjjsonschemaDto> projColonProjjson) {
    //    this.projColonProjjson = projColonProjjson;
    //  }

    public GdalStacDto projColonShape(List<Integer> projColonShape) {
        this.projColonShape = projColonShape;
        return this;
    }

    public GdalStacDto addProjColonShapeItem(Integer projColonShapeItem) {
        if (this.projColonShape == null) {
            this.projColonShape = new ArrayList<>();
        }
        this.projColonShape.add(projColonShapeItem);
        return this;
    }

    /**
     * note that the order of items in proj:shape is height,width starting with GDAL 3.8.5 (previous versions ordered it
     * wrongly as width,height)
     *
     * @return projColonShape
     */
    @Size(min = 2, max = 2)
    @Schema(
            name = "proj:shape",
            description = "note that the order of items in proj:shape is height,width starting with GDAL 3.8.5"
                    + " (previous versions ordered it wrongly as width,height)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("proj:shape")
    @JacksonXmlProperty(localName = "proj:shape")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "proj:shape")
    public List<Integer> getProjColonShape() {
        return projColonShape;
    }

    public void setProjColonShape(List<Integer> projColonShape) {
        this.projColonShape = projColonShape;
    }

    public GdalStacDto projColonTransform(List<BigDecimal> projColonTransform) {
        this.projColonTransform = projColonTransform;
        return this;
    }

    public GdalStacDto addProjColonTransformItem(BigDecimal projColonTransformItem) {
        if (this.projColonTransform == null) {
            this.projColonTransform = new ArrayList<>();
        }
        this.projColonTransform.add(projColonTransformItem);
        return this;
    }

    /**
     * Get projColonTransform
     *
     * @return projColonTransform
     */
    @Valid
    @Schema(name = "proj:transform", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("proj:transform")
    @JacksonXmlProperty(localName = "proj:transform")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "proj:transform")
    public List<BigDecimal> getProjColonTransform() {
        return projColonTransform;
    }

    public void setProjColonTransform(List<BigDecimal> projColonTransform) {
        this.projColonTransform = projColonTransform;
    }

    public GdalStacDto eoColonBands(GdalBandsDto eoColonBands) {
        this.eoColonBands = eoColonBands;
        return this;
    }

    /**
     * Get eoColonBands
     *
     * @return eoColonBands
     */
    @Valid
    @Schema(name = "eo:bands", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("eo:bands")
    @JacksonXmlProperty(localName = "eo:bands")
    @XmlElement(name = "eo:bands")
    public GdalBandsDto getEoColonBands() {
        return eoColonBands;
    }

    public void setEoColonBands(GdalBandsDto eoColonBands) {
        this.eoColonBands = eoColonBands;
    }

    public GdalStacDto rasterColonBands(GdalBandsDto rasterColonBands) {
        this.rasterColonBands = rasterColonBands;
        return this;
    }

    /**
     * Get rasterColonBands
     *
     * @return rasterColonBands
     */
    @Valid
    @Schema(name = "raster:bands", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("raster:bands")
    @JacksonXmlProperty(localName = "raster:bands")
    @XmlElement(name = "raster:bands")
    public GdalBandsDto getRasterColonBands() {
        return rasterColonBands;
    }

    public void setRasterColonBands(GdalBandsDto rasterColonBands) {
        this.rasterColonBands = rasterColonBands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalStacDto stac = (GdalStacDto) o;
        return equalsNullable(this.projColonEpsg, stac.projColonEpsg)
                && equalsNullable(this.projColonWkt2, stac.projColonWkt2)
                && equalsNullable(this.projColonProjjson, stac.projColonProjjson)
                && Objects.equals(this.projColonShape, stac.projColonShape)
                && Objects.equals(this.projColonTransform, stac.projColonTransform)
                && Objects.equals(this.eoColonBands, stac.eoColonBands)
                && Objects.equals(this.rasterColonBands, stac.rasterColonBands);
    }

    private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
        return a == b
                || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                hashCodeNullable(projColonEpsg),
                hashCodeNullable(projColonWkt2),
                hashCodeNullable(projColonProjjson),
                projColonShape,
                projColonTransform,
                eoColonBands,
                rasterColonBands);
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
        sb.append("class GdalStacDto {\n");
        sb.append("    projColonEpsg: ").append(toIndentedString(projColonEpsg)).append("\n");
        sb.append("    projColonWkt2: ").append(toIndentedString(projColonWkt2)).append("\n");
        sb.append("    projColonProjjson: ")
                .append(toIndentedString(projColonProjjson))
                .append("\n");
        sb.append("    projColonShape: ")
                .append(toIndentedString(projColonShape))
                .append("\n");
        sb.append("    projColonTransform: ")
                .append(toIndentedString(projColonTransform))
                .append("\n");
        sb.append("    eoColonBands: ").append(toIndentedString(eoColonBands)).append("\n");
        sb.append("    rasterColonBands: ")
                .append(toIndentedString(rasterColonBands))
                .append("\n");
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

        private GdalStacDto instance;

        public Builder() {
            this(new GdalStacDto());
        }

        protected Builder(GdalStacDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalStacDto value) {
            this.instance.setProjColonEpsg(value.projColonEpsg);
            this.instance.setProjColonWkt2(value.projColonWkt2);
            //      this.instance.setProjColonProjjson(value.projColonProjjson);
            this.instance.setProjColonShape(value.projColonShape);
            this.instance.setProjColonTransform(value.projColonTransform);
            this.instance.setEoColonBands(value.eoColonBands);
            this.instance.setRasterColonBands(value.rasterColonBands);
            return this;
        }

        public Builder projColonEpsg(Integer projColonEpsg) {
            this.instance.projColonEpsg(projColonEpsg);
            return this;
        }

        public Builder projColonEpsg(JsonNullable<Integer> projColonEpsg) {
            this.instance.projColonEpsg = projColonEpsg;
            return this;
        }

        public Builder projColonWkt2(String projColonWkt2) {
            this.instance.projColonWkt2(projColonWkt2);
            return this;
        }

        public Builder projColonWkt2(JsonNullable<String> projColonWkt2) {
            this.instance.projColonWkt2 = projColonWkt2;
            return this;
        }

        public Builder projColonProjjson(GdalProjjsonschemaDto projColonProjjson) {
            this.instance.projColonProjjson(projColonProjjson);
            return this;
        }

        public Builder projColonProjjson(JsonNullable<GdalProjjsonschemaDto> projColonProjjson) {
            this.instance.projColonProjjson = projColonProjjson;
            return this;
        }

        public Builder projColonShape(List<Integer> projColonShape) {
            this.instance.projColonShape(projColonShape);
            return this;
        }

        public Builder projColonTransform(List<BigDecimal> projColonTransform) {
            this.instance.projColonTransform(projColonTransform);
            return this;
        }

        public Builder eoColonBands(GdalBandsDto eoColonBands) {
            this.instance.eoColonBands(eoColonBands);
            return this;
        }

        public Builder rasterColonBands(GdalBandsDto rasterColonBands) {
            this.instance.rasterColonBands(rasterColonBands);
            return this;
        }

        /**
         * returns a built GdalStacDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalStacDto build() {
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
