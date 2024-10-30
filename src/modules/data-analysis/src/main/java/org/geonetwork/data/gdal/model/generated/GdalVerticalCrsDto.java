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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.openapitools.jackson.nullable.JsonNullable;

/** One and only one of datum and datum_ensemble must be provided */
@Schema(name = "vertical_crs", description = "One and only one of datum and datum_ensemble must be provided")
@JsonTypeName("vertical_crs")
@JacksonXmlRootElement(localName = "GdalVerticalCrsDto")
@XmlRootElement(name = "GdalVerticalCrsDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalVerticalCrsDto implements GdalCrsDto {

    /** Gets or Sets type */
    public enum TypeEnum {
        VERTICAL_CRS("VerticalCRS");

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

    private JsonNullable<GdalGdalVerticalCrsDatumDtoOneOfDto> datum =
            JsonNullable.<GdalGdalVerticalCrsDatumDtoOneOfDto>undefined();

    private Object datumEnsemble = null;

    private Object coordinateSystem = null;

    private Object geoidModel = null;

    @Valid
    private List<@Valid GdalGeoidModelDto> geoidModels = new ArrayList<>();

    @Valid
    private List<@Valid GdalDeformationModelDto> deformationModels = new ArrayList<>();

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

    public GdalVerticalCrsDto() {
        super();
    }

    /** Constructor with only required parameters */
    public GdalVerticalCrsDto(String name) {
        this.name = name;
    }

    public GdalVerticalCrsDto type(TypeEnum type) {
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

    public GdalVerticalCrsDto name(String name) {
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

    public GdalVerticalCrsDto datum(GdalGdalVerticalCrsDatumDtoOneOfDto datum) {
        this.datum = JsonNullable.of(datum);
        return this;
    }

    /**
     * Get datum
     *
     * @return datum
     */
    @Valid
    @Schema(name = "datum", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("datum")
    @JacksonXmlProperty(localName = "datum")
    @XmlElement(name = "datum")
    public JsonNullable<GdalGdalVerticalCrsDatumDtoOneOfDto> getDatum() {
        return datum;
    }

    public void setDatum(JsonNullable<GdalGdalVerticalCrsDatumDtoOneOfDto> datum) {
        this.datum = datum;
    }

    public GdalVerticalCrsDto datumEnsemble(Object datumEnsemble) {
        this.datumEnsemble = datumEnsemble;
        return this;
    }

    /**
     * Get datumEnsemble
     *
     * @return datumEnsemble
     */
    @Schema(name = "datum_ensemble", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("datum_ensemble")
    @JacksonXmlProperty(localName = "datum_ensemble")
    @XmlElement(name = "datum_ensemble")
    public Object getDatumEnsemble() {
        return datumEnsemble;
    }

    public void setDatumEnsemble(Object datumEnsemble) {
        this.datumEnsemble = datumEnsemble;
    }

    public GdalVerticalCrsDto coordinateSystem(Object coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
        return this;
    }

    /**
     * Get coordinateSystem
     *
     * @return coordinateSystem
     */
    @Schema(name = "coordinate_system", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("coordinate_system")
    @JacksonXmlProperty(localName = "coordinate_system")
    @XmlElement(name = "coordinate_system")
    public Object getCoordinateSystem() {
        return coordinateSystem;
    }

    public void setCoordinateSystem(Object coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    public GdalVerticalCrsDto geoidModel(Object geoidModel) {
        this.geoidModel = geoidModel;
        return this;
    }

    /**
     * Get geoidModel
     *
     * @return geoidModel
     */
    @Schema(name = "geoid_model", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("geoid_model")
    @JacksonXmlProperty(localName = "geoid_model")
    @XmlElement(name = "geoid_model")
    public Object getGeoidModel() {
        return geoidModel;
    }

    public void setGeoidModel(Object geoidModel) {
        this.geoidModel = geoidModel;
    }

    public GdalVerticalCrsDto geoidModels(List<@Valid GdalGeoidModelDto> geoidModels) {
        this.geoidModels = geoidModels;
        return this;
    }

    public GdalVerticalCrsDto addGeoidModelsItem(GdalGeoidModelDto geoidModelsItem) {
        if (this.geoidModels == null) {
            this.geoidModels = new ArrayList<>();
        }
        this.geoidModels.add(geoidModelsItem);
        return this;
    }

    /**
     * Get geoidModels
     *
     * @return geoidModels
     */
    @Valid
    @Schema(name = "geoid_models", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("geoid_models")
    @JacksonXmlProperty(localName = "geoid_models")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "geoid_models")
    public List<@Valid GdalGeoidModelDto> getGeoidModels() {
        return geoidModels;
    }

    public void setGeoidModels(List<@Valid GdalGeoidModelDto> geoidModels) {
        this.geoidModels = geoidModels;
    }

    public GdalVerticalCrsDto deformationModels(List<@Valid GdalDeformationModelDto> deformationModels) {
        this.deformationModels = deformationModels;
        return this;
    }

    public GdalVerticalCrsDto addDeformationModelsItem(GdalDeformationModelDto deformationModelsItem) {
        if (this.deformationModels == null) {
            this.deformationModels = new ArrayList<>();
        }
        this.deformationModels.add(deformationModelsItem);
        return this;
    }

    /**
     * Get deformationModels
     *
     * @return deformationModels
     */
    @Valid
    @Schema(name = "deformation_models", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("deformation_models")
    @JacksonXmlProperty(localName = "deformation_models")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "deformation_models")
    public List<@Valid GdalDeformationModelDto> getDeformationModels() {
        return deformationModels;
    }

    public void setDeformationModels(List<@Valid GdalDeformationModelDto> deformationModels) {
        this.deformationModels = deformationModels;
    }

    public GdalVerticalCrsDto $schema(Object $schema) {
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

    public GdalVerticalCrsDto scope(Object scope) {
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

    public GdalVerticalCrsDto area(Object area) {
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

    public GdalVerticalCrsDto bbox(Object bbox) {
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

    public GdalVerticalCrsDto verticalExtent(Object verticalExtent) {
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

    public GdalVerticalCrsDto temporalExtent(Object temporalExtent) {
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

    public GdalVerticalCrsDto usages(Object usages) {
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

    public GdalVerticalCrsDto remarks(Object remarks) {
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

    public GdalVerticalCrsDto id(Object id) {
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

    public GdalVerticalCrsDto ids(Object ids) {
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
        GdalVerticalCrsDto verticalCrs = (GdalVerticalCrsDto) o;
        return Objects.equals(this.type, verticalCrs.type)
                && Objects.equals(this.name, verticalCrs.name)
                && equalsNullable(this.datum, verticalCrs.datum)
                && Objects.equals(this.datumEnsemble, verticalCrs.datumEnsemble)
                && Objects.equals(this.coordinateSystem, verticalCrs.coordinateSystem)
                && Objects.equals(this.geoidModel, verticalCrs.geoidModel)
                && Objects.equals(this.geoidModels, verticalCrs.geoidModels)
                && Objects.equals(this.deformationModels, verticalCrs.deformationModels)
                && equalsNullable(this.$schema, verticalCrs.$schema)
                && equalsNullable(this.scope, verticalCrs.scope)
                && equalsNullable(this.area, verticalCrs.area)
                && equalsNullable(this.bbox, verticalCrs.bbox)
                && equalsNullable(this.verticalExtent, verticalCrs.verticalExtent)
                && equalsNullable(this.temporalExtent, verticalCrs.temporalExtent)
                && equalsNullable(this.usages, verticalCrs.usages)
                && equalsNullable(this.remarks, verticalCrs.remarks)
                && equalsNullable(this.id, verticalCrs.id)
                && equalsNullable(this.ids, verticalCrs.ids);
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
                hashCodeNullable(datum),
                datumEnsemble,
                coordinateSystem,
                geoidModel,
                geoidModels,
                deformationModels,
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
        sb.append("class GdalVerticalCrsDto {\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    datum: ").append(toIndentedString(datum)).append("\n");
        sb.append("    datumEnsemble: ").append(toIndentedString(datumEnsemble)).append("\n");
        sb.append("    coordinateSystem: ")
                .append(toIndentedString(coordinateSystem))
                .append("\n");
        sb.append("    geoidModel: ").append(toIndentedString(geoidModel)).append("\n");
        sb.append("    geoidModels: ").append(toIndentedString(geoidModels)).append("\n");
        sb.append("    deformationModels: ")
                .append(toIndentedString(deformationModels))
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

        private GdalVerticalCrsDto instance;

        public Builder() {
            this(new GdalVerticalCrsDto());
        }

        protected Builder(GdalVerticalCrsDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalVerticalCrsDto value) {
            this.instance.setType(value.type);
            this.instance.setName(value.name);
            this.instance.setDatum(value.datum);
            this.instance.setDatumEnsemble(value.datumEnsemble);
            this.instance.setCoordinateSystem(value.coordinateSystem);
            this.instance.setGeoidModel(value.geoidModel);
            this.instance.setGeoidModels(value.geoidModels);
            this.instance.setDeformationModels(value.deformationModels);
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

        public Builder datum(GdalGdalVerticalCrsDatumDtoOneOfDto datum) {
            this.instance.datum(datum);
            return this;
        }

        public Builder datum(JsonNullable<GdalGdalVerticalCrsDatumDtoOneOfDto> datum) {
            this.instance.datum = datum;
            return this;
        }

        public Builder datumEnsemble(Object datumEnsemble) {
            this.instance.datumEnsemble(datumEnsemble);
            return this;
        }

        public Builder coordinateSystem(Object coordinateSystem) {
            this.instance.coordinateSystem(coordinateSystem);
            return this;
        }

        public Builder geoidModel(Object geoidModel) {
            this.instance.geoidModel(geoidModel);
            return this;
        }

        public Builder geoidModels(List<@Valid GdalGeoidModelDto> geoidModels) {
            this.instance.geoidModels(geoidModels);
            return this;
        }

        public Builder deformationModels(List<@Valid GdalDeformationModelDto> deformationModels) {
            this.instance.deformationModels(deformationModels);
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
         * returns a built GdalVerticalCrsDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalVerticalCrsDto build() {
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
