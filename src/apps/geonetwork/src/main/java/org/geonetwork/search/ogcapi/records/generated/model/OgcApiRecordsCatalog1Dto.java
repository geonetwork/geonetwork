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
import jakarta.validation.constraints.NotNull;
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

/** OgcApiRecordsCatalog1Dto */
@JsonTypeName("catalog_1")
@JacksonXmlRootElement(localName = "OgcApiRecordsCatalog1Dto")
@XmlRootElement(name = "OgcApiRecordsCatalog1Dto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
public class OgcApiRecordsCatalog1Dto {

  @Valid private List<String> conformsTo = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime created;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updated;

  private TypeEnum type;
  private String title;
  private String description;
  @Valid private List<String> keywords = new ArrayList<>();
  @Valid private List<@Valid OgcApiRecordsThemeDto> themes = new ArrayList<>();
  private OgcApiRecordsLanguageDto language;
  @Valid private List<@Valid OgcApiRecordsLanguageDto> languages = new ArrayList<>();
  @Valid private List<@Valid OgcApiRecordsLanguageDto> resourceLanguages = new ArrayList<>();

  @Valid
  private List<@Valid OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto> externalIds =
      new ArrayList<>();

  @Valid private List<@Valid OgcApiRecordsFormatDto> formats = new ArrayList<>();
  @Valid private List<@Valid OgcApiRecordsContactDto> contacts = new ArrayList<>();
  private OgcApiRecordsLicenseDto license;
  private String rights;
  private String id;
  private OgcApiRecordsCatalog1AllOfItemTypeDto itemType;
  private OgcApiRecordsExtentDto extent;

  @Valid
  private List<String> crs =
      new ArrayList<>(List.of("http://www.opengis.net/def/crs/OGC/1.3/CRS84"));

  private String recordsArrayName = "[records]";
  @Valid private List<@Valid OgcApiRecordsRecordGeoJSONDto> records = new ArrayList<>();
  @Valid private List<@Valid OgcApiRecordsLinkBaseDto> links = new ArrayList<>();
  @Valid private List<OgcApiRecordsLinkTemplateDto> linkTemplates = new ArrayList<>();

  public OgcApiRecordsCatalog1Dto() {
    super();
  }

  /** Constructor with only required parameters */
  public OgcApiRecordsCatalog1Dto(
      TypeEnum type, String id, List<@Valid OgcApiRecordsLinkBaseDto> links) {
    this.type = type;
    this.id = id;
    this.links = links;
  }

  /** Create a builder with no initialized field (except for the default values). */
  public static Builder builder() {
    return new Builder();
  }

  public OgcApiRecordsCatalog1Dto conformsTo(List<String> conformsTo) {
    this.conformsTo = conformsTo;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addConformsToItem(String conformsToItem) {
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

  public OgcApiRecordsCatalog1Dto created(OffsetDateTime created) {
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

  public OgcApiRecordsCatalog1Dto updated(OffsetDateTime updated) {
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

  public OgcApiRecordsCatalog1Dto type(TypeEnum type) {
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

  public OgcApiRecordsCatalog1Dto title(String title) {
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

  public OgcApiRecordsCatalog1Dto description(String description) {
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

  public OgcApiRecordsCatalog1Dto keywords(List<String> keywords) {
    this.keywords = keywords;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addKeywordsItem(String keywordsItem) {
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
      description =
          "The topic or topics of the resource. Typically represented using free-form keywords,"
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

  public OgcApiRecordsCatalog1Dto themes(List<@Valid OgcApiRecordsThemeDto> themes) {
    this.themes = themes;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addThemesItem(OgcApiRecordsThemeDto themesItem) {
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

  public OgcApiRecordsCatalog1Dto language(OgcApiRecordsLanguageDto language) {
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

  public OgcApiRecordsCatalog1Dto languages(List<@Valid OgcApiRecordsLanguageDto> languages) {
    this.languages = languages;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addLanguagesItem(OgcApiRecordsLanguageDto languagesItem) {
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

  public OgcApiRecordsCatalog1Dto resourceLanguages(
      List<@Valid OgcApiRecordsLanguageDto> resourceLanguages) {
    this.resourceLanguages = resourceLanguages;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addResourceLanguagesItem(
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
      description =
          "The list of languages in which the resource described by this record is available.",
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

  public OgcApiRecordsCatalog1Dto externalIds(
      List<@Valid OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto> externalIds) {
    this.externalIds = externalIds;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addExternalIdsItem(
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
      description =
          "An identifier for the resource assigned by an external (to the catalog) entity.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("externalIds")
  @JacksonXmlProperty(localName = "externalIds")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "externalIds")
  public List<@Valid OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto> getExternalIds() {
    return externalIds;
  }

  public void setExternalIds(
      List<@Valid OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto> externalIds) {
    this.externalIds = externalIds;
  }

  public OgcApiRecordsCatalog1Dto formats(List<@Valid OgcApiRecordsFormatDto> formats) {
    this.formats = formats;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addFormatsItem(OgcApiRecordsFormatDto formatsItem) {
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

  public OgcApiRecordsCatalog1Dto contacts(List<@Valid OgcApiRecordsContactDto> contacts) {
    this.contacts = contacts;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addContactsItem(OgcApiRecordsContactDto contactsItem) {
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
      description =
          "A list of contacts qualified by their role(s) in association to the record or the"
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

  public OgcApiRecordsCatalog1Dto license(OgcApiRecordsLicenseDto license) {
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

  public OgcApiRecordsCatalog1Dto rights(String rights) {
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
      description =
          "A statement that concerns all rights not addressed by the license such as a copyright"
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

  public OgcApiRecordsCatalog1Dto id(String id) {
    this.id = id;
    return this;
  }

  /**
   * The identifier of the catalog.
   *
   * @return id
   */
  @NotNull
  @Schema(
      name = "id",
      description = "The identifier of the catalog.",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  @JacksonXmlProperty(localName = "id")
  @XmlElement(name = "id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public OgcApiRecordsCatalog1Dto itemType(OgcApiRecordsCatalog1AllOfItemTypeDto itemType) {
    this.itemType = itemType;
    return this;
  }

  /**
   * Get itemType
   *
   * @return itemType
   */
  @Valid
  @Schema(name = "itemType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemType")
  @JacksonXmlProperty(localName = "itemType")
  @XmlElement(name = "itemType")
  public OgcApiRecordsCatalog1AllOfItemTypeDto getItemType() {
    return itemType;
  }

  public void setItemType(OgcApiRecordsCatalog1AllOfItemTypeDto itemType) {
    this.itemType = itemType;
  }

  public OgcApiRecordsCatalog1Dto extent(OgcApiRecordsExtentDto extent) {
    this.extent = extent;
    return this;
  }

  /**
   * Get extent
   *
   * @return extent
   */
  @Valid
  @Schema(name = "extent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("extent")
  @JacksonXmlProperty(localName = "extent")
  @XmlElement(name = "extent")
  public OgcApiRecordsExtentDto getExtent() {
    return extent;
  }

  public void setExtent(OgcApiRecordsExtentDto extent) {
    this.extent = extent;
  }

  public OgcApiRecordsCatalog1Dto crs(List<String> crs) {
    this.crs = crs;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addCrsItem(String crsItem) {
    if (this.crs == null) {
      this.crs = new ArrayList<>(List.of("http://www.opengis.net/def/crs/OGC/1.3/CRS84"));
    }
    this.crs.add(crsItem);
    return this;
  }

  /**
   * The list of supported coordinate reference systems.
   *
   * @return crs
   */
  @Schema(
      name = "crs",
      description = "The list of supported coordinate reference systems.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crs")
  @JacksonXmlProperty(localName = "crs")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "crs")
  public List<String> getCrs() {
    return crs;
  }

  public void setCrs(List<String> crs) {
    this.crs = crs;
  }

  public OgcApiRecordsCatalog1Dto recordsArrayName(String recordsArrayName) {
    this.recordsArrayName = recordsArrayName;
    return this;
  }

  /**
   * Get recordsArrayName
   *
   * @return recordsArrayName
   */
  @Schema(name = "recordsArrayName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recordsArrayName")
  @JacksonXmlProperty(localName = "recordsArrayName")
  @XmlElement(name = "recordsArrayName")
  public String getRecordsArrayName() {
    return recordsArrayName;
  }

  public void setRecordsArrayName(String recordsArrayName) {
    this.recordsArrayName = recordsArrayName;
  }

  public OgcApiRecordsCatalog1Dto records(List<@Valid OgcApiRecordsRecordGeoJSONDto> records) {
    this.records = records;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addRecordsItem(OgcApiRecordsRecordGeoJSONDto recordsItem) {
    if (this.records == null) {
      this.records = new ArrayList<>();
    }
    this.records.add(recordsItem);
    return this;
  }

  /**
   * An array of records that are part of this catalog that are encoded in-line with the catalog.
   *
   * @return records
   */
  @Valid
  @Schema(
      name = "records",
      description =
          "An array of records that are part of this catalog that are encoded in-line with the"
              + " catalog.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("records")
  @JacksonXmlProperty(localName = "records")
  @JacksonXmlElementWrapper(useWrapping = false)
  @XmlElement(name = "records")
  public List<@Valid OgcApiRecordsRecordGeoJSONDto> getRecords() {
    return records;
  }

  public void setRecords(List<@Valid OgcApiRecordsRecordGeoJSONDto> records) {
    this.records = records;
  }

  public OgcApiRecordsCatalog1Dto links(List<@Valid OgcApiRecordsLinkBaseDto> links) {
    this.links = links;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addLinksItem(OgcApiRecordsLinkBaseDto linksItem) {
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
  public List<@Valid OgcApiRecordsLinkBaseDto> getLinks() {
    return links;
  }

  public void setLinks(List<@Valid OgcApiRecordsLinkBaseDto> links) {
    this.links = links;
  }

  public OgcApiRecordsCatalog1Dto linkTemplates(List<OgcApiRecordsLinkTemplateDto> linkTemplates) {
    this.linkTemplates = linkTemplates;
    return this;
  }

  public OgcApiRecordsCatalog1Dto addLinkTemplatesItem(
      OgcApiRecordsLinkTemplateDto linkTemplatesItem) {
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
    OgcApiRecordsCatalog1Dto catalog1 = (OgcApiRecordsCatalog1Dto) o;
    return Objects.equals(this.conformsTo, catalog1.conformsTo)
        && Objects.equals(this.created, catalog1.created)
        && Objects.equals(this.updated, catalog1.updated)
        && Objects.equals(this.type, catalog1.type)
        && Objects.equals(this.title, catalog1.title)
        && Objects.equals(this.description, catalog1.description)
        && Objects.equals(this.keywords, catalog1.keywords)
        && Objects.equals(this.themes, catalog1.themes)
        && Objects.equals(this.language, catalog1.language)
        && Objects.equals(this.languages, catalog1.languages)
        && Objects.equals(this.resourceLanguages, catalog1.resourceLanguages)
        && Objects.equals(this.externalIds, catalog1.externalIds)
        && Objects.equals(this.formats, catalog1.formats)
        && Objects.equals(this.contacts, catalog1.contacts)
        && Objects.equals(this.license, catalog1.license)
        && Objects.equals(this.rights, catalog1.rights)
        && Objects.equals(this.id, catalog1.id)
        && Objects.equals(this.itemType, catalog1.itemType)
        && Objects.equals(this.extent, catalog1.extent)
        && Objects.equals(this.crs, catalog1.crs)
        && Objects.equals(this.recordsArrayName, catalog1.recordsArrayName)
        && Objects.equals(this.records, catalog1.records)
        && Objects.equals(this.links, catalog1.links)
        && Objects.equals(this.linkTemplates, catalog1.linkTemplates);
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
        rights,
        id,
        itemType,
        extent,
        crs,
        recordsArrayName,
        records,
        links,
        linkTemplates);
  }

  @Override
  public String toString() {
    String sb =
        "class OgcApiRecordsCatalog1Dto {\n"
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
            + "    id: "
            + toIndentedString(id)
            + "\n"
            + "    itemType: "
            + toIndentedString(itemType)
            + "\n"
            + "    extent: "
            + toIndentedString(extent)
            + "\n"
            + "    crs: "
            + toIndentedString(crs)
            + "\n"
            + "    recordsArrayName: "
            + toIndentedString(recordsArrayName)
            + "\n"
            + "    records: "
            + toIndentedString(records)
            + "\n"
            + "    links: "
            + toIndentedString(links)
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
    CATALOG("Catalog");

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

    private OgcApiRecordsCatalog1Dto instance;

    public Builder() {
      this(new OgcApiRecordsCatalog1Dto());
    }

    protected Builder(OgcApiRecordsCatalog1Dto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(OgcApiRecordsCatalog1Dto value) {
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
      this.instance.setId(value.id);
      this.instance.setItemType(value.itemType);
      this.instance.setExtent(value.extent);
      this.instance.setCrs(value.crs);
      this.instance.setRecordsArrayName(value.recordsArrayName);
      this.instance.setRecords(value.records);
      this.instance.setLinks(value.links);
      this.instance.setLinkTemplates(value.linkTemplates);
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

    public Builder type(TypeEnum type) {
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

    public Builder externalIds(
        List<@Valid OgcApiRecordsRecordCommonPropertiesExternalIdsInnerDto> externalIds) {
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

    public Builder id(String id) {
      this.instance.id(id);
      return this;
    }

    public Builder itemType(OgcApiRecordsCatalog1AllOfItemTypeDto itemType) {
      this.instance.itemType(itemType);
      return this;
    }

    public Builder extent(OgcApiRecordsExtentDto extent) {
      this.instance.extent(extent);
      return this;
    }

    public Builder crs(List<String> crs) {
      this.instance.crs(crs);
      return this;
    }

    public Builder recordsArrayName(String recordsArrayName) {
      this.instance.recordsArrayName(recordsArrayName);
      return this;
    }

    public Builder records(List<@Valid OgcApiRecordsRecordGeoJSONDto> records) {
      this.instance.records(records);
      return this;
    }

    public Builder links(List<@Valid OgcApiRecordsLinkBaseDto> links) {
      this.instance.links(links);
      return this;
    }

    public Builder linkTemplates(List<OgcApiRecordsLinkTemplateDto> linkTemplates) {
      this.instance.linkTemplates(linkTemplates);
      return this;
    }

    /**
     * returns a built OgcApiRecordsCatalog1Dto instance.
     *
     * <p>The builder is not reusable (NullPointerException)
     */
    public OgcApiRecordsCatalog1Dto build() {
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
