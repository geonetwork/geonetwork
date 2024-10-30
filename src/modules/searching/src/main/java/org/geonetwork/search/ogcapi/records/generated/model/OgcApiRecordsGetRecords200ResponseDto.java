/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

/** OgcApiRecordsGetRecords200ResponseDto */
@JsonTypeName("getRecords_200_response")
@JacksonXmlRootElement(localName = "OgcApiRecordsGetRecords200ResponseDto")
@XmlRootElement(name = "OgcApiRecordsGetRecords200ResponseDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsGetRecords200ResponseDto {

    private TypeEnum type;

    @Valid
    private List<@Valid OgcApiRecordsRecordGeoJSONDto> features = new ArrayList<>();

    @Valid
    private List<@Valid OgcApiRecordsLinkDto> links = new ArrayList<>();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime timeStamp;

    private Integer numberMatched;
    private Integer numberReturned;

    @Valid
    private List<OgcApiRecordsLinkTemplateDto> linkTemplates = new ArrayList<>();

    public OgcApiRecordsGetRecords200ResponseDto() {
        super();
    }

    /** Constructor with only required parameters */
    public OgcApiRecordsGetRecords200ResponseDto(TypeEnum type, List<@Valid OgcApiRecordsRecordGeoJSONDto> features) {
        this.type = type;
        this.features = features;
    }

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsGetRecords200ResponseDto type(TypeEnum type) {
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

    public OgcApiRecordsGetRecords200ResponseDto features(List<@Valid OgcApiRecordsRecordGeoJSONDto> features) {
        this.features = features;
        return this;
    }

    public OgcApiRecordsGetRecords200ResponseDto addFeaturesItem(OgcApiRecordsRecordGeoJSONDto featuresItem) {
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
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "features")
    public List<@Valid OgcApiRecordsRecordGeoJSONDto> getFeatures() {
        return features;
    }

    public void setFeatures(List<@Valid OgcApiRecordsRecordGeoJSONDto> features) {
        this.features = features;
    }

    public OgcApiRecordsGetRecords200ResponseDto links(List<@Valid OgcApiRecordsLinkDto> links) {
        this.links = links;
        return this;
    }

    public OgcApiRecordsGetRecords200ResponseDto addLinksItem(OgcApiRecordsLinkDto linksItem) {
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
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "links")
    public List<@Valid OgcApiRecordsLinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<@Valid OgcApiRecordsLinkDto> links) {
        this.links = links;
    }

    public OgcApiRecordsGetRecords200ResponseDto timeStamp(OffsetDateTime timeStamp) {
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
    @XmlElement(name = "timeStamp")
    public OffsetDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(OffsetDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public OgcApiRecordsGetRecords200ResponseDto numberMatched(Integer numberMatched) {
        this.numberMatched = numberMatched;
        return this;
    }

    /**
     * Get numberMatched minimum: 0
     *
     * @return numberMatched
     */
    @Min(0)
    @Schema(name = "numberMatched", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("numberMatched")
    @JacksonXmlProperty(localName = "numberMatched")
    @XmlElement(name = "numberMatched")
    public Integer getNumberMatched() {
        return numberMatched;
    }

    public void setNumberMatched(Integer numberMatched) {
        this.numberMatched = numberMatched;
    }

    public OgcApiRecordsGetRecords200ResponseDto numberReturned(Integer numberReturned) {
        this.numberReturned = numberReturned;
        return this;
    }

    /**
     * Get numberReturned minimum: 0
     *
     * @return numberReturned
     */
    @Min(0)
    @Schema(name = "numberReturned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("numberReturned")
    @JacksonXmlProperty(localName = "numberReturned")
    @XmlElement(name = "numberReturned")
    public Integer getNumberReturned() {
        return numberReturned;
    }

    public void setNumberReturned(Integer numberReturned) {
        this.numberReturned = numberReturned;
    }

    public OgcApiRecordsGetRecords200ResponseDto linkTemplates(List<OgcApiRecordsLinkTemplateDto> linkTemplates) {
        this.linkTemplates = linkTemplates;
        return this;
    }

    public OgcApiRecordsGetRecords200ResponseDto addLinkTemplatesItem(OgcApiRecordsLinkTemplateDto linkTemplatesItem) {
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
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "linkTemplates")
    public List<OgcApiRecordsLinkTemplateDto> getLinkTemplates() {
        return linkTemplates;
    }

    public void setLinkTemplates(List<OgcApiRecordsLinkTemplateDto> linkTemplates) {
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
        OgcApiRecordsGetRecords200ResponseDto getRecords200Response = (OgcApiRecordsGetRecords200ResponseDto) o;
        return Objects.equals(this.type, getRecords200Response.type)
                && Objects.equals(this.features, getRecords200Response.features)
                && Objects.equals(this.links, getRecords200Response.links)
                && Objects.equals(this.timeStamp, getRecords200Response.timeStamp)
                && Objects.equals(this.numberMatched, getRecords200Response.numberMatched)
                && Objects.equals(this.numberReturned, getRecords200Response.numberReturned)
                && Objects.equals(this.linkTemplates, getRecords200Response.linkTemplates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, features, links, timeStamp, numberMatched, numberReturned, linkTemplates);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsGetRecords200ResponseDto {\n"
                + "    type: "
                + toIndentedString(type)
                + "\n"
                + "    features: "
                + toIndentedString(features)
                + "\n"
                + "    links: "
                + toIndentedString(links)
                + "\n"
                + "    timeStamp: "
                + toIndentedString(timeStamp)
                + "\n"
                + "    numberMatched: "
                + toIndentedString(numberMatched)
                + "\n"
                + "    numberReturned: "
                + toIndentedString(numberReturned)
                + "\n"
                + "    linkTemplates: "
                + toIndentedString(linkTemplates)
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

    /** Gets or Sets type */
    public enum TypeEnum {
        FEATURE_COLLECTION("FeatureCollection");

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

        private OgcApiRecordsGetRecords200ResponseDto instance;

        public Builder() {
            this(new OgcApiRecordsGetRecords200ResponseDto());
        }

        protected Builder(OgcApiRecordsGetRecords200ResponseDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsGetRecords200ResponseDto value) {
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

        public Builder features(List<@Valid OgcApiRecordsRecordGeoJSONDto> features) {
            this.instance.features(features);
            return this;
        }

        public Builder links(List<@Valid OgcApiRecordsLinkDto> links) {
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

        public Builder linkTemplates(List<OgcApiRecordsLinkTemplateDto> linkTemplates) {
            this.instance.linkTemplates(linkTemplates);
            return this;
        }

        /**
         * returns a built OgcApiRecordsGetRecords200ResponseDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsGetRecords200ResponseDto build() {
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
