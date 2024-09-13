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
import java.math.BigDecimal;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.openapitools.jackson.nullable.JsonNullable;

/** GdalConcatenatedOperationDto */
@JsonTypeName("concatenated_operation")
@JacksonXmlRootElement(localName = "GdalConcatenatedOperationDto")
@XmlRootElement(name = "GdalConcatenatedOperationDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalConcatenatedOperationDto implements GdalProjjsonschemaDto {

  /** Gets or Sets type */
  public enum TypeEnum {
    CONCATENATED_OPERATION("ConcatenatedOperation");

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

  private Object sourceCrs = null;

  private Object targetCrs = null;

  @Valid private List<GdalSingleOperationDto> steps = new ArrayList<>();

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

  private GdalValueAndUnitDto semiMajorAxis = null;

  private GdalValueAndUnitDto semiMinorAxis = null;

  private BigDecimal inverseFlattening;

  private GdalValueAndUnitDto radius = null;

  public GdalConcatenatedOperationDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GdalConcatenatedOperationDto(
      String name, Object sourceCrs, Object targetCrs, List<GdalSingleOperationDto> steps) {
    this.name = name;
    this.sourceCrs = sourceCrs;
    this.targetCrs = targetCrs;
    this.steps = steps;
    this.semiMajorAxis = semiMajorAxis;
    this.semiMinorAxis = semiMinorAxis;
    this.inverseFlattening = inverseFlattening;
    this.radius = radius;
  }

  public GdalConcatenatedOperationDto type(TypeEnum type) {
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

  public GdalConcatenatedOperationDto name(String name) {
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

  public GdalConcatenatedOperationDto sourceCrs(Object sourceCrs) {
    this.sourceCrs = sourceCrs;
    return this;
  }

  /**
   * Get sourceCrs
   *
   * @return sourceCrs
   */
  @NotNull
  @Schema(name = "source_crs", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source_crs")
  @JacksonXmlProperty(localName = "source_crs")
  @XmlElement(name = "source_crs")
  public Object getSourceCrs() {
    return sourceCrs;
  }

  public void setSourceCrs(Object sourceCrs) {
    this.sourceCrs = sourceCrs;
  }

  public GdalConcatenatedOperationDto targetCrs(Object targetCrs) {
    this.targetCrs = targetCrs;
    return this;
  }

  /**
   * Get targetCrs
   *
   * @return targetCrs
   */
  @NotNull
  @Schema(name = "target_crs", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_crs")
  @JacksonXmlProperty(localName = "target_crs")
  @XmlElement(name = "target_crs")
  public Object getTargetCrs() {
    return targetCrs;
  }

  public void setTargetCrs(Object targetCrs) {
    this.targetCrs = targetCrs;
  }

  public GdalConcatenatedOperationDto steps(List<GdalSingleOperationDto> steps) {
    this.steps = steps;
    return this;
  }

  public GdalConcatenatedOperationDto addStepsItem(GdalSingleOperationDto stepsItem) {
    if (this.steps == null) {
      this.steps = new ArrayList<>();
    }
    this.steps.add(stepsItem);
    return this;
  }

  /**
   * Get steps
   *
   * @return steps
   */
  @NotNull
  @Valid
  @Schema(name = "steps", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("steps")
  @JacksonXmlProperty(localName = "steps")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "steps")
  public List<GdalSingleOperationDto> getSteps() {
    return steps;
  }

  public void setSteps(List<GdalSingleOperationDto> steps) {
    this.steps = steps;
  }

  public GdalConcatenatedOperationDto $schema(Object $schema) {
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

  public GdalConcatenatedOperationDto scope(Object scope) {
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

  public GdalConcatenatedOperationDto area(Object area) {
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

  public GdalConcatenatedOperationDto bbox(Object bbox) {
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

  public GdalConcatenatedOperationDto verticalExtent(Object verticalExtent) {
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

  public GdalConcatenatedOperationDto temporalExtent(Object temporalExtent) {
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

  public GdalConcatenatedOperationDto usages(Object usages) {
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

  public GdalConcatenatedOperationDto remarks(Object remarks) {
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

  public GdalConcatenatedOperationDto id(Object id) {
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

  public GdalConcatenatedOperationDto ids(Object ids) {
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

  public GdalConcatenatedOperationDto semiMajorAxis(GdalValueAndUnitDto semiMajorAxis) {
    this.semiMajorAxis = semiMajorAxis;
    return this;
  }

  /**
   * Get semiMajorAxis
   *
   * @return semiMajorAxis
   */
  @NotNull
  @Valid
  @Schema(name = "semi_major_axis", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("semi_major_axis")
  @JacksonXmlProperty(localName = "semi_major_axis")
  @XmlElement(name = "semi_major_axis")
  public GdalValueAndUnitDto getSemiMajorAxis() {
    return semiMajorAxis;
  }

  public void setSemiMajorAxis(GdalValueAndUnitDto semiMajorAxis) {
    this.semiMajorAxis = semiMajorAxis;
  }

  public GdalConcatenatedOperationDto semiMinorAxis(GdalValueAndUnitDto semiMinorAxis) {
    this.semiMinorAxis = semiMinorAxis;
    return this;
  }

  /**
   * Get semiMinorAxis
   *
   * @return semiMinorAxis
   */
  @NotNull
  @Valid
  @Schema(name = "semi_minor_axis", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("semi_minor_axis")
  @JacksonXmlProperty(localName = "semi_minor_axis")
  @XmlElement(name = "semi_minor_axis")
  public GdalValueAndUnitDto getSemiMinorAxis() {
    return semiMinorAxis;
  }

  public void setSemiMinorAxis(GdalValueAndUnitDto semiMinorAxis) {
    this.semiMinorAxis = semiMinorAxis;
  }

  public GdalConcatenatedOperationDto inverseFlattening(BigDecimal inverseFlattening) {
    this.inverseFlattening = inverseFlattening;
    return this;
  }

  /**
   * Get inverseFlattening
   *
   * @return inverseFlattening
   */
  @NotNull
  @Valid
  @Schema(name = "inverse_flattening", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("inverse_flattening")
  @JacksonXmlProperty(localName = "inverse_flattening")
  @XmlElement(name = "inverse_flattening")
  public BigDecimal getInverseFlattening() {
    return inverseFlattening;
  }

  public void setInverseFlattening(BigDecimal inverseFlattening) {
    this.inverseFlattening = inverseFlattening;
  }

  public GdalConcatenatedOperationDto radius(GdalValueAndUnitDto radius) {
    this.radius = radius;
    return this;
  }

  /**
   * Get radius
   *
   * @return radius
   */
  @NotNull
  @Valid
  @Schema(name = "radius", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("radius")
  @JacksonXmlProperty(localName = "radius")
  @XmlElement(name = "radius")
  public GdalValueAndUnitDto getRadius() {
    return radius;
  }

  public void setRadius(GdalValueAndUnitDto radius) {
    this.radius = radius;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalConcatenatedOperationDto concatenatedOperation = (GdalConcatenatedOperationDto) o;
    return Objects.equals(this.type, concatenatedOperation.type)
        && Objects.equals(this.name, concatenatedOperation.name)
        && Objects.equals(this.sourceCrs, concatenatedOperation.sourceCrs)
        && Objects.equals(this.targetCrs, concatenatedOperation.targetCrs)
        && Objects.equals(this.steps, concatenatedOperation.steps)
        && equalsNullable(this.$schema, concatenatedOperation.$schema)
        && equalsNullable(this.scope, concatenatedOperation.scope)
        && equalsNullable(this.area, concatenatedOperation.area)
        && equalsNullable(this.bbox, concatenatedOperation.bbox)
        && equalsNullable(this.verticalExtent, concatenatedOperation.verticalExtent)
        && equalsNullable(this.temporalExtent, concatenatedOperation.temporalExtent)
        && equalsNullable(this.usages, concatenatedOperation.usages)
        && equalsNullable(this.remarks, concatenatedOperation.remarks)
        && equalsNullable(this.id, concatenatedOperation.id)
        && equalsNullable(this.ids, concatenatedOperation.ids)
        && Objects.equals(this.semiMajorAxis, concatenatedOperation.semiMajorAxis)
        && Objects.equals(this.semiMinorAxis, concatenatedOperation.semiMinorAxis)
        && Objects.equals(this.inverseFlattening, concatenatedOperation.inverseFlattening)
        && Objects.equals(this.radius, concatenatedOperation.radius);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b
        || (a != null
            && b != null
            && a.isPresent()
            && b.isPresent()
            && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        type,
        name,
        sourceCrs,
        targetCrs,
        steps,
        hashCodeNullable($schema),
        hashCodeNullable(scope),
        hashCodeNullable(area),
        hashCodeNullable(bbox),
        hashCodeNullable(verticalExtent),
        hashCodeNullable(temporalExtent),
        hashCodeNullable(usages),
        hashCodeNullable(remarks),
        hashCodeNullable(id),
        hashCodeNullable(ids),
        semiMajorAxis,
        semiMinorAxis,
        inverseFlattening,
        radius);
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
    sb.append("class GdalConcatenatedOperationDto {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    sourceCrs: ").append(toIndentedString(sourceCrs)).append("\n");
    sb.append("    targetCrs: ").append(toIndentedString(targetCrs)).append("\n");
    sb.append("    steps: ").append(toIndentedString(steps)).append("\n");
    sb.append("    $schema: ").append(toIndentedString($schema)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    area: ").append(toIndentedString(area)).append("\n");
    sb.append("    bbox: ").append(toIndentedString(bbox)).append("\n");
    sb.append("    verticalExtent: ").append(toIndentedString(verticalExtent)).append("\n");
    sb.append("    temporalExtent: ").append(toIndentedString(temporalExtent)).append("\n");
    sb.append("    usages: ").append(toIndentedString(usages)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    ids: ").append(toIndentedString(ids)).append("\n");
    sb.append("    semiMajorAxis: ").append(toIndentedString(semiMajorAxis)).append("\n");
    sb.append("    semiMinorAxis: ").append(toIndentedString(semiMinorAxis)).append("\n");
    sb.append("    inverseFlattening: ").append(toIndentedString(inverseFlattening)).append("\n");
    sb.append("    radius: ").append(toIndentedString(radius)).append("\n");
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

    private GdalConcatenatedOperationDto instance;

    public Builder() {
      this(new GdalConcatenatedOperationDto());
    }

    protected Builder(GdalConcatenatedOperationDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalConcatenatedOperationDto value) {
      this.instance.setType(value.type);
      this.instance.setName(value.name);
      this.instance.setSourceCrs(value.sourceCrs);
      this.instance.setTargetCrs(value.targetCrs);
      this.instance.setSteps(value.steps);
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
      this.instance.setSemiMajorAxis(value.semiMajorAxis);
      this.instance.setSemiMinorAxis(value.semiMinorAxis);
      this.instance.setInverseFlattening(value.inverseFlattening);
      this.instance.setRadius(value.radius);
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

    public Builder sourceCrs(Object sourceCrs) {
      this.instance.sourceCrs(sourceCrs);
      return this;
    }

    public Builder targetCrs(Object targetCrs) {
      this.instance.targetCrs(targetCrs);
      return this;
    }

    public Builder steps(List<GdalSingleOperationDto> steps) {
      this.instance.steps(steps);
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

    public Builder semiMajorAxis(GdalValueAndUnitDto semiMajorAxis) {
      this.instance.semiMajorAxis(semiMajorAxis);
      return this;
    }

    public Builder semiMinorAxis(GdalValueAndUnitDto semiMinorAxis) {
      this.instance.semiMinorAxis(semiMinorAxis);
      return this;
    }

    public Builder inverseFlattening(BigDecimal inverseFlattening) {
      this.instance.inverseFlattening(inverseFlattening);
      return this;
    }

    public Builder radius(GdalValueAndUnitDto radius) {
      this.instance.radius(radius);
      return this;
    }

    /**
     * returns a built GdalConcatenatedOperationDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalConcatenatedOperationDto build() {
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
