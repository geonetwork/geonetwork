package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RecordGeoJSONPropertiesDto
 */

@JsonTypeName("recordGeoJSON_properties")
@JacksonXmlRootElement(localName = "RecordGeoJSONPropertiesDto")
@XmlRootElement(name = "RecordGeoJSONPropertiesDto")
@XmlAccessorType(XmlAccessType.FIELD)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]", comments = "Generator version: 7.6.0")
public class RecordGeoJSONPropertiesDto {

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
    private List<@Valid ThemeDto> themes = new ArrayList<>();

    private LanguageDto language;

    @Valid
    private List<@Valid LanguageDto> languages = new ArrayList<>();

    @Valid
    private List<@Valid LanguageDto> resourceLanguages = new ArrayList<>();

    @Valid
    private List<@Valid RecordCommonPropertiesExternalIdsInnerDto> externalIds = new ArrayList<>();

    @Valid
    private List<@Valid FormatDto> formats = new ArrayList<>();

    @Valid
    private List<@Valid ContactDto> contacts = new ArrayList<>();

    private LicenseDto license;

    private String rights;

    /**
     * Create a builder with no initialized field (except for the default values).
     */
    public static Builder builder() {
        return new Builder();
    }

    public RecordGeoJSONPropertiesDto conformsTo(List<String> conformsTo) {
        this.conformsTo = conformsTo;
        return this;
    }

