/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CollectionsDto
 */

@JsonTypeName("collections")
@JacksonXmlRootElement(localName = "CollectionsDto")
@XmlRootElement(name = "CollectionsDto")
@XmlAccessorType(XmlAccessType.FIELD)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]", comments = "Generator version: 7.6.0")
public class CollectionsDto {

    @Valid
    private List<@Valid LinkDto> links = new ArrayList<>();

    @Valid
    private List<@Valid CollectionDto> collections = new ArrayList<>();

    public CollectionsDto() {
        super();
    }

    /**
     * Constructor with only required parameters
     */
    public CollectionsDto(List<@Valid LinkDto> links, List<@Valid CollectionDto> collections) {
        this.links = links;
        this.collections = collections;
    }

    /**
     * Create a builder with no initialized field (except for the default values).
     */
    public static Builder builder() {
        return new Builder();
    }

    public CollectionsDto links(List<@Valid LinkDto> links) {
        this.links = links;
        return this;
    }

    public CollectionsDto addLinksItem(LinkDto linksItem) {
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
    public List<@Valid LinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<@Valid LinkDto> links) {
        this.links = links;
    }

    public CollectionsDto collections(List<@Valid CollectionDto> collections) {
        this.collections = collections;
        return this;
    }

    public CollectionsDto addCollectionsItem(CollectionDto collectionsItem) {
        if (this.collections == null) {
            this.collections = new ArrayList<>();
        }
        this.collections.add(collectionsItem);
        return this;
    }

    /**
     * Get collections
     *
     * @return collections
     */
    @NotNull
    @Valid
    @Schema(name = "collections", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("collections")
    @JacksonXmlProperty(localName = "collections")
    public List<@Valid CollectionDto> getCollections() {
        return collections;
    }

    public void setCollections(List<@Valid CollectionDto> collections) {
        this.collections = collections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CollectionsDto collections = (CollectionsDto) o;
        return Objects.equals(this.links, collections.links) &&
            Objects.equals(this.collections, collections.collections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, collections);
    }

    @Override
    public String toString() {
        String sb = "class CollectionsDto {\n" +
            "    links: " + toIndentedString(links) + "\n" +
            "    collections: " + toIndentedString(collections) + "\n" +
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

    public static class Builder {

        private CollectionsDto instance;

        public Builder() {
            this(new CollectionsDto());
        }

        protected Builder(CollectionsDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(CollectionsDto value) {
            this.instance.setLinks(value.links);
            this.instance.setCollections(value.collections);
            return this;
        }

        public Builder links(List<@Valid LinkDto> links) {
            this.instance.links(links);
            return this;
        }

        public Builder collections(List<@Valid CollectionDto> collections) {
            this.instance.collections(collections);
            return this;
        }

        /**
         * returns a built CollectionsDto instance.
         * <p>
         * The builder is not reusable (NullPointerException)
         */
        public CollectionsDto build() {
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

