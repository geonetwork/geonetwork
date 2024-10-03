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
import java.util.List;
import java.util.Objects;

/** GdalRelationshipDto */
@JsonTypeName("relationship")
@JacksonXmlRootElement(localName = "GdalRelationshipDto")
@XmlRootElement(name = "GdalRelationshipDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class GdalRelationshipDto {

  /** Gets or Sets type */
  public enum TypeEnum {
    COMPOSITE("Composite"),

    ASSOCIATION("Association"),

    AGGREGATION("Aggregation");

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

  private String relatedTableType;

  /** Gets or Sets cardinality */
  public enum CardinalityEnum {
    ONE_TO_ONE("OneToOne"),

    ONE_TO_MANY("OneToMany"),

    MANY_TO_ONE("ManyToOne"),

    MANY_TO_MANY("ManyToMany");

    private String value;

    CardinalityEnum(String value) {
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
    public static CardinalityEnum fromValue(String value) {
      for (CardinalityEnum b : CardinalityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CardinalityEnum cardinality;

  private String leftTableName;

  private String rightTableName;

  @Valid private List<String> leftTableFields = new ArrayList<>();

  @Valid private List<String> rightTableFields = new ArrayList<>();

  private String mappingTableName;

  @Valid private List<String> leftMappingTableFields = new ArrayList<>();

  @Valid private List<String> rightMappingTableFields = new ArrayList<>();

  private String forwardPathLabel;

  private String backwardPathLabel;

  public GdalRelationshipDto() {
    super();
  }

  /** Constructor with only required parameters */
  public GdalRelationshipDto(
      TypeEnum type,
      String relatedTableType,
      CardinalityEnum cardinality,
      String leftTableName,
      String rightTableName,
      List<String> leftTableFields,
      List<String> rightTableFields,
      String forwardPathLabel,
      String backwardPathLabel) {
    this.type = type;
    this.relatedTableType = relatedTableType;
    this.cardinality = cardinality;
    this.leftTableName = leftTableName;
    this.rightTableName = rightTableName;
    this.leftTableFields = leftTableFields;
    this.rightTableFields = rightTableFields;
    this.forwardPathLabel = forwardPathLabel;
    this.backwardPathLabel = backwardPathLabel;
  }

  public GdalRelationshipDto type(TypeEnum type) {
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

  public GdalRelationshipDto relatedTableType(String relatedTableType) {
    this.relatedTableType = relatedTableType;
    return this;
  }

  /**
   * Get relatedTableType
   *
   * @return relatedTableType
   */
  @NotNull
  @Schema(name = "related_table_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("related_table_type")
  @JacksonXmlProperty(localName = "related_table_type")
  @XmlElement(name = "related_table_type")
  public String getRelatedTableType() {
    return relatedTableType;
  }

  public void setRelatedTableType(String relatedTableType) {
    this.relatedTableType = relatedTableType;
  }

  public GdalRelationshipDto cardinality(CardinalityEnum cardinality) {
    this.cardinality = cardinality;
    return this;
  }

  /**
   * Get cardinality
   *
   * @return cardinality
   */
  @NotNull
  @Schema(name = "cardinality", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cardinality")
  @JacksonXmlProperty(localName = "cardinality")
  @XmlElement(name = "cardinality")
  public CardinalityEnum getCardinality() {
    return cardinality;
  }

  public void setCardinality(CardinalityEnum cardinality) {
    this.cardinality = cardinality;
  }

  public GdalRelationshipDto leftTableName(String leftTableName) {
    this.leftTableName = leftTableName;
    return this;
  }

  /**
   * Get leftTableName
   *
   * @return leftTableName
   */
  @NotNull
  @Schema(name = "left_table_name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("left_table_name")
  @JacksonXmlProperty(localName = "left_table_name")
  @XmlElement(name = "left_table_name")
  public String getLeftTableName() {
    return leftTableName;
  }

  public void setLeftTableName(String leftTableName) {
    this.leftTableName = leftTableName;
  }

  public GdalRelationshipDto rightTableName(String rightTableName) {
    this.rightTableName = rightTableName;
    return this;
  }

  /**
   * Get rightTableName
   *
   * @return rightTableName
   */
  @NotNull
  @Schema(name = "right_table_name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("right_table_name")
  @JacksonXmlProperty(localName = "right_table_name")
  @XmlElement(name = "right_table_name")
  public String getRightTableName() {
    return rightTableName;
  }

  public void setRightTableName(String rightTableName) {
    this.rightTableName = rightTableName;
  }

  public GdalRelationshipDto leftTableFields(List<String> leftTableFields) {
    this.leftTableFields = leftTableFields;
    return this;
  }

  public GdalRelationshipDto addLeftTableFieldsItem(String leftTableFieldsItem) {
    if (this.leftTableFields == null) {
      this.leftTableFields = new ArrayList<>();
    }
    this.leftTableFields.add(leftTableFieldsItem);
    return this;
  }

  /**
   * Get leftTableFields
   *
   * @return leftTableFields
   */
  @NotNull
  @Schema(name = "left_table_fields", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("left_table_fields")
  @JacksonXmlProperty(localName = "left_table_fields")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "left_table_fields")
  public List<String> getLeftTableFields() {
    return leftTableFields;
  }

  public void setLeftTableFields(List<String> leftTableFields) {
    this.leftTableFields = leftTableFields;
  }

  public GdalRelationshipDto rightTableFields(List<String> rightTableFields) {
    this.rightTableFields = rightTableFields;
    return this;
  }

  public GdalRelationshipDto addRightTableFieldsItem(String rightTableFieldsItem) {
    if (this.rightTableFields == null) {
      this.rightTableFields = new ArrayList<>();
    }
    this.rightTableFields.add(rightTableFieldsItem);
    return this;
  }

  /**
   * Get rightTableFields
   *
   * @return rightTableFields
   */
  @NotNull
  @Schema(name = "right_table_fields", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("right_table_fields")
  @JacksonXmlProperty(localName = "right_table_fields")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "right_table_fields")
  public List<String> getRightTableFields() {
    return rightTableFields;
  }

  public void setRightTableFields(List<String> rightTableFields) {
    this.rightTableFields = rightTableFields;
  }

  public GdalRelationshipDto mappingTableName(String mappingTableName) {
    this.mappingTableName = mappingTableName;
    return this;
  }

  /**
   * Get mappingTableName
   *
   * @return mappingTableName
   */
  @Schema(name = "mapping_table_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mapping_table_name")
  @JacksonXmlProperty(localName = "mapping_table_name")
  @XmlElement(name = "mapping_table_name")
  public String getMappingTableName() {
    return mappingTableName;
  }

  public void setMappingTableName(String mappingTableName) {
    this.mappingTableName = mappingTableName;
  }

  public GdalRelationshipDto leftMappingTableFields(List<String> leftMappingTableFields) {
    this.leftMappingTableFields = leftMappingTableFields;
    return this;
  }

  public GdalRelationshipDto addLeftMappingTableFieldsItem(String leftMappingTableFieldsItem) {
    if (this.leftMappingTableFields == null) {
      this.leftMappingTableFields = new ArrayList<>();
    }
    this.leftMappingTableFields.add(leftMappingTableFieldsItem);
    return this;
  }

  /**
   * Get leftMappingTableFields
   *
   * @return leftMappingTableFields
   */
  @Schema(name = "left_mapping_table_fields", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("left_mapping_table_fields")
  @JacksonXmlProperty(localName = "left_mapping_table_fields")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "left_mapping_table_fields")
  public List<String> getLeftMappingTableFields() {
    return leftMappingTableFields;
  }

  public void setLeftMappingTableFields(List<String> leftMappingTableFields) {
    this.leftMappingTableFields = leftMappingTableFields;
  }

  public GdalRelationshipDto rightMappingTableFields(List<String> rightMappingTableFields) {
    this.rightMappingTableFields = rightMappingTableFields;
    return this;
  }

  public GdalRelationshipDto addRightMappingTableFieldsItem(String rightMappingTableFieldsItem) {
    if (this.rightMappingTableFields == null) {
      this.rightMappingTableFields = new ArrayList<>();
    }
    this.rightMappingTableFields.add(rightMappingTableFieldsItem);
    return this;
  }

  /**
   * Get rightMappingTableFields
   *
   * @return rightMappingTableFields
   */
  @Schema(name = "right_mapping_table_fields", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("right_mapping_table_fields")
  @JacksonXmlProperty(localName = "right_mapping_table_fields")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "right_mapping_table_fields")
  public List<String> getRightMappingTableFields() {
    return rightMappingTableFields;
  }

  public void setRightMappingTableFields(List<String> rightMappingTableFields) {
    this.rightMappingTableFields = rightMappingTableFields;
  }

  public GdalRelationshipDto forwardPathLabel(String forwardPathLabel) {
    this.forwardPathLabel = forwardPathLabel;
    return this;
  }

  /**
   * Get forwardPathLabel
   *
   * @return forwardPathLabel
   */
  @NotNull
  @Schema(name = "forward_path_label", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("forward_path_label")
  @JacksonXmlProperty(localName = "forward_path_label")
  @XmlElement(name = "forward_path_label")
  public String getForwardPathLabel() {
    return forwardPathLabel;
  }

  public void setForwardPathLabel(String forwardPathLabel) {
    this.forwardPathLabel = forwardPathLabel;
  }

  public GdalRelationshipDto backwardPathLabel(String backwardPathLabel) {
    this.backwardPathLabel = backwardPathLabel;
    return this;
  }

  /**
   * Get backwardPathLabel
   *
   * @return backwardPathLabel
   */
  @NotNull
  @Schema(name = "backward_path_label", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("backward_path_label")
  @JacksonXmlProperty(localName = "backward_path_label")
  @XmlElement(name = "backward_path_label")
  public String getBackwardPathLabel() {
    return backwardPathLabel;
  }

  public void setBackwardPathLabel(String backwardPathLabel) {
    this.backwardPathLabel = backwardPathLabel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdalRelationshipDto relationship = (GdalRelationshipDto) o;
    return Objects.equals(this.type, relationship.type)
        && Objects.equals(this.relatedTableType, relationship.relatedTableType)
        && Objects.equals(this.cardinality, relationship.cardinality)
        && Objects.equals(this.leftTableName, relationship.leftTableName)
        && Objects.equals(this.rightTableName, relationship.rightTableName)
        && Objects.equals(this.leftTableFields, relationship.leftTableFields)
        && Objects.equals(this.rightTableFields, relationship.rightTableFields)
        && Objects.equals(this.mappingTableName, relationship.mappingTableName)
        && Objects.equals(this.leftMappingTableFields, relationship.leftMappingTableFields)
        && Objects.equals(this.rightMappingTableFields, relationship.rightMappingTableFields)
        && Objects.equals(this.forwardPathLabel, relationship.forwardPathLabel)
        && Objects.equals(this.backwardPathLabel, relationship.backwardPathLabel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        type,
        relatedTableType,
        cardinality,
        leftTableName,
        rightTableName,
        leftTableFields,
        rightTableFields,
        mappingTableName,
        leftMappingTableFields,
        rightMappingTableFields,
        forwardPathLabel,
        backwardPathLabel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdalRelationshipDto {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    relatedTableType: ").append(toIndentedString(relatedTableType)).append("\n");
    sb.append("    cardinality: ").append(toIndentedString(cardinality)).append("\n");
    sb.append("    leftTableName: ").append(toIndentedString(leftTableName)).append("\n");
    sb.append("    rightTableName: ").append(toIndentedString(rightTableName)).append("\n");
    sb.append("    leftTableFields: ").append(toIndentedString(leftTableFields)).append("\n");
    sb.append("    rightTableFields: ").append(toIndentedString(rightTableFields)).append("\n");
    sb.append("    mappingTableName: ").append(toIndentedString(mappingTableName)).append("\n");
    sb.append("    leftMappingTableFields: ")
        .append(toIndentedString(leftMappingTableFields))
        .append("\n");
    sb.append("    rightMappingTableFields: ")
        .append(toIndentedString(rightMappingTableFields))
        .append("\n");
    sb.append("    forwardPathLabel: ").append(toIndentedString(forwardPathLabel)).append("\n");
    sb.append("    backwardPathLabel: ").append(toIndentedString(backwardPathLabel)).append("\n");
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

    private GdalRelationshipDto instance;

    public Builder() {
      this(new GdalRelationshipDto());
    }

    protected Builder(GdalRelationshipDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(GdalRelationshipDto value) {
      this.instance.setType(value.type);
      this.instance.setRelatedTableType(value.relatedTableType);
      this.instance.setCardinality(value.cardinality);
      this.instance.setLeftTableName(value.leftTableName);
      this.instance.setRightTableName(value.rightTableName);
      this.instance.setLeftTableFields(value.leftTableFields);
      this.instance.setRightTableFields(value.rightTableFields);
      this.instance.setMappingTableName(value.mappingTableName);
      this.instance.setLeftMappingTableFields(value.leftMappingTableFields);
      this.instance.setRightMappingTableFields(value.rightMappingTableFields);
      this.instance.setForwardPathLabel(value.forwardPathLabel);
      this.instance.setBackwardPathLabel(value.backwardPathLabel);
      return this;
    }

    public Builder type(TypeEnum type) {
      this.instance.type(type);
      return this;
    }

    public Builder relatedTableType(String relatedTableType) {
      this.instance.relatedTableType(relatedTableType);
      return this;
    }

    public Builder cardinality(CardinalityEnum cardinality) {
      this.instance.cardinality(cardinality);
      return this;
    }

    public Builder leftTableName(String leftTableName) {
      this.instance.leftTableName(leftTableName);
      return this;
    }

    public Builder rightTableName(String rightTableName) {
      this.instance.rightTableName(rightTableName);
      return this;
    }

    public Builder leftTableFields(List<String> leftTableFields) {
      this.instance.leftTableFields(leftTableFields);
      return this;
    }

    public Builder rightTableFields(List<String> rightTableFields) {
      this.instance.rightTableFields(rightTableFields);
      return this;
    }

    public Builder mappingTableName(String mappingTableName) {
      this.instance.mappingTableName(mappingTableName);
      return this;
    }

    public Builder leftMappingTableFields(List<String> leftMappingTableFields) {
      this.instance.leftMappingTableFields(leftMappingTableFields);
      return this;
    }

    public Builder rightMappingTableFields(List<String> rightMappingTableFields) {
      this.instance.rightMappingTableFields(rightMappingTableFields);
      return this;
    }

    public Builder forwardPathLabel(String forwardPathLabel) {
      this.instance.forwardPathLabel(forwardPathLabel);
      return this;
    }

    public Builder backwardPathLabel(String backwardPathLabel) {
      this.instance.backwardPathLabel(backwardPathLabel);
      return this;
    }

    /**
     * returns a built GdalRelationshipDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public GdalRelationshipDto build() {
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
