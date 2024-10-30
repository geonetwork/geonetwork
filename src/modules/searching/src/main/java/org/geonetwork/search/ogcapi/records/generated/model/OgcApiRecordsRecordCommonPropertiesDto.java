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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

/** OgcApiRecordsRecordCommonPropertiesDto */
@JsonTypeName("recordCommonProperties")
@JacksonXmlRootElement(localName = "OgcApiRecordsRecordCommonPropertiesDto")
@XmlRootElement(name = "OgcApiRecordsRecordCommonPropertiesDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsRecordCommonPropertiesDto {

    @Valid
    private List<String> conformsTo = new ArrayList<>();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime created;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime updated;

    private String type;

    private String title;

    private String description;

    @Valid
    private List<String> keywords = new ArrayList<>();

    @Valid
    private List<@Valid OgcApiRecordsThemeDto> themes = new ArrayList<>();

    private OgcApiRecordsLanguageDto language;

    @Valid
    private List<@Valid OgcApiRecordsLanguageDto> languages = new ArrayList<>();

    @Valid
    private List<@Valid OgcApiRecordsLanguageDto> resourceLanguages = new ArrayList<>();

    @Valid
    private List<@Valid OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto> externalIds = new ArrayList<>();

    @Valid
    private List<@Valid OgcApiRecordsFormatDto> formats = new ArrayList<>();

    @Valid
    private List<@Valid OgcApiRecordsContactDto> contacts = new ArrayList<>();

    private OgcApiRecordsLicenseDto license;

    private String rights;

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsRecordCommonPropertiesDto conformsTo(List<String> conformsTo) {
        this.conformsTo = conformsTo;
        return this;
    }

    public OgcApiRecordsRecordCommonPropertiesDto addConformsToItem(String conformsToItem) {
        if (this.conformsTo == null) {
            this.conformsTo = new ArrayList<>();
        }
        this.conformsTo.add(conformsToItem);
        return this;
    }

    /**
     * The extensions/conformance classes used in this record.
     *
     * @return conformsTo
     */
    @Schema(
            name = "conformsTo",
            description = "The extensions/conformance classes used in this record.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("conformsTo")
    @JacksonXmlProperty(localName = "conformsTo")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "conformsTo")
    public List<String> getConformsTo() {
        return conformsTo;
    }

    public void setConformsTo(List<String> conformsTo) {
        this.conformsTo = conformsTo;
    }

    public OgcApiRecordsRecordCommonPropertiesDto created(OffsetDateTime created) {
        this.created = created;
        return this;
    }

    /**
     * The date this record was created in the server.
     *
     * @return created
     */
    @Valid
    @Schema(
            name = "created",
            description = "The date this record was created in the server.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("created")
    @JacksonXmlProperty(localName = "created")
    @XmlElement(name = "created")
    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public OgcApiRecordsRecordCommonPropertiesDto updated(OffsetDateTime updated) {
        this.updated = updated;
        return this;
    }

    /**
     * The most recent date on which the record was changed.
     *
     * @return updated
     */
    @Valid
    @Schema(
            name = "updated",
            description = "The most recent date on which the record was changed.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("updated")
    @JacksonXmlProperty(localName = "updated")
    @XmlElement(name = "updated")
    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(OffsetDateTime updated) {
        this.updated = updated;
    }

    public OgcApiRecordsRecordCommonPropertiesDto type(String type) {
        this.type = type;
        return this;
    }

    /**
     * The nature or genre of the resource. The value should be a code, convenient for filtering
     * records. Where available, a link to the canonical URI of the record type resource will be added
     * to the 'links' property.
     *
     * @return type
     */
    @Schema(
            name = "type",
            description = "The nature or genre of the resource. The value should be a code, convenient for"
                    + " filtering records. Where available, a link to the canonical URI of the record"
                    + " type resource will be added to the 'links' property.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("type")
    @JacksonXmlProperty(localName = "type")
    @XmlElement(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OgcApiRecordsRecordCommonPropertiesDto title(String title) {
        this.title = title;
        return this;
    }

    /**
     * A human-readable name given to the resource.
     *
     * @return title
     */
    @Schema(
            name = "title",
            description = "A human-readable name given to the resource.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title")
    @XmlElement(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public OgcApiRecordsRecordCommonPropertiesDto description(String description) {
        this.description = description;
        return this;
    }

    /**
     * A free-text account of the resource.
     *
     * @return description
     */
    @Schema(
            name = "description",
            description = "A free-text account of the resource.",
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

    public OgcApiRecordsRecordCommonPropertiesDto keywords(List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    public OgcApiRecordsRecordCommonPropertiesDto addKeywordsItem(String keywordsItem) {
        if (this.keywords == null) {
            this.keywords = new ArrayList<>();
        }
        this.keywords.add(keywordsItem);
        return this;
    }

    /**
     * The topic or topics of the resource. Typically represented using free-form keywords, tags, key
     * phrases, or classification codes.
     *
     * @return keywords
     */
    @Schema(
            name = "keywords",
            description = "The topic or topics of the resource. Typically represented using free-form keywords,"
                    + " tags, key phrases, or classification codes.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("keywords")
    @JacksonXmlProperty(localName = "keywords")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "keywords")
    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public OgcApiRecordsRecordCommonPropertiesDto themes(List<@Valid OgcApiRecordsThemeDto> themes) {
        this.themes = themes;
        return this;
    }

    public OgcApiRecordsRecordCommonPropertiesDto addThemesItem(OgcApiRecordsThemeDto themesItem) {
        if (this.themes == null) {
            this.themes = new ArrayList<>();
        }
        this.themes.add(themesItem);
        return this;
    }

    /**
     * A knowledge organization system used to classify the resource.
     *
     * @return themes
     */
    @Valid
    @Size(min = 1)
    @Schema(
            name = "themes",
            description = "A knowledge organization system used to classify the resource.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("themes")
    @JacksonXmlProperty(localName = "themes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "themes")
    public List<@Valid OgcApiRecordsThemeDto> getThemes() {
        return themes;
    }

    public void setThemes(List<@Valid OgcApiRecordsThemeDto> themes) {
        this.themes = themes;
    }

    public OgcApiRecordsRecordCommonPropertiesDto language(OgcApiRecordsLanguageDto language) {
        this.language = language;
        return this;
    }

    /**
     * Get language
     *
     * @return language
     */
    @Valid
    @Schema(name = "language", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("language")
    @JacksonXmlProperty(localName = "language")
    @XmlElement(name = "language")
    public OgcApiRecordsLanguageDto getLanguage() {
        return language;
    }

    public void setLanguage(OgcApiRecordsLanguageDto language) {
        this.language = language;
    }

    public OgcApiRecordsRecordCommonPropertiesDto languages(List<@Valid OgcApiRecordsLanguageDto> languages) {
        this.languages = languages;
        return this;
    }

    public OgcApiRecordsRecordCommonPropertiesDto addLanguagesItem(OgcApiRecordsLanguageDto languagesItem) {
        if (this.languages == null) {
            this.languages = new ArrayList<>();
        }
        this.languages.add(languagesItem);
        return this;
    }

    /**
     * This list of languages in which this record is available.
     *
     * @return languages
     */
    @Valid
    @Schema(
            name = "languages",
            description = "This list of languages in which this record is available.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("languages")
    @JacksonXmlProperty(localName = "languages")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "languages")
    public List<@Valid OgcApiRecordsLanguageDto> getLanguages() {
        return languages;
    }

    public void setLanguages(List<@Valid OgcApiRecordsLanguageDto> languages) {
        this.languages = languages;
    }

    public OgcApiRecordsRecordCommonPropertiesDto resourceLanguages(
            List<@Valid OgcApiRecordsLanguageDto> resourceLanguages) {
        this.resourceLanguages = resourceLanguages;
        return this;
    }

    public OgcApiRecordsRecordCommonPropertiesDto addResourceLanguagesItem(
            OgcApiRecordsLanguageDto resourceLanguagesItem) {
        if (this.resourceLanguages == null) {
            this.resourceLanguages = new ArrayList<>();
        }
        this.resourceLanguages.add(resourceLanguagesItem);
        return this;
    }

    /**
     * The list of languages in which the resource described by this record is available.
     *
     * @return resourceLanguages
     */
    @Valid
    @Schema(
            name = "resourceLanguages",
            description = "The list of languages in which the resource described by this record is available.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("resourceLanguages")
    @JacksonXmlProperty(localName = "resourceLanguages")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "resourceLanguages")
    public List<@Valid OgcApiRecordsLanguageDto> getResourceLanguages() {
        return resourceLanguages;
    }

    public void setResourceLanguages(List<@Valid OgcApiRecordsLanguageDto> resourceLanguages) {
        this.resourceLanguages = resourceLanguages;
    }

    public OgcApiRecordsRecordCommonPropertiesDto externalIds(
            List<@Valid OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto> externalIds) {
        this.externalIds = externalIds;
        return this;
    }

    public OgcApiRecordsRecordCommonPropertiesDto addExternalIdsItem(
            OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto externalIdsItem) {
        if (this.externalIds == null) {
            this.externalIds = new ArrayList<>();
        }
        this.externalIds.add(externalIdsItem);
        return this;
    }

    /**
     * An identifier for the resource assigned by an external (to the catalog) entity.
     *
     * @return externalIds
     */
    @Valid
    @Schema(
            name = "externalIds",
            description = "An identifier for the resource assigned by an external (to the catalog) entity.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("externalIds")
    @JacksonXmlProperty(localName = "externalIds")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "externalIds")
    public List<@Valid OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto> getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(List<@Valid OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto> externalIds) {
        this.externalIds = externalIds;
    }

    public OgcApiRecordsRecordCommonPropertiesDto formats(List<@Valid OgcApiRecordsFormatDto> formats) {
        this.formats = formats;
        return this;
    }

    public OgcApiRecordsRecordCommonPropertiesDto addFormatsItem(OgcApiRecordsFormatDto formatsItem) {
        if (this.formats == null) {
            this.formats = new ArrayList<>();
        }
        this.formats.add(formatsItem);
        return this;
    }

    /**
     * A list of available distributions of the resource.
     *
     * @return formats
     */
    @Valid
    @Schema(
            name = "formats",
            description = "A list of available distributions of the resource.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("formats")
    @JacksonXmlProperty(localName = "formats")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "formats")
    public List<@Valid OgcApiRecordsFormatDto> getFormats() {
        return formats;
    }

    public void setFormats(List<@Valid OgcApiRecordsFormatDto> formats) {
        this.formats = formats;
    }

    public OgcApiRecordsRecordCommonPropertiesDto contacts(List<@Valid OgcApiRecordsContactDto> contacts) {
        this.contacts = contacts;
        return this;
    }

    public OgcApiRecordsRecordCommonPropertiesDto addContactsItem(OgcApiRecordsContactDto contactsItem) {
        if (this.contacts == null) {
            this.contacts = new ArrayList<>();
        }
        this.contacts.add(contactsItem);
        return this;
    }

    /**
     * A list of contacts qualified by their role(s) in association to the record or the resource
     * described by the record.
     *
     * @return contacts
     */
    @Valid
    @Schema(
            name = "contacts",
            description = "A list of contacts qualified by their role(s) in association to the record or the"
                    + " resource described by the record.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("contacts")
    @JacksonXmlProperty(localName = "contacts")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "contacts")
    public List<@Valid OgcApiRecordsContactDto> getContacts() {
        return contacts;
    }

    public void setContacts(List<@Valid OgcApiRecordsContactDto> contacts) {
        this.contacts = contacts;
    }

    public OgcApiRecordsRecordCommonPropertiesDto license(OgcApiRecordsLicenseDto license) {
        this.license = license;
        return this;
    }

    /**
     * Get license
     *
     * @return license
     */
    @Valid
    @Schema(name = "license", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("license")
    @JacksonXmlProperty(localName = "license")
    @XmlElement(name = "license")
    public OgcApiRecordsLicenseDto getLicense() {
        return license;
    }

    public void setLicense(OgcApiRecordsLicenseDto license) {
        this.license = license;
    }

    public OgcApiRecordsRecordCommonPropertiesDto rights(String rights) {
        this.rights = rights;
        return this;
    }

    /**
     * A statement that concerns all rights not addressed by the license such as a copyright
     * statement.
     *
     * @return rights
     */
    @Schema(
            name = "rights",
            description = "A statement that concerns all rights not addressed by the license such as a copyright"
                    + " statement.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("rights")
    @JacksonXmlProperty(localName = "rights")
    @XmlElement(name = "rights")
    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OgcApiRecordsRecordCommonPropertiesDto recordCommonProperties = (OgcApiRecordsRecordCommonPropertiesDto) o;
        return Objects.equals(this.conformsTo, recordCommonProperties.conformsTo)
                && Objects.equals(this.created, recordCommonProperties.created)
                && Objects.equals(this.updated, recordCommonProperties.updated)
                && Objects.equals(this.type, recordCommonProperties.type)
                && Objects.equals(this.title, recordCommonProperties.title)
                && Objects.equals(this.description, recordCommonProperties.description)
                && Objects.equals(this.keywords, recordCommonProperties.keywords)
                && Objects.equals(this.themes, recordCommonProperties.themes)
                && Objects.equals(this.language, recordCommonProperties.language)
                && Objects.equals(this.languages, recordCommonProperties.languages)
                && Objects.equals(this.resourceLanguages, recordCommonProperties.resourceLanguages)
                && Objects.equals(this.externalIds, recordCommonProperties.externalIds)
                && Objects.equals(this.formats, recordCommonProperties.formats)
                && Objects.equals(this.contacts, recordCommonProperties.contacts)
                && Objects.equals(this.license, recordCommonProperties.license)
                && Objects.equals(this.rights, recordCommonProperties.rights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                conformsTo,
                created,
                updated,
                type,
                title,
                description,
                keywords,
                themes,
                language,
                languages,
                resourceLanguages,
                externalIds,
                formats,
                contacts,
                license,
                rights);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsRecordCommonPropertiesDto {\n"
                + "    conformsTo: "
                + toIndentedString(conformsTo)
                + "\n"
                + "    created: "
                + toIndentedString(created)
                + "\n"
                + "    updated: "
                + toIndentedString(updated)
                + "\n"
                + "    type: "
                + toIndentedString(type)
                + "\n"
                + "    title: "
                + toIndentedString(title)
                + "\n"
                + "    description: "
                + toIndentedString(description)
                + "\n"
                + "    keywords: "
                + toIndentedString(keywords)
                + "\n"
                + "    themes: "
                + toIndentedString(themes)
                + "\n"
                + "    language: "
                + toIndentedString(language)
                + "\n"
                + "    languages: "
                + toIndentedString(languages)
                + "\n"
                + "    resourceLanguages: "
                + toIndentedString(resourceLanguages)
                + "\n"
                + "    externalIds: "
                + toIndentedString(externalIds)
                + "\n"
                + "    formats: "
                + toIndentedString(formats)
                + "\n"
                + "    contacts: "
                + toIndentedString(contacts)
                + "\n"
                + "    license: "
                + toIndentedString(license)
                + "\n"
                + "    rights: "
                + toIndentedString(rights)
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

    public static class Builder {

        private OgcApiRecordsRecordCommonPropertiesDto instance;

        public Builder() {
            this(new OgcApiRecordsRecordCommonPropertiesDto());
        }

        protected Builder(OgcApiRecordsRecordCommonPropertiesDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsRecordCommonPropertiesDto value) {
            this.instance.setConformsTo(value.conformsTo);
            this.instance.setCreated(value.created);
            this.instance.setUpdated(value.updated);
            this.instance.setType(value.type);
            this.instance.setTitle(value.title);
            this.instance.setDescription(value.description);
            this.instance.setKeywords(value.keywords);
            this.instance.setThemes(value.themes);
            this.instance.setLanguage(value.language);
            this.instance.setLanguages(value.languages);
            this.instance.setResourceLanguages(value.resourceLanguages);
            this.instance.setExternalIds(value.externalIds);
            this.instance.setFormats(value.formats);
            this.instance.setContacts(value.contacts);
            this.instance.setLicense(value.license);
            this.instance.setRights(value.rights);
            return this;
        }

        public Builder conformsTo(List<String> conformsTo) {
            this.instance.conformsTo(conformsTo);
            return this;
        }

        public Builder created(OffsetDateTime created) {
            this.instance.created(created);
            return this;
        }

        public Builder updated(OffsetDateTime updated) {
            this.instance.updated(updated);
            return this;
        }

        public Builder type(String type) {
            this.instance.type(type);
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

        public Builder keywords(List<String> keywords) {
            this.instance.keywords(keywords);
            return this;
        }

        public Builder themes(List<@Valid OgcApiRecordsThemeDto> themes) {
            this.instance.themes(themes);
            return this;
        }

        public Builder language(OgcApiRecordsLanguageDto language) {
            this.instance.language(language);
            return this;
        }

        public Builder languages(List<@Valid OgcApiRecordsLanguageDto> languages) {
            this.instance.languages(languages);
            return this;
        }

        public Builder resourceLanguages(List<@Valid OgcApiRecordsLanguageDto> resourceLanguages) {
            this.instance.resourceLanguages(resourceLanguages);
            return this;
        }

        public Builder externalIds(List<@Valid OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto> externalIds) {
            this.instance.externalIds(externalIds);
            return this;
        }

        public Builder formats(List<@Valid OgcApiRecordsFormatDto> formats) {
            this.instance.formats(formats);
            return this;
        }

        public Builder contacts(List<@Valid OgcApiRecordsContactDto> contacts) {
            this.instance.contacts(contacts);
            return this;
        }

        public Builder license(OgcApiRecordsLicenseDto license) {
            this.instance.license(license);
            return this;
        }

        public Builder rights(String rights) {
            this.instance.rights(rights);
            return this;
        }

        /**
         * returns a built OgcApiRecordsRecordCommonPropertiesDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsRecordCommonPropertiesDto build() {
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
