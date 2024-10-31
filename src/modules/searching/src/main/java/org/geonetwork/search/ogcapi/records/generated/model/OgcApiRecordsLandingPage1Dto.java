/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** OgcApiRecordsLandingPage1Dto */
@JsonTypeName("landingPage_1")
@JacksonXmlRootElement(localName = "OgcApiRecordsLandingPage1Dto")
@XmlRootElement(name = "OgcApiRecordsLandingPage1Dto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsLandingPage1Dto {

    private String title;

    private String description;

    @Valid
    private List<@Valid OgcApiRecordsLinkDto> links = new ArrayList<>();

    public OgcApiRecordsLandingPage1Dto() {
        super();
    }

    /** Constructor with only required parameters */
    public OgcApiRecordsLandingPage1Dto(List<@Valid OgcApiRecordsLinkDto> links) {
        this.links = links;
    }

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsLandingPage1Dto title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Get title
     *
     * @return title
     */
    @Schema(name = "title", example = "Buildings in Bonn", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title")
    @XmlElement(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public OgcApiRecordsLandingPage1Dto description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     *
     * @return description
     */
    @Schema(
            name = "description",
            example = "Access to data about buildings in the city of Bonn via a Web API that conforms to the"
                    + " OGC API Features specification.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("description")
    @JacksonXmlProperty(localName = "description")
    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OgcApiRecordsLandingPage1Dto links(List<@Valid OgcApiRecordsLinkDto> links) {
        this.links = links;
        return this;
    }

    public OgcApiRecordsLandingPage1Dto addLinksItem(OgcApiRecordsLinkDto linksItem) {
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
    @NotNull
    @Valid
    @Schema(name = "links", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OgcApiRecordsLandingPage1Dto landingPage1 = (OgcApiRecordsLandingPage1Dto) o;
        return Objects.equals(this.title, landingPage1.title)
                && Objects.equals(this.description, landingPage1.description)
                && Objects.equals(this.links, landingPage1.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, links);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsLandingPage1Dto {\n"
                + "    title: "
                + toIndentedString(title)
                + "\n"
                + "    description: "
                + toIndentedString(description)
                + "\n"
                + "    links: "
                + toIndentedString(links)
                + "\n"
                + "}";
        return sb;
    }

    /** Convert the given object to string with each line indented by 4 spaces (except the first line). */
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

    public static class Builder {

        private OgcApiRecordsLandingPage1Dto instance;

        public Builder() {
            this(new OgcApiRecordsLandingPage1Dto());
        }

        protected Builder(OgcApiRecordsLandingPage1Dto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsLandingPage1Dto value) {
            this.instance.setTitle(value.title);
            this.instance.setDescription(value.description);
            this.instance.setLinks(value.links);
            return this;
        }

        public Builder title(String title) {
            this.instance.title(title);
            return this;
        }

        public Builder description(String description) {
            this.instance.description(description);
            return this;
        }

        public Builder links(List<@Valid OgcApiRecordsLinkDto> links) {
            this.instance.links(links);
            return this;
        }

        /**
         * returns a built OgcApiRecordsLandingPage1Dto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsLandingPage1Dto build() {
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
