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
import java.util.Objects;

/** GdalDomainDto */
@JsonTypeName("domain")
@JacksonXmlRootElement(localName = "GdalDomainDto")
@XmlRootElement(name = "GdalDomainDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class GdalDomainDto {

    private String description;

    /** Gets or Sets type */
    public enum TypeEnum {
        CODED("coded"),

        RANGE("range"),

        GLOB("glob");

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

    private GdalFieldTypeDto fieldType;

    private GdalFieldSubTypeDto fieldSubType;

    private String glob;

    private Object codedValues;

    /** Gets or Sets splitPolicy */
    public enum SplitPolicyEnum {
        DEFAULT_VALUE("default value"),

        DUPLICATE("duplicate"),

        GEOMETRY_RATIO("geometry ratio");

        private String value;

        SplitPolicyEnum(String value) {
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
        public static SplitPolicyEnum fromValue(String value) {
            for (SplitPolicyEnum b : SplitPolicyEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    private SplitPolicyEnum splitPolicy;

    /** Gets or Sets mergePolicy */
    public enum MergePolicyEnum {
        DEFAULT_VALUE("default value"),

        SUM("sum"),

        GEOMETRY_WEIGHTED("geometry weighted");

        private String value;

        MergePolicyEnum(String value) {
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
        public static MergePolicyEnum fromValue(String value) {
            for (MergePolicyEnum b : MergePolicyEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    private MergePolicyEnum mergePolicy;

    private GdalDomainMinValueDto minValue;

    private Boolean minValueIncluded;

    private GdalDomainMinValueDto maxValue;

    private Boolean maxValueIncluded;

    public GdalDomainDto() {
        super();
    }

    /** Constructor with only required parameters */
    public GdalDomainDto(
            TypeEnum type, GdalFieldTypeDto fieldType, SplitPolicyEnum splitPolicy, MergePolicyEnum mergePolicy) {
        this.type = type;
        this.fieldType = fieldType;
        this.splitPolicy = splitPolicy;
        this.mergePolicy = mergePolicy;
    }

    public GdalDomainDto description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     *
     * @return description
     */
    @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("description")
    @JacksonXmlProperty(localName = "description")
    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GdalDomainDto type(TypeEnum type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     */
    @NotNull
    @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("type")
    @JacksonXmlProperty(localName = "type")
    @XmlElement(name = "type")
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public GdalDomainDto fieldType(GdalFieldTypeDto fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    /**
     * Get fieldType
     *
     * @return fieldType
     */
    @NotNull
    @Valid
    @Schema(name = "fieldType", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("fieldType")
    @JacksonXmlProperty(localName = "fieldType")
    @XmlElement(name = "fieldType")
    public GdalFieldTypeDto getFieldType() {
        return fieldType;
    }

    public void setFieldType(GdalFieldTypeDto fieldType) {
        this.fieldType = fieldType;
    }

    public GdalDomainDto fieldSubType(GdalFieldSubTypeDto fieldSubType) {
        this.fieldSubType = fieldSubType;
        return this;
    }

    /**
     * Get fieldSubType
     *
     * @return fieldSubType
     */
    @Valid
    @Schema(name = "fieldSubType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("fieldSubType")
    @JacksonXmlProperty(localName = "fieldSubType")
    @XmlElement(name = "fieldSubType")
    public GdalFieldSubTypeDto getFieldSubType() {
        return fieldSubType;
    }

    public void setFieldSubType(GdalFieldSubTypeDto fieldSubType) {
        this.fieldSubType = fieldSubType;
    }

    public GdalDomainDto glob(String glob) {
        this.glob = glob;
        return this;
    }

    /**
     * only present when type=coded
     *
     * @return glob
     */
    @Schema(
            name = "glob",
            description = "only present when type=coded",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("glob")
    @JacksonXmlProperty(localName = "glob")
    @XmlElement(name = "glob")
    public String getGlob() {
        return glob;
    }

    public void setGlob(String glob) {
        this.glob = glob;
    }

    public GdalDomainDto codedValues(Object codedValues) {
        this.codedValues = codedValues;
        return this;
    }

    /**
     * Get codedValues
     *
     * @return codedValues
     */
    @Schema(name = "codedValues", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("codedValues")
    @JacksonXmlProperty(localName = "codedValues")
    @XmlElement(name = "codedValues")
    public Object getCodedValues() {
        return codedValues;
    }

    public void setCodedValues(Object codedValues) {
        this.codedValues = codedValues;
    }

    public GdalDomainDto splitPolicy(SplitPolicyEnum splitPolicy) {
        this.splitPolicy = splitPolicy;
        return this;
    }

    /**
     * Get splitPolicy
     *
     * @return splitPolicy
     */
    @NotNull
    @Schema(name = "splitPolicy", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("splitPolicy")
    @JacksonXmlProperty(localName = "splitPolicy")
    @XmlElement(name = "splitPolicy")
    public SplitPolicyEnum getSplitPolicy() {
        return splitPolicy;
    }

    public void setSplitPolicy(SplitPolicyEnum splitPolicy) {
        this.splitPolicy = splitPolicy;
    }

    public GdalDomainDto mergePolicy(MergePolicyEnum mergePolicy) {
        this.mergePolicy = mergePolicy;
        return this;
    }

    /**
     * Get mergePolicy
     *
     * @return mergePolicy
     */
    @NotNull
    @Schema(name = "mergePolicy", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("mergePolicy")
    @JacksonXmlProperty(localName = "mergePolicy")
    @XmlElement(name = "mergePolicy")
    public MergePolicyEnum getMergePolicy() {
        return mergePolicy;
    }

    public void setMergePolicy(MergePolicyEnum mergePolicy) {
        this.mergePolicy = mergePolicy;
    }

    public GdalDomainDto minValue(GdalDomainMinValueDto minValue) {
        this.minValue = minValue;
        return this;
    }

    /**
     * Get minValue
     *
     * @return minValue
     */
    @Valid
    @Schema(name = "minValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("minValue")
    @JacksonXmlProperty(localName = "minValue")
    @XmlElement(name = "minValue")
    public GdalDomainMinValueDto getMinValue() {
        return minValue;
    }

    public void setMinValue(GdalDomainMinValueDto minValue) {
        this.minValue = minValue;
    }

    public GdalDomainDto minValueIncluded(Boolean minValueIncluded) {
        this.minValueIncluded = minValueIncluded;
        return this;
    }

    /**
     * only present when type=range
     *
     * @return minValueIncluded
     */
    @Schema(
            name = "minValueIncluded",
            description = "only present when type=range",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("minValueIncluded")
    @JacksonXmlProperty(localName = "minValueIncluded")
    @XmlElement(name = "minValueIncluded")
    public Boolean getMinValueIncluded() {
        return minValueIncluded;
    }

    public void setMinValueIncluded(Boolean minValueIncluded) {
        this.minValueIncluded = minValueIncluded;
    }

    public GdalDomainDto maxValue(GdalDomainMinValueDto maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    /**
     * Get maxValue
     *
     * @return maxValue
     */
    @Valid
    @Schema(name = "maxValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("maxValue")
    @JacksonXmlProperty(localName = "maxValue")
    @XmlElement(name = "maxValue")
    public GdalDomainMinValueDto getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(GdalDomainMinValueDto maxValue) {
        this.maxValue = maxValue;
    }

    public GdalDomainDto maxValueIncluded(Boolean maxValueIncluded) {
        this.maxValueIncluded = maxValueIncluded;
        return this;
    }

    /**
     * only present when type=range
     *
     * @return maxValueIncluded
     */
    @Schema(
            name = "maxValueIncluded",
            description = "only present when type=range",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("maxValueIncluded")
    @JacksonXmlProperty(localName = "maxValueIncluded")
    @XmlElement(name = "maxValueIncluded")
    public Boolean getMaxValueIncluded() {
        return maxValueIncluded;
    }

    public void setMaxValueIncluded(Boolean maxValueIncluded) {
        this.maxValueIncluded = maxValueIncluded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdalDomainDto domain = (GdalDomainDto) o;
        return Objects.equals(this.description, domain.description)
                && Objects.equals(this.type, domain.type)
                && Objects.equals(this.fieldType, domain.fieldType)
                && Objects.equals(this.fieldSubType, domain.fieldSubType)
                && Objects.equals(this.glob, domain.glob)
                && Objects.equals(this.codedValues, domain.codedValues)
                && Objects.equals(this.splitPolicy, domain.splitPolicy)
                && Objects.equals(this.mergePolicy, domain.mergePolicy)
                && Objects.equals(this.minValue, domain.minValue)
                && Objects.equals(this.minValueIncluded, domain.minValueIncluded)
                && Objects.equals(this.maxValue, domain.maxValue)
                && Objects.equals(this.maxValueIncluded, domain.maxValueIncluded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                description,
                type,
                fieldType,
                fieldSubType,
                glob,
                codedValues,
                splitPolicy,
                mergePolicy,
                minValue,
                minValueIncluded,
                maxValue,
                maxValueIncluded);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GdalDomainDto {\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    fieldType: ").append(toIndentedString(fieldType)).append("\n");
        sb.append("    fieldSubType: ").append(toIndentedString(fieldSubType)).append("\n");
        sb.append("    glob: ").append(toIndentedString(glob)).append("\n");
        sb.append("    codedValues: ").append(toIndentedString(codedValues)).append("\n");
        sb.append("    splitPolicy: ").append(toIndentedString(splitPolicy)).append("\n");
        sb.append("    mergePolicy: ").append(toIndentedString(mergePolicy)).append("\n");
        sb.append("    minValue: ").append(toIndentedString(minValue)).append("\n");
        sb.append("    minValueIncluded: ")
                .append(toIndentedString(minValueIncluded))
                .append("\n");
        sb.append("    maxValue: ").append(toIndentedString(maxValue)).append("\n");
        sb.append("    maxValueIncluded: ")
                .append(toIndentedString(maxValueIncluded))
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

        private GdalDomainDto instance;

        public Builder() {
            this(new GdalDomainDto());
        }

        protected Builder(GdalDomainDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GdalDomainDto value) {
            this.instance.setDescription(value.description);
            this.instance.setType(value.type);
            this.instance.setFieldType(value.fieldType);
            this.instance.setFieldSubType(value.fieldSubType);
            this.instance.setGlob(value.glob);
            this.instance.setCodedValues(value.codedValues);
            this.instance.setSplitPolicy(value.splitPolicy);
            this.instance.setMergePolicy(value.mergePolicy);
            this.instance.setMinValue(value.minValue);
            this.instance.setMinValueIncluded(value.minValueIncluded);
            this.instance.setMaxValue(value.maxValue);
            this.instance.setMaxValueIncluded(value.maxValueIncluded);
            return this;
        }

        public Builder description(String description) {
            this.instance.description(description);
            return this;
        }

        public Builder type(TypeEnum type) {
            this.instance.type(type);
            return this;
        }

        public Builder fieldType(GdalFieldTypeDto fieldType) {
            this.instance.fieldType(fieldType);
            return this;
        }

        public Builder fieldSubType(GdalFieldSubTypeDto fieldSubType) {
            this.instance.fieldSubType(fieldSubType);
            return this;
        }

        public Builder glob(String glob) {
            this.instance.glob(glob);
            return this;
        }

        public Builder codedValues(Object codedValues) {
            this.instance.codedValues(codedValues);
            return this;
        }

        public Builder splitPolicy(SplitPolicyEnum splitPolicy) {
            this.instance.splitPolicy(splitPolicy);
            return this;
        }

        public Builder mergePolicy(MergePolicyEnum mergePolicy) {
            this.instance.mergePolicy(mergePolicy);
            return this;
        }

        public Builder minValue(GdalDomainMinValueDto minValue) {
            this.instance.minValue(minValue);
            return this;
        }

        public Builder minValueIncluded(Boolean minValueIncluded) {
            this.instance.minValueIncluded(minValueIncluded);
            return this;
        }

        public Builder maxValue(GdalDomainMinValueDto maxValue) {
            this.instance.maxValue(maxValue);
            return this;
        }

        public Builder maxValueIncluded(Boolean maxValueIncluded) {
            this.instance.maxValueIncluded(maxValueIncluded);
            return this;
        }

        /**
         * returns a built GdalDomainDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public GdalDomainDto build() {
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
