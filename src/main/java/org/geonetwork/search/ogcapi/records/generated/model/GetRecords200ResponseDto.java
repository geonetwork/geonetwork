package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * GetRecords200ResponseDto
 */

@JsonTypeName("getRecords_200_response")
@JacksonXmlRootElement(localName = "GetRecords200ResponseDto")
@XmlRootElement(name = "GetRecords200ResponseDto")
@XmlAccessorType(XmlAccessType.FIELD)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]", comments = "Generator version: 7.6.0")
public class GetRecords200ResponseDto {

    private TypeEnum type;
    @Valid
    private List<@Valid RecordGeoJSONDto> features = new ArrayList<>();
    @Valid
    private List<@Valid LinkDto> links = new ArrayList<>();
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime timeStamp;
    private Integer numberMatched;
    private Integer numberReturned;
    @Valid
    private List<LinkTemplateDto> linkTemplates = new ArrayList<>();

    public GetRecords200ResponseDto() {
        super();
    }

    /**
     * Constructor with only required parameters
     */
    public GetRecords200ResponseDto(TypeEnum type, List<@Valid RecordGeoJSONDto> features) {
        this.type = type;
        this.features = features;
    }

    /**
     * Create a builder with no initialized field (except for the default values).
     */
    public static Builder builder() {
        return new Builder();
    }

