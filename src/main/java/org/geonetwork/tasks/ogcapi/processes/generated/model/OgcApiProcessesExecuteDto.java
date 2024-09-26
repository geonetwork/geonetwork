/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.tasks.ogcapi.processes.generated.model;

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
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** OgcApiProcessesExecuteDto */
@JsonTypeName("execute")
@JacksonXmlRootElement(localName = "OgcApiProcessesExecuteDto")
@XmlRootElement(name = "OgcApiProcessesExecuteDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-06T16:12:06.105974013+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiProcessesExecuteDto {

  @Valid private Map<String, OgcApiProcessesExecuteInputsValueDto> inputs = new HashMap<>();

  @Valid private Map<String, OgcApiProcessesOutputDto> outputs = new HashMap<>();
  private ResponseEnum response;
  private OgcApiProcessesSubscriberDto subscriber;

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiProcessesExecuteDto inputs(
      Map<String, OgcApiProcessesExecuteInputsValueDto> inputs) {
    this.inputs = inputs;
    return this;
  }

  public OgcApiProcessesExecuteDto putInputsItem(
      String key, OgcApiProcessesExecuteInputsValueDto inputsItem) {
    if (this.inputs == null) {
      this.inputs = new HashMap<>();
    }
    this.inputs.put(key, inputsItem);
    return this;
  }

  /**
   * Get inputs
   *
   * @return inputs
   */
  @Valid
  @Schema(name = "inputs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inputs")
  @JacksonXmlProperty(localName = "inputs")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "inputs")
  public Map<String, OgcApiProcessesExecuteInputsValueDto> getInputs() {
    return inputs;
  }

  public void setInputs(Map<String, OgcApiProcessesExecuteInputsValueDto> inputs) {
    this.inputs = inputs;
  }

  public OgcApiProcessesExecuteDto outputs(Map<String, OgcApiProcessesOutputDto> outputs) {
    this.outputs = outputs;
    return this;
  }

  public OgcApiProcessesExecuteDto putOutputsItem(
      String key, OgcApiProcessesOutputDto outputsItem) {
    if (this.outputs == null) {
      this.outputs = new HashMap<>();
    }
    this.outputs.put(key, outputsItem);
    return this;
  }

  /**
   * Get outputs
   *
   * @return outputs
   */
  @Valid
  @Schema(name = "outputs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outputs")
  @JacksonXmlProperty(localName = "outputs")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "outputs")
  public Map<String, OgcApiProcessesOutputDto> getOutputs() {
    return outputs;
  }

  public void setOutputs(Map<String, OgcApiProcessesOutputDto> outputs) {
    this.outputs = outputs;
  }

  public OgcApiProcessesExecuteDto response(ResponseEnum response) {
    this.response = response;
    return this;
  }

  /**
   * Get response
   *
   * @return response
   */
  @Schema(name = "response", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("response")
  @JacksonXmlProperty(localName = "response")
  @XmlElement(name = "response")
  public ResponseEnum getResponse() {
    return response;
  }

  public void setResponse(ResponseEnum response) {
    this.response = response;
  }

  public OgcApiProcessesExecuteDto subscriber(OgcApiProcessesSubscriberDto subscriber) {
    this.subscriber = subscriber;
    return this;
  }

  /**
   * Get subscriber
   *
   * @return subscriber
   */
  @Valid
  @Schema(name = "subscriber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subscriber")
  @JacksonXmlProperty(localName = "subscriber")
  @XmlElement(name = "subscriber")
  public OgcApiProcessesSubscriberDto getSubscriber() {
    return subscriber;
  }

  public void setSubscriber(OgcApiProcessesSubscriberDto subscriber) {
    this.subscriber = subscriber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiProcessesExecuteDto execute = (OgcApiProcessesExecuteDto) o;
    return Objects.equals(this.inputs, execute.inputs)
        && Objects.equals(this.outputs, execute.outputs)
        && Objects.equals(this.response, execute.response)
        && Objects.equals(this.subscriber, execute.subscriber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inputs, outputs, response, subscriber);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiProcessesExecuteDto {\n"
            + "    inputs: "
            + toIndentedString(inputs)
            + "\n"
            + "    outputs: "
            + toIndentedString(outputs)
            + "\n"
            + "    response: "
            + toIndentedString(response)
            + "\n"
            + "    subscriber: "
            + toIndentedString(subscriber)
            + "\n"
            + "}";
    return sb;
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

  /** Create a builder with a shallow copy of this instance. */
  public Builder toBuilder() {
    Builder builder = new Builder();
    return builder.copyOf(this);
  }

  /** Gets or Sets response */
  public enum ResponseEnum {
    RAW("raw"),

    DOCUMENT("document");

    private final String value;

    ResponseEnum(String value) {
      this.value = value;
    }

    @JsonCreator
    public static ResponseEnum fromValue(String value) {
      for (ResponseEnum b : ResponseEnum.values()) {
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

  public static class Builder {

    private OgcApiProcessesExecuteDto instance;

    public Builder() {
      this(new OgcApiProcessesExecuteDto());
    }

    protected Builder(OgcApiProcessesExecuteDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiProcessesExecuteDto value) {
      this.instance.setInputs(value.inputs);
      this.instance.setOutputs(value.outputs);
      this.instance.setResponse(value.response);
      this.instance.setSubscriber(value.subscriber);
      return this;
    }

    public Builder inputs(Map<String, OgcApiProcessesExecuteInputsValueDto> inputs) {
      this.instance.inputs(inputs);
      return this;
    }

    public Builder outputs(Map<String, OgcApiProcessesOutputDto> outputs) {
      this.instance.outputs(outputs);
      return this;
    }

    public Builder response(ResponseEnum response) {
      this.instance.response(response);
      return this;
    }

    public Builder subscriber(OgcApiProcessesSubscriberDto subscriber) {
      this.instance.subscriber(subscriber);
      return this;
    }

    /**
     * returns a built OgcApiProcessesExecuteDto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiProcessesExecuteDto build() {
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
}
