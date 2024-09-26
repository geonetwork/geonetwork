/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.tasks.ogcapi.processes.generated.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

/** Gets or Sets jobControlOptions */
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public enum OgcApiProcessesJobControlOptionsDto {
  SYNC_EXECUTE("sync-execute"),

  ASYNC_EXECUTE("async-execute"),

  DISMISS("dismiss");

  private final String value;

  OgcApiProcessesJobControlOptionsDto(String value) {
    this.value = value;
  }

  @JsonCreator
  public static OgcApiProcessesJobControlOptionsDto fromValue(String value) {
    for (OgcApiProcessesJobControlOptionsDto b : OgcApiProcessesJobControlOptionsDto.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