    public RecordGeoJSONPropertiesDto addConformsToItem(String conformsToItem) {
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

    @Schema(name = "conformsTo", description = "The extensions/conformance classes used in this record.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("conformsTo")
    @JacksonXmlProperty(localName = "conformsTo")
    public List<String> getConformsTo() {
        return conformsTo;
    }

    public void setConformsTo(List<String> conformsTo) {
        this.conformsTo = conformsTo;
    }

    public RecordGeoJSONPropertiesDto created(OffsetDateTime created) {
        this.created = created;
        return this;
    }

    /**
     * The date this record was created in the server.
     *
     * @return created
     */
    @Valid
    @Schema(name = "created", description = "The date this record was created in the server.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("created")
    @JacksonXmlProperty(localName = "created")
    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public RecordGeoJSONPropertiesDto updated(OffsetDateTime updated) {
        this.updated = updated;
        return this;
    }

    /**
     * The most recent date on which the record was changed.
     *
     * @return updated
     */
    @Valid
    @Schema(name = "updated", description = "The most recent date on which the record was changed.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("updated")
    @JacksonXmlProperty(localName = "updated")
    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(OffsetDateTime updated) {
        this.updated = updated;
    }

    public RecordGeoJSONPropertiesDto type(String type) {
        this.type = type;
        return this;
    }

    /**
     * The nature or genre of the resource. The value should be a code, convenient for filtering records. Where available, a link to the canonical URI of the record type resource will be added to the 'links' property.
     *
     * @return type
     */

    @Schema(name = "type", description = "The nature or genre of the resource. The value should be a code, convenient for filtering records. Where available, a link to the canonical URI of the record type resource will be added to the 'links' property.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("type")
    @JacksonXmlProperty(localName = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RecordGeoJSONPropertiesDto title(String title) {
        this.title = title;
        return this;
    }

    /**
     * A human-readable name given to the resource.
     *
     * @return title
     */

    @Schema(name = "title", description = "A human-readable name given to the resource.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RecordGeoJSONPropertiesDto description(String description) {
        this.description = description;
        return this;
    }

    /**
     * A free-text account of the resource.
     *
     * @return description
     */

    @Schema(name = "description", description = "A free-text account of the resource.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("description")
    @JacksonXmlProperty(localName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RecordGeoJSONPropertiesDto keywords(List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    public RecordGeoJSONPropertiesDto addKeywordsItem(String keywordsItem) {
        if (this.keywords == null) {
            this.keywords = new ArrayList<>();
        }
        this.keywords.add(keywordsItem);
        return this;
    }

    /**
     * The topic or topics of the resource. Typically represented using free-form keywords, tags, key phrases, or classification codes.
     *
     * @return keywords
     */

    @Schema(name = "keywords", description = "The topic or topics of the resource. Typically represented using free-form keywords, tags, key phrases, or classification codes.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("keywords")
    @JacksonXmlProperty(localName = "keywords")
    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public RecordGeoJSONPropertiesDto themes(List<@Valid ThemeDto> themes) {
        this.themes = themes;
        return this;
    }

    public RecordGeoJSONPropertiesDto addThemesItem(ThemeDto themesItem) {
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
    @Schema(name = "themes", description = "A knowledge organization system used to classify the resource.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("themes")
    @JacksonXmlProperty(localName = "themes")
    public List<@Valid ThemeDto> getThemes() {
        return themes;
    }

    public void setThemes(List<@Valid ThemeDto> themes) {
        this.themes = themes;
    }

    public RecordGeoJSONPropertiesDto language(LanguageDto language) {
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
    public LanguageDto getLanguage() {
        return language;
    }

    public void setLanguage(LanguageDto language) {
        this.language = language;
    }

    public RecordGeoJSONPropertiesDto languages(List<@Valid LanguageDto> languages) {
        this.languages = languages;
        return this;
    }

    public RecordGeoJSONPropertiesDto addLanguagesItem(LanguageDto languagesItem) {
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
    @Schema(name = "languages", description = "This list of languages in which this record is available.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("languages")
    @JacksonXmlProperty(localName = "languages")
    public List<@Valid LanguageDto> getLanguages() {
        return languages;
    }

    public void setLanguages(List<@Valid LanguageDto> languages) {
        this.languages = languages;
    }

    public RecordGeoJSONPropertiesDto resourceLanguages(List<@Valid LanguageDto> resourceLanguages) {
        this.resourceLanguages = resourceLanguages;
        return this;
    }

    public RecordGeoJSONPropertiesDto addResourceLanguagesItem(LanguageDto resourceLanguagesItem) {
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
    @Schema(name = "resourceLanguages", description = "The list of languages in which the resource described by this record is available.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("resourceLanguages")
    @JacksonXmlProperty(localName = "resourceLanguages")
    public List<@Valid LanguageDto> getResourceLanguages() {
        return resourceLanguages;
    }

    public void setResourceLanguages(List<@Valid LanguageDto> resourceLanguages) {
        this.resourceLanguages = resourceLanguages;
    }

    public RecordGeoJSONPropertiesDto externalIds(List<@Valid RecordCommonPropertiesExternalIdsInnerDto> externalIds) {
        this.externalIds = externalIds;
        return this;
    }

    public RecordGeoJSONPropertiesDto addExternalIdsItem(RecordCommonPropertiesExternalIdsInnerDto externalIdsItem) {
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
    @Schema(name = "externalIds", description = "An identifier for the resource assigned by an external (to the catalog) entity.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("externalIds")
    @JacksonXmlProperty(localName = "externalIds")
    public List<@Valid RecordCommonPropertiesExternalIdsInnerDto> getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(List<@Valid RecordCommonPropertiesExternalIdsInnerDto> externalIds) {
        this.externalIds = externalIds;
    }

    public RecordGeoJSONPropertiesDto formats(List<@Valid FormatDto> formats) {
        this.formats = formats;
        return this;
    }

    public RecordGeoJSONPropertiesDto addFormatsItem(FormatDto formatsItem) {
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
    @Schema(name = "formats", description = "A list of available distributions of the resource.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("formats")
    @JacksonXmlProperty(localName = "formats")
    public List<@Valid FormatDto> getFormats() {
        return formats;
    }

    public void setFormats(List<@Valid FormatDto> formats) {
        this.formats = formats;
    }

    public RecordGeoJSONPropertiesDto contacts(List<@Valid ContactDto> contacts) {
        this.contacts = contacts;
        return this;
    }

    public RecordGeoJSONPropertiesDto addContactsItem(ContactDto contactsItem) {
        if (this.contacts == null) {
            this.contacts = new ArrayList<>();
        }
        this.contacts.add(contactsItem);
        return this;
    }

    /**
     * A list of contacts qualified by their role(s) in association to the record or the resource described by the record.
     *
     * @return contacts
     */
    @Valid
    @Schema(name = "contacts", description = "A list of contacts qualified by their role(s) in association to the record or the resource described by the record.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("contacts")
    @JacksonXmlProperty(localName = "contacts")
    public List<@Valid ContactDto> getContacts() {
        return contacts;
    }

    public void setContacts(List<@Valid ContactDto> contacts) {
        this.contacts = contacts;
    }

    public RecordGeoJSONPropertiesDto license(LicenseDto license) {
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
    public LicenseDto getLicense() {
        return license;
    }

    public void setLicense(LicenseDto license) {
        this.license = license;
    }

    public RecordGeoJSONPropertiesDto rights(String rights) {
        this.rights = rights;
        return this;
    }

    /**
     * A statement that concerns all rights not addressed by the license such as a copyright statement.
     *
     * @return rights
     */

    @Schema(name = "rights", description = "A statement that concerns all rights not addressed by the license such as a copyright statement.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("rights")
    @JacksonXmlProperty(localName = "rights")
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
        RecordGeoJSONPropertiesDto recordGeoJSONProperties = (RecordGeoJSONPropertiesDto) o;
        return Objects.equals(this.conformsTo, recordGeoJSONProperties.conformsTo) &&
            Objects.equals(this.created, recordGeoJSONProperties.created) &&
            Objects.equals(this.updated, recordGeoJSONProperties.updated) &&
            Objects.equals(this.type, recordGeoJSONProperties.type) &&
            Objects.equals(this.title, recordGeoJSONProperties.title) &&
            Objects.equals(this.description, recordGeoJSONProperties.description) &&
            Objects.equals(this.keywords, recordGeoJSONProperties.keywords) &&
            Objects.equals(this.themes, recordGeoJSONProperties.themes) &&
            Objects.equals(this.language, recordGeoJSONProperties.language) &&
            Objects.equals(this.languages, recordGeoJSONProperties.languages) &&
            Objects.equals(this.resourceLanguages, recordGeoJSONProperties.resourceLanguages) &&
            Objects.equals(this.externalIds, recordGeoJSONProperties.externalIds) &&
            Objects.equals(this.formats, recordGeoJSONProperties.formats) &&
            Objects.equals(this.contacts, recordGeoJSONProperties.contacts) &&
            Objects.equals(this.license, recordGeoJSONProperties.license) &&
            Objects.equals(this.rights, recordGeoJSONProperties.rights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conformsTo, created, updated, type, title, description, keywords, themes, language, languages, resourceLanguages, externalIds, formats, contacts, license, rights);
    }

    @Override
    public String toString() {
        String sb = "class RecordGeoJSONPropertiesDto {\n" +
            "    conformsTo: " + toIndentedString(conformsTo) + "\n" +
            "    created: " + toIndentedString(created) + "\n" +
            "    updated: " + toIndentedString(updated) + "\n" +
            "    type: " + toIndentedString(type) + "\n" +
            "    title: " + toIndentedString(title) + "\n" +
            "    description: " + toIndentedString(description) + "\n" +
            "    keywords: " + toIndentedString(keywords) + "\n" +
            "    themes: " + toIndentedString(themes) + "\n" +
            "    language: " + toIndentedString(language) + "\n" +
            "    languages: " + toIndentedString(languages) + "\n" +
            "    resourceLanguages: " + toIndentedString(resourceLanguages) + "\n" +
            "    externalIds: " + toIndentedString(externalIds) + "\n" +
            "    formats: " + toIndentedString(formats) + "\n" +
            "    contacts: " + toIndentedString(contacts) + "\n" +
            "    license: " + toIndentedString(license) + "\n" +
            "    rights: " + toIndentedString(rights) + "\n" +
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

        private RecordGeoJSONPropertiesDto instance;

        public Builder() {
            this(new RecordGeoJSONPropertiesDto());
        }

        protected Builder(RecordGeoJSONPropertiesDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(RecordGeoJSONPropertiesDto value) {
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

        public Builder themes(List<@Valid ThemeDto> themes) {
            this.instance.themes(themes);
            return this;
        }

        public Builder language(LanguageDto language) {
            this.instance.language(language);
            return this;
        }

        public Builder languages(List<@Valid LanguageDto> languages) {
            this.instance.languages(languages);
            return this;
        }

        public Builder resourceLanguages(List<@Valid LanguageDto> resourceLanguages) {
            this.instance.resourceLanguages(resourceLanguages);
            return this;
        }

        public Builder externalIds(List<@Valid RecordCommonPropertiesExternalIdsInnerDto> externalIds) {
            this.instance.externalIds(externalIds);
            return this;
        }

        public Builder formats(List<@Valid FormatDto> formats) {
            this.instance.formats(formats);
            return this;
        }

        public Builder contacts(List<@Valid ContactDto> contacts) {
            this.instance.contacts(contacts);
            return this;
        }

        public Builder license(LicenseDto license) {
            this.instance.license(license);
            return this;
        }

        public Builder rights(String rights) {
            this.instance.rights(rights);
            return this;
        }

        /**
         * returns a built RecordGeoJSONPropertiesDto instance.
         * <p>
         * The builder is not reusable (NullPointerException)
         */
        public RecordGeoJSONPropertiesDto build() {
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

