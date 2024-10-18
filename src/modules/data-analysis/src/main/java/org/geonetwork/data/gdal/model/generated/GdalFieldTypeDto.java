/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

/** Gets or Sets fieldType */
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public enum GdalFieldTypeDto {
  INTEGER("Integer"),

  INTEGER64("Integer64"),

  REAL("Real"),

  STRING("String"),

  BINARY("Binary"),

  INTEGER_LIST("IntegerList"),

  INTEGER64_LIST("Integer64List"),

  REAL_LIST("RealList"),

  STRING_LIST("StringList"),

  DATE("Date"),

  TIME("Time"),

  DATE_TIME("DateTime");

  private String value;

  GdalFieldTypeDto(String value) {
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
  public static GdalFieldTypeDto fromValue(String value) {
    for (GdalFieldTypeDto b : GdalFieldTypeDto.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