    public GetRecords200ResponseDto type(TypeEnum type) {
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
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public GetRecords200ResponseDto features(List<@Valid RecordGeoJSONDto> features) {
        this.features = features;
        return this;
    }

    public GetRecords200ResponseDto addFeaturesItem(RecordGeoJSONDto featuresItem) {
        if (this.features == null) {
            this.features = new ArrayList<>();
        }
        this.features.add(featuresItem);
        return this;
    }

    /**
     * Get features
     *
     * @return features
     */
    @NotNull
    @Valid
    @Schema(name = "features", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("features")
    @JacksonXmlProperty(localName = "features")
    public List<@Valid RecordGeoJSONDto> getFeatures() {
        return features;
    }

    public void setFeatures(List<@Valid RecordGeoJSONDto> features) {
        this.features = features;
    }

    public GetRecords200ResponseDto links(List<@Valid LinkDto> links) {
        this.links = links;
        return this;
    }

    public GetRecords200ResponseDto addLinksItem(LinkDto linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

    /**
     * Get links
     *
     * @return links
     */
    @Valid
    @Schema(name = "links", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("links")
    @JacksonXmlProperty(localName = "links")
    public List<@Valid LinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<@Valid LinkDto> links) {
        this.links = links;
    }

    public GetRecords200ResponseDto timeStamp(OffsetDateTime timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    /**
     * Get timeStamp
     *
     * @return timeStamp
     */
    @Valid
    @Schema(name = "timeStamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("timeStamp")
    @JacksonXmlProperty(localName = "timeStamp")
    public OffsetDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(OffsetDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public GetRecords200ResponseDto numberMatched(Integer numberMatched) {
        this.numberMatched = numberMatched;
        return this;
    }

    /**
     * Get numberMatched
     * minimum: 0
     *
     * @return numberMatched
     */
    @Min(0)
    @Schema(name = "numberMatched", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("numberMatched")
    @JacksonXmlProperty(localName = "numberMatched")
    public Integer getNumberMatched() {
        return numberMatched;
    }

    public void setNumberMatched(Integer numberMatched) {
        this.numberMatched = numberMatched;
    }

    public GetRecords200ResponseDto numberReturned(Integer numberReturned) {
        this.numberReturned = numberReturned;
        return this;
    }

    /**
     * Get numberReturned
     * minimum: 0
     *
     * @return numberReturned
     */
    @Min(0)
    @Schema(name = "numberReturned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("numberReturned")
    @JacksonXmlProperty(localName = "numberReturned")
    public Integer getNumberReturned() {
        return numberReturned;
    }

    public void setNumberReturned(Integer numberReturned) {
        this.numberReturned = numberReturned;
    }

    public GetRecords200ResponseDto linkTemplates(List<LinkTemplateDto> linkTemplates) {
        this.linkTemplates = linkTemplates;
        return this;
    }

    public GetRecords200ResponseDto addLinkTemplatesItem(LinkTemplateDto linkTemplatesItem) {
        if (this.linkTemplates == null) {
            this.linkTemplates = new ArrayList<>();
        }
        this.linkTemplates.add(linkTemplatesItem);
        return this;
    }

    /**
     * Get linkTemplates
     *
     * @return linkTemplates
     */
    @Valid
    @Schema(name = "linkTemplates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("linkTemplates")
    @JacksonXmlProperty(localName = "linkTemplates")
    public List<LinkTemplateDto> getLinkTemplates() {
        return linkTemplates;
    }

    public void setLinkTemplates(List<LinkTemplateDto> linkTemplates) {
        this.linkTemplates = linkTemplates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetRecords200ResponseDto getRecords200Response = (GetRecords200ResponseDto) o;
        return Objects.equals(this.type, getRecords200Response.type) &&
            Objects.equals(this.features, getRecords200Response.features) &&
            Objects.equals(this.links, getRecords200Response.links) &&
            Objects.equals(this.timeStamp, getRecords200Response.timeStamp) &&
            Objects.equals(this.numberMatched, getRecords200Response.numberMatched) &&
            Objects.equals(this.numberReturned, getRecords200Response.numberReturned) &&
            Objects.equals(this.linkTemplates, getRecords200Response.linkTemplates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, features, links, timeStamp, numberMatched, numberReturned, linkTemplates);
    }

    @Override
    public String toString() {
        String sb = "class GetRecords200ResponseDto {\n" +
            "    type: " + toIndentedString(type) + "\n" +
            "    features: " + toIndentedString(features) + "\n" +
            "    links: " + toIndentedString(links) + "\n" +
            "    timeStamp: " + toIndentedString(timeStamp) + "\n" +
            "    numberMatched: " + toIndentedString(numberMatched) + "\n" +
            "    numberReturned: " + toIndentedString(numberReturned) + "\n" +
            "    linkTemplates: " + toIndentedString(linkTemplates) + "\n" +
            "}";
        return sb;
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    /**
     * Create a builder with a shallow copy of this instance.
     */
    public Builder toBuilder() {
        Builder builder = new Builder();
        return builder.copyOf(this);
    }

    /**
     * Gets or Sets type
     */
    public enum TypeEnum {
        FEATURECOLLECTION("FeatureCollection");

        private final String value;

        TypeEnum(String value) {
            this.value = value;
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

        private GetRecords200ResponseDto instance;

        public Builder() {
            this(new GetRecords200ResponseDto());
        }

        protected Builder(GetRecords200ResponseDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(GetRecords200ResponseDto value) {
            this.instance.setType(value.type);
            this.instance.setFeatures(value.features);
            this.instance.setLinks(value.links);
            this.instance.setTimeStamp(value.timeStamp);
            this.instance.setNumberMatched(value.numberMatched);
            this.instance.setNumberReturned(value.numberReturned);
            this.instance.setLinkTemplates(value.linkTemplates);
            return this;
        }

        public Builder type(TypeEnum type) {
            this.instance.type(type);
            return this;
        }

        public Builder features(List<@Valid RecordGeoJSONDto> features) {
            this.instance.features(features);
            return this;
        }

        public Builder links(List<@Valid LinkDto> links) {
            this.instance.links(links);
            return this;
        }

        public Builder timeStamp(OffsetDateTime timeStamp) {
            this.instance.timeStamp(timeStamp);
            return this;
        }

        public Builder numberMatched(Integer numberMatched) {
            this.instance.numberMatched(numberMatched);
            return this;
        }

        public Builder numberReturned(Integer numberReturned) {
            this.instance.numberReturned(numberReturned);
            return this;
        }

        public Builder linkTemplates(List<LinkTemplateDto> linkTemplates) {
            this.instance.linkTemplates(linkTemplates);
            return this;
        }

        /**
         * returns a built GetRecords200ResponseDto instance.
         * <p>
         * The builder is not reusable (NullPointerException)
         */
        public GetRecords200ResponseDto build() {
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

