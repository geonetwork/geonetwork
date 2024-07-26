/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import static org.geonetwork.index.model.record.IndexRecordFieldNames.CommonField.DEFAULT_TEXT;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.HIERARCHY_SUFFIX;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.NUMBER_SUFFIX;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Metadata record to be indexed.
 *
 * <p>When using XML, it is required to sort properties to workaround
 * https://github.com/FasterXML/jackson-dataformat-xml/issues/275
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@JacksonXmlRootElement(localName = "indexRecord")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
  IndexRecordFieldNames.DOC_TYPE,
  IndexRecordFieldNames.DOCUMENT,
  IndexRecordFieldNames.METADATA_IDENTIFIER,
  IndexRecordFieldNames.STANDARD_NAME,
  IndexRecordFieldNames.STANDARD_VERSION,
  IndexRecordFieldNames.INDEXING_DATE,
  IndexRecordFieldNames.DATE_STAMP,
  IndexRecordFieldNames.MAIN_LANGUAGE,
  IndexRecordFieldNames.RESOURCE_TYPE,
  IndexRecordFieldNames.RESOURCE_TYPE_NAME,
  IndexRecordFieldNames.RESOURCE_TITLE,
  IndexRecordFieldNames.RESOURCE_ALT_TITLE,
  IndexRecordFieldNames.RESOURCE_DATE,
  IndexRecordFieldNames.RESOURCE_TEMPORAL_DATE_RANGE,
  IndexRecordFieldNames.RESOURCE_IDENTIFIER,
  IndexRecordFieldNames.RESOURCE_ABSTRACT,
  IndexRecordFieldNames.SUPPLEMENTAL_INFORMATION,
  IndexRecordFieldNames.PURPOSE,
  IndexRecordFieldNames.HAS_OVERVIEW,
  IndexRecordFieldNames.TAG,
  IndexRecordFieldNames.IS_OPEN_DATA,
  IndexRecordFieldNames.ALL_KEYWORDS,
  IndexRecordFieldNames.RESOLUTION_DISTANCE,
  IndexRecordFieldNames.RESOLUTION_SCALE_DENOMINATOR,
  IndexRecordFieldNames.EXTENT_IDENTIFIER,
  IndexRecordFieldNames.EXTENT_DESCRIPTION,
  IndexRecordFieldNames.SHAPE,
  IndexRecordFieldNames.SHAPE_PARSING_ERROR,
  IndexRecordFieldNames.GEOM,
  IndexRecordFieldNames.LOCATION,
  IndexRecordFieldNames.RESOURCE_TEMPORAL_EXTENT_DATE_RANGE,
  IndexRecordFieldNames.RESOURCE_TEMPORAL_EXTENT_DATE_RANGE_DETAILS,
  IndexRecordFieldNames.RESOURCE_VERTICAL_RANGE,
  IndexRecordFieldNames.COORDINATE_SYSTEM,
  IndexRecordFieldNames.CRS_DETAILS,
  IndexRecordFieldNames.FEATURE_TYPES,
  IndexRecordFieldNames.LINK,
  IndexRecordFieldNames.RESOURCE_LINEAGE,
  IndexRecordFieldNames.HASSOURCE,
  IndexRecordFieldNames.RECORD_LINK,
  IndexRecordFieldNames.SOURCE_DESCRIPTION,
  IndexRecordFieldNames.PROCESS_STEPS,
  IndexRecordFieldNames.MEASURE,
  IndexRecordFieldNames.FORMAT,
  IndexRecordFieldNames.ORDERING_INSTRUCTIONS,
  IndexRecordFieldNames.RECORD_GROUP,
  IndexRecordFieldNames.PARENT_UUID,
  IndexRecordFieldNames.RECORD_OWNER,
  IndexRecordFieldNames.UUID,
  IndexRecordFieldNames.HARVESTER_UUID,
  IndexRecordFieldNames.GROUP_PUBLISHED_ID,
  IndexRecordFieldNames.POPULARITY,
  IndexRecordFieldNames.USER_INFO,
  IndexRecordFieldNames.IS_PUBLISHED_TO_ALL,
  IndexRecordFieldNames.DRAFT,
  IndexRecordFieldNames.CHANGE_DATE,
  IndexRecordFieldNames.ID,
  IndexRecordFieldNames.CREATE_DATE,
  IndexRecordFieldNames.IS_PUBLISHED_TO_INTRANET,
  IndexRecordFieldNames.SCHEMA,
  IndexRecordFieldNames.VALID,
  IndexRecordFieldNames.IS_TEMPLATE,
  IndexRecordFieldNames.FEEDBACK_COUNT,
  IndexRecordFieldNames.RATING,
  IndexRecordFieldNames.IS_HARVESTED,
  IndexRecordFieldNames.USER_SAVED_COUNT,
  IndexRecordFieldNames.SOURCE_CATALOGUE,
  IndexRecordFieldNames.OVERVIEW,
  IndexRecordFieldNames.UUID
})
@Slf4j(topic = "org.geonetwork.index.model")
public class IndexRecord {

  /**
   *
   *
   * <pre>
   *         "MD_LegalConstraintsOtherConstraintsObject": [{
   *             "default": "No limitations to public access",
   *             "langfre": "No limitations to public access",
   *             "link": "http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/noLimitations"
   * </pre>
   */
  @JsonIgnore
  @Singular("constraints")
  final Map<String, List<Map<String, String>>> constraints = new HashMap<>();

  @JsonIgnore
  @Singular("useLimitations")
  final Map<String, List<Map<String, String>>> useLimitations = new HashMap<>();

  @JsonIgnore final Map<String, String> conformsTo = new HashMap<>();

  @JsonProperty(IndexRecordFieldNames.LICENSE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  @Singular
  final List<Map<String, String>> licenses = new ArrayList<>();

  @JsonIgnore private final Map<String, ArrayList<String>> resourceDateDetails = new HashMap<>();

  @JsonIgnore
  private final Map<String, List<Map<String, String>>> orgForResourceByRole = new HashMap<>();

  // others eg. pointOfContactOrgForResource are in other properties
  // ForResource
  // ForProcessing
  // ForDistribution
  @JsonIgnore private final Map<String, List<Contact>> contactByRole = new HashMap<>();

  @JsonIgnore
  @Singular("associatedUuidsByType")
  private final Map<String, List<String>> associatedUuidsByType = new HashMap<>();

  // "measure_Exactitudeglobale": "83.80 %",
  @JsonIgnore @Singular private final Map<String, List<String>> measureFields = new HashMap<>();

  //  "recordLink_sources": [
  //          "Orthophotos 2020",
  //          "Relief de la Wallonie - Mod\\u00E8le Num\\u00E9rique de Terrain (MNT) 2013-2014"
  //  ],
  //  "recordLink_sources_uuid": [
  //          "7369222c-5241-452a-af07-4929506212f9",
  //          "6029e738-f828-438b-b10a-85e67f77af92"
  //  ],
  //  "recordLink_sources_url": [
  @JsonIgnore @Singular
  private final Map<String, ArrayList<String>> associatedResourceFields = new HashMap<>();

  @JsonIgnore @Singular
  private final Map<String, ArrayList<String>> linkByProtocols = new HashMap<>();

  /**
   * Number of keyword per thesaurus.
   *
   * <pre>
   *     "th_otherKeywords-Number": "7",
   *         "th_otherKeywords-": [
   *     {
   *         "default": "Occupation du sol",
   *         "langfre": "Occupation du sol"
   *     },
   *     </pre>
   */
  @JsonIgnore
  @Singular("keywordByThesaurus")
  private final Map<String, List<Keyword>> keywordByThesaurus = new HashMap<>();

  @JsonIgnore
  @Singular("numberOfKeywordByThesaurus")
  private final Map<String, Integer> numberOfKeywordByThesaurus = new HashMap<>();

  /**
   *
   *
   * <pre>
   *      "th_httpinspireeceuropaeutheme-theme": {
   *         "id": "geonetwork.thesaurus.external.theme.httpinspireeceuropaeutheme-theme",
   *         "title": "GEMET - INSPIRE themes, version 1.0",
   *         "theme": "theme",
   *         "link": "https://metawal.wallonie.be/geonetwork/srv/api/registries/vocabularies/external.theme.httpinspireeceuropaeutheme-theme",
   *         "keywords": [{
   *             "default": "Occupation des terres",
   *                 "langfre": "Occupation des terres",
   *                 "link": "http://inspire.ec.europa.eu/theme/lc"
   *         }
   * </pre>
   */
  private final Map<String, Thesaurus> allKeywords = new HashMap<>();

  /**
   *
   *
   * <pre>
   *         "th_Themes_geoportail_wallon_hierarchy_tree": {
   *             "default": [
   *                     "Aménagement du territoire",
   *                     "Données de base",
   *                     "Nature et environnement",
   *                     "Nature et environnement^Sol et sous-sol"
   *             ],
   *             "key": [
   *                     "https://metawal.wallonie.be/thesaurus/theme-geoportail-wallon#ThemesGeoportailWallon/10",
   *                     "https://metawal.wallonie.be/thesaurus/theme-geoportail-wallon#ThemesGeoportailWallon/10^https://metawal.wallonie.be/thesaurus/theme-geoportail-wallon#SubThemesGeoportailWallon/1030",
   *                     "https://metawal.wallonie.be/thesaurus/theme-geoportail-wallon#ThemesGeoportailWallon/20",
   *                     "https://metawal.wallonie.be/thesaurus/theme-geoportail-wallon#ThemesGeoportailWallon/50"
   *         ]
   *         },
   *     </pre>
   */
  @JsonIgnore
  @Singular("keywordHierarchyByThesaurus")
  private final Map<String, KeywordHierarchy> keywordHierarchyByThesaurus = new HashMap<>();

  @JsonIgnore
  @Singular("operation")
  @Builder.ObtainVia(method = "copyOperations")
  private Map<String, ArrayList<Integer>> operations;

  private Map<String, ArrayList<Integer>> copyOperations() {
    return operations != null ? operations : new HashMap<>();
  }

  @Singular private final Map<String, ArrayList<String>> otherProperties = new HashMap<>();

  /**
   * Record title.
   *
   * <pre>
   *     resourceTitleObject: {
   *         default: "Organisation de la protection civile dans
   *         le canton de Fribourg - Organisation des Zivilschutzes im Kanton Freiburg",
   *             langfre: "Organisation de la protection civile dans le canton de Fribourg
   *             - Organisation des Zivilschutzes im Kanton Freiburg"
   * </pre>
   */
  @JsonProperty(IndexRecordFieldNames.RESOURCE_TITLE)
  Map<String, String> resourceTitle;

  @JsonProperty(IndexRecordFieldNames.RESOURCE_ALT_TITLE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  List<HashMap<String, String>> resourceAltTitle;

  @JsonProperty(IndexRecordFieldNames.RESOURCE_LINEAGE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  List<HashMap<String, String>> resourceLineage;

  @JsonProperty(IndexRecordFieldNames.SOURCE_DESCRIPTION)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  List<HashMap<String, String>> sourceDescription;

  @JsonProperty(IndexRecordFieldNames.PROCESS_STEPS)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  List<ProcessStep> processSteps;

  @JsonProperty(IndexRecordFieldNames.RESOURCE_ABSTRACT)
  Map<String, String> resourceAbstract;

  @JsonProperty(IndexRecordFieldNames.RESOURCE_CREDIT)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  List<HashMap<String, String>> resourceCredit;

  @JsonProperty(IndexRecordFieldNames.SUPPLEMENTAL_INFORMATION)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  List<HashMap<String, String>> supplementalInformation;

  @JsonProperty(IndexRecordFieldNames.ORDERING_INSTRUCTIONS)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  List<HashMap<String, String>> orderingInstructions;

  @JsonProperty(IndexRecordFieldNames.PURPOSE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  List<HashMap<String, String>> purpose;

  private List<HashMap<String, String>> copyPurpose() {
    return purpose != null ? purpose : new ArrayList<>();
  }

  @JsonProperty(IndexRecordFieldNames.CATEGORY)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  @Singular("category")
  @Builder.ObtainVia(method = "copyCategory")
  private List<String> category;

  private List<String> copyCategory() {
    return category != null ? category : new ArrayList<>();
  }

  @JsonProperty(IndexRecordFieldNames.TAG)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  ArrayList<HashMap<String, String>> tag;

  @JsonProperty(IndexRecordFieldNames.TAG_NUMBER)
  int tagNumber;

  @JsonProperty(IndexRecordFieldNames.ID)
  private Integer id;

  @JsonProperty(IndexRecordFieldNames.UUID)
  private String uuid;

  @JsonProperty(IndexRecordFieldNames.METADATA_IDENTIFIER)
  private String metadataIdentifier;

  private IndexDocumentType docType;
  // Unused for now. Was used to store XML document.
  private String document;
  // eg. iso19139
  private String documentStandard;

  // eg. ISO 19119/2005
  @JsonProperty(IndexRecordFieldNames.STANDARD_NAME)
  private Map<String, String> standardName;

  @JsonProperty(IndexRecordFieldNames.STANDARD_VERSION)
  private Map<String, String> standardVersion;

  @JsonProperty(IndexRecordFieldNames.DRAFT)
  private String draft;

  @JsonProperty(IndexRecordFieldNames.LAST_WORKFLOW_STATUS)
  private String lastWorkflowStatus;

  @JsonProperty(IndexRecordFieldNames.IS_TEMPLATE)
  private Character isTemplate;

  private String root;

  private String indexingDate;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<IndexingErrorMsg> indexingErrorMsg;

  private String dateStamp;

  @JsonProperty(IndexRecordFieldNames.CHANGE_DATE)
  private String changeDate;

  @JsonProperty(IndexRecordFieldNames.CREATE_DATE)
  private String createDate;

  @JsonProperty(IndexRecordFieldNames.RESOURCE_DATE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<ResourceDate> resourceDate;

  private Integer owner;
  private Integer groupOwner;
  private String recordOwner;

  @JsonProperty(IndexRecordFieldNames.USER_INFO)
  private String userinfo;

  @JsonProperty(IndexRecordFieldNames.SOURCE_CATALOGUE)
  private String sourceCatalogue;

  private String logo;

  @JsonProperty(IndexRecordFieldNames.IS_PUBLISHED_TO_ALL)
  private boolean publishedToAll;

  @JsonProperty(IndexRecordFieldNames.IS_PUBLISHED_TO_INTRANET)
  private boolean publishedToIntranet;

  @JsonProperty(IndexRecordFieldNames.IS_PUBLISHED_TO_GUEST)
  private boolean publishedToGuest;

  @JsonProperty(IndexRecordFieldNames.GROUP_PUBLISHED)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  @Singular("groupPublished")
  @Builder.ObtainVia(method = "copyGroupPublished")
  private List<String> groupPublished;

  private List<String> copyGroupPublished() {
    return groupPublished != null ? groupPublished : new ArrayList<>();
  }

  @JsonProperty(IndexRecordFieldNames.GROUP_PUBLISHED_ID)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  @Singular("groupPublishedId")
  @Builder.ObtainVia(method = "copyGroupPublishedId")
  private List<Integer> groupPublishedId;

  private List<Integer> copyGroupPublishedId() {
    return groupPublishedId != null ? groupPublishedId : new ArrayList<>();
  }

  @JsonProperty(IndexRecordFieldNames.POPULARITY)
  private int popularity;

  @JsonProperty(IndexRecordFieldNames.MAIN_LANGUAGE)
  private String mainLanguage;

  @JsonProperty(IndexRecordFieldNames.RESOURCE_TYPE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> resourceType;

  @JsonProperty(IndexRecordFieldNames.RESOURCE_TYPE_NAME)
  private Map<String, String> resourceTypeName;

  @JsonProperty(IndexRecordFieldNames.VALID)
  private Integer valid;

  @JsonIgnore
  @Singular("validation")
  @Builder.ObtainVia(method = "copyValidationByType")
  private Map<String, String> validationByType;

  private Map<String, String> copyValidationByType() {
    return validationByType != null ? validationByType : new HashMap<>();
  }

  @JsonProperty(IndexRecordFieldNames.FEEDBACK_COUNT)
  private long feedbackCount;

  @JsonProperty(IndexRecordFieldNames.USER_SAVED_COUNT)
  private long userSavedCount;

  @JsonProperty(IndexRecordFieldNames.RATING)
  private Integer rating;

  @JsonProperty(IndexRecordFieldNames.IS_HARVESTED)
  private boolean harvested;

  @JsonProperty(IndexRecordFieldNames.HARVESTER_UUID)
  private String harvesterUuid;

  @JsonProperty(IndexRecordFieldNames.HAS_XLINKS)
  private Boolean hasxlinks;

  @JsonProperty(IndexRecordFieldNames.IS_OPEN_DATA)
  private Boolean isOpenData;

  @JsonProperty(IndexRecordFieldNames.INSPIRE_CONFORM_RESOURCE)
  @JsonDeserialize(using = NumericOrStringBooleanSerializer.class)
  private Boolean inspireConformResource;

  @JsonProperty(IndexRecordFieldNames.INSPIRE_VALID)
  private int inspireValid;

  @JsonProperty(IndexRecordFieldNames.INSPIRE_REPORT_URL)
  private String inspireReportUrl;

  @JsonProperty(IndexRecordFieldNames.INSPIRE_VALIDATION_DATE)
  private String inspireValidationDate;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> otherLanguage;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> otherLanguageId;

  @JsonProperty(IndexRecordFieldNames.RESOURCE_LANGUAGE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> resourceLanguage;

  @JsonProperty(IndexRecordFieldNames.RESOURCE_IDENTIFIER)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private ArrayList<ResourceIdentifier> resourceIdentifier;

  private String resourceEdition;

  /** Unused. */
  private Integer displayOrder;

  @JsonProperty(IndexRecordFieldNames.ORG)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<HashMap<String, String>> organizations;

  @JsonProperty(IndexRecordFieldNames.FORMAT)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> formats;

  @JsonProperty(IndexRecordFieldNames.COORDINATE_SYSTEM)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> coordinateSystem;

  @JsonProperty(IndexRecordFieldNames.CRS_DETAILS)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<CrsDetails> coordinateSystemDetails;

  @JsonProperty(IndexRecordFieldNames.OVERVIEW)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Overview> overview;

  @JsonProperty(IndexRecordFieldNames.RECORD_LINK)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<RecordLink> associatedRecords;

  @JsonProperty(IndexRecordFieldNames.RECORD_OPERATE_ON)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> recordOperateOn;

  @JsonProperty(IndexRecordFieldNames.AGG_ASSOCIATED)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> associatedUuids;

  @JsonProperty(IndexRecordFieldNames.CHILD_UUID)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> childUuid;

  @JsonProperty(IndexRecordFieldNames.PARENT_UUID)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> parentUuid;

  /** Same as parent. */
  @JsonProperty(IndexRecordFieldNames.RECORD_GROUP)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> recordGroup;

  @JsonProperty(IndexRecordFieldNames.MEASURE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Measure> measures;

  @JsonProperty(IndexRecordFieldNames.LINK)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Link> links;

  @JsonProperty(IndexRecordFieldNames.LINK_URL)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> linkUrls;

  @JsonProperty(IndexRecordFieldNames.LINK_PROTOCOL)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private Set<String> linkProtocols;

  @JsonProperty(IndexRecordFieldNames.RESOLUTION_SCALE_DENOMINATOR)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Float> resolutionScaleDenominator;

  @JsonProperty(IndexRecordFieldNames.RESOLUTION_DISTANCE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> resolutionDistance;

  /**
   * Temporal date range for the resource.
   *
   * <pre>
   *   resourceTemporalDateRange: [{
   *         gte: "2017-02-03T14:00:00",
   *         lte: "2017-02-03T14:00:00"
   * </pre>
   */
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<DateRange> resourceTemporalDateRange;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<DateRange> resourceTemporalExtentDateRange;

  @JsonProperty(IndexRecordFieldNames.RESOURCE_TEMPORAL_EXTENT_DATE_RANGE_DETAILS)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<DateRangeDetails> resourceTemporalExtentDetails;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<VerticalRange> resourceVerticalRange;

  @JsonProperty(IndexRecordFieldNames.LOCATION)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> locations;

  @JsonProperty(IndexRecordFieldNames.EXTENT_IDENTIFIER)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<HashMap<String, String>> extentIdentifier;

  @JsonProperty(IndexRecordFieldNames.EXTENT_DESCRIPTION)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<HashMap<String, String>> extentDescription;

  @JsonProperty(IndexRecordFieldNames.SHAPE_PARSING_ERROR)
  private List<String> shapeParsingError;

  @JsonProperty(IndexRecordFieldNames.SHAPE)
  @JsonDeserialize(using = NodeTreeAsStringDeserializer.class)
  @JsonSerialize(using = StringAsNodeTreeSerializer.class)
  private List<String> shapes;

  @JsonProperty(IndexRecordFieldNames.GEOM)
  @JsonDeserialize(using = NodeTreeAsStringDeserializer.class)
  @JsonSerialize(using = StringAsNodeTreeSerializer.class)
  private List<String> geometries;

  @JsonProperty(IndexRecordFieldNames.SERVICE_TYPE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> serviceType;

  @JsonProperty(IndexRecordFieldNames.SERVICE_TYPE_VERSION)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> serviceTypeVersion;

  @JsonIgnore
  @Singular("codelist")
  @Builder.ObtainVia(method = "copyCodelist")
  private Map<String, ArrayList<Codelist>> codelists;

  private Map<String, ArrayList<Codelist>> copyCodelist() {
    return codelists != null ? codelists : new HashMap<>();
  }

  /**
   *
   *
   * <pre>
   *         "keywordType-theme": [{
   *             "default": "Régional",
   *             "langfre": "Régional",
   *             "link": "http://inspire.ec.europa.eu/metadata-codelist/SpatialScope/regional"
   *         },
   *      </pre>
   */
  @JsonIgnore
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(localName = "keywords")
  @Singular("keywordByType")
  @Builder.ObtainVia(method = "copyKeywordByType")
  private Map<String, List<Keyword>> keywordByType;

  private Map<String, List<Keyword>> copyKeywordByType() {
    return keywordByType != null ? keywordByType : new HashMap<>();
  }

  @JsonProperty(IndexRecordFieldNames.SPECIFICATION_CONFORMANCE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  @Singular("specificationConformance")
  @Builder.ObtainVia(method = "copySpecificationConformance")
  private List<SpecificationConformance> specificationConformance;

  private List<SpecificationConformance> copySpecificationConformance() {
    return specificationConformance != null ? specificationConformance : new ArrayList<>();
  }

  @JsonProperty(IndexRecordFieldNames.FEATURE_TYPES)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<FeatureType> featureTypes;

  @JsonProperty(IndexRecordFieldNames.HASFEATURECAT)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<String> hasfeaturecat;

  @JsonProperty(IndexRecordFieldNames.HASSOURCE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  @JacksonXmlProperty(localName = IndexRecordFieldNames.HASSOURCE)
  @JacksonXmlElementWrapper(useWrapping = false)
  @Singular("hassource")
  @Builder.ObtainVia(method = "copyHasSource")
  private List<String> hassource;

  private List<String> copyHasSource() {
    return hassource != null ? hassource : new ArrayList<>();
  }

  private static boolean isDateField(String name) {
    return name.endsWith("Date" + IndexRecordFieldNames.FOR_RESOURCE_SUFFIX)
        || name.endsWith("Year" + IndexRecordFieldNames.FOR_RESOURCE_SUFFIX)
        || name.endsWith("Month" + IndexRecordFieldNames.FOR_RESOURCE_SUFFIX);
  }

  private static boolean isKeywordHierarchyField(String name) {
    return name.startsWith(IndexRecordFieldNames.KEYWORD_BY_THESAURUS_PREFIX)
        && name.endsWith(HIERARCHY_SUFFIX);
  }

  private static boolean isKeywordNumberField(String name) {
    return name.startsWith(IndexRecordFieldNames.KEYWORD_BY_THESAURUS_PREFIX)
        && name.endsWith(NUMBER_SUFFIX);
  }

  /** Unwrapped all other properties and all fields stored in Map on serialization. */
  @JsonAnyGetter
  public Map<String, Object> getOtherProperties() {
    Map<String, Object> allFieldsInStoredAsMap = new HashMap<>();
    Stream.of(
            otherProperties,
            codelists,
            keywordByType,
            keywordByThesaurus,
            keywordHierarchyByThesaurus,
            numberOfKeywordByThesaurus,
            conformsTo,
            constraints,
            useLimitations,
            associatedUuidsByType,
            orgForResourceByRole,
            contactByRole,
            measureFields,
            associatedResourceFields,
            resourceDateDetails,
            linkByProtocols,
            validationByType,
            operations)
        .filter(Objects::nonNull)
        .forEach(allFieldsInStoredAsMap::putAll);
    return allFieldsInStoredAsMap;
  }

  /**
   * Collect all other properties in proper Map fields. The getOtherProperties is used to serialize
   * all those properties in the JSON.
   */
  @JsonAnySetter
  public void handleUnrecognizedField(String name, Object value) {
    try {
      @SuppressWarnings({"ReturnValueIgnored", "UnusedVariable"})
      Field declaredField = IndexRecord.class.getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      try {
        if (name.startsWith(IndexRecordFieldNames.OP_PREFIX)) {
          handleOperationsProperties(name, value);
        } else if (name.endsWith(IndexRecordFieldNames.ORG_FOR_RESOURCE_SUFFIX)) {
          handleOrgForResourceProperties(orgForResourceByRole, name, value);
        } else if (name.endsWith(IndexRecordFieldNames.ORG_SUFFIX)) {
          handleOrgForResourceProperties(orgForResourceByRole, name, value);
        } else if (name.contains(IndexRecordFieldNames.ORG_FOR_PREFIX)) {
          handleOrgForResourceProperties(orgForResourceByRole, name, value);
        } else if (name.contains(IndexRecordFieldNames.CONTACT_FOR_PREFIX)
            || name.equals(IndexRecordFieldNames.CONTACT)) {
          handleContactByRoleProperties(contactByRole, name, value);
        } else if (name.startsWith(IndexRecordFieldNames.Codelists.PREFIX)) {
          handleCodelistProperties(name, value);
        } else if (name.startsWith(IndexRecordFieldNames.CONFORMS_TO_PREFIX)) {
          handleConformProperties(name, value);
        } else if (name.startsWith(IndexRecordFieldNames.RECORD_LINK_PREFIX)) {
          handleRecordLinkProperties(name, value);
        } else if (name.startsWith(IndexRecordFieldNames.AGG_ASSOCIATED_PREFIX)) {
          handleListOfUuidProperties(associatedUuidsByType, name, value);
        } else if (name.startsWith(IndexRecordFieldNames.MEASURE_PREFIX)) {
          handleListOfUuidProperties(measureFields, name, value);
        } else if (name.startsWith(IndexRecordFieldNames.LINK_URL_PROTOCOL_PREFIX)) {
          handleLinkUrlProperties(name, value);
        } else if (name.startsWith(IndexRecordFieldNames.KEYWORD_BY_TYPE_PREFIX)) {
          handleKeywordByTypeProperties(keywordByType, name, value);
        } else if (isKeywordNumberField(name)) {
          handleKeywordNumberProperties(name, value);
        } else if (isKeywordHierarchyField(name)) {
          handleKeywordHierarchyProperties(name, value);
        } else if (name.startsWith(IndexRecordFieldNames.KEYWORD_BY_THESAURUS_PREFIX)) {
          handleKeywordByTypeProperties(keywordByThesaurus, name, value);
        } else if (name.endsWith(IndexRecordFieldNames.CONSTRAINT_SUFFIX)) {
          handleConstraintProperties(constraints, name, value);
        } else if (name.endsWith(IndexRecordFieldNames.USE_LIMITATION_SUFFIX)) {
          handleConstraintProperties(useLimitations, name, value);
        } else if (isDateField(name)) {
          handleDateProperties(name, value);
        } else if (name.isEmpty()) {
          log.atWarn()
              .log(
                  "Deserializing index object warning. No field name for value {}. "
                      + "This property is ignored. Check the input document for {}",
                  value,
                  this.uuid);
        } else {
          handleOtherProperties(name, value);
        }
      } catch (Exception fieldException) {
        log.atWarn()
            .log(
                "Error while handling field {} with value {} for record {}. {}",
                name,
                value,
                this.uuid,
                fieldException.getMessage());
      }
    }
  }

  private void handleOperationsProperties(String name, Object value) {
    ArrayList<Integer> operationField =
        this.copyOperations().computeIfAbsent(name, k -> new ArrayList<>());
    if (value instanceof List) {
      @SuppressWarnings("unchecked")
      Collection<? extends Integer> collection = (Collection<? extends Integer>) value;
      operationField.addAll(collection);
    }
    if (value instanceof String) {
      operationField.add(Integer.parseInt(value.toString()));
    }
  }

  private void handleOtherProperties(String name, Object value) {
    ArrayList<String> s = otherProperties.get(name);
    if (s == null) {
      s = new ArrayList<>(1);
      s.add(value.toString());
      otherProperties.put(name, s);
    } else {
      s.add(value.toString());
    }
  }

  private void handleDateProperties(String name, Object value) {
    List<String> resourceDateForField =
        resourceDateDetails.computeIfAbsent(name, k -> new ArrayList<String>());
    if (value instanceof List) {
      @SuppressWarnings("unchecked")
      Collection<? extends String> collection = (Collection<? extends String>) value;
      resourceDateForField.addAll(collection);
    }
    if (value instanceof String) {
      resourceDateForField.add(value.toString());
    }
  }

  private void handleConstraintProperties(
      Map<String, List<Map<String, String>>> constraints, String name, Object value) {
    List<Map<String, String>> constraintByType =
        constraints.computeIfAbsent(name, k -> new ArrayList<>());
    if (value instanceof Map) {
      @SuppressWarnings("unchecked")
      HashMap<String, String> map = (HashMap<String, String>) value;
      constraintByType.add(map);
    } else if (value instanceof List) {
      @SuppressWarnings("unchecked")
      List<HashMap<String, String>> list = (List<HashMap<String, String>>) value;
      constraintByType.addAll(list);
    }
  }

  private void handleOrgForResourceProperties(
      Map<String, List<Map<String, String>>> orgForResourceByRole, String name, Object value) {
    List<Map<String, String>> resourceByRole =
        orgForResourceByRole.computeIfAbsent(name, k -> new ArrayList<>());

    if (value instanceof Map) {
      @SuppressWarnings("unchecked")
      Map<String, String> map = (Map<String, String>) value;
      resourceByRole.add(map);
    }
  }

  private void handleContactByRoleProperties(
      Map<String, List<Contact>> contactByRoleList, String name, Object value) {
    List<Contact> resourceByRole = contactByRoleList.computeIfAbsent(name, k -> new ArrayList<>());
    if (value instanceof List) {
      @SuppressWarnings("unchecked")
      List<Contact> list = (List<Contact>) value;
      resourceByRole.addAll(list);
    } else if (value instanceof Map) {
      @SuppressWarnings("unchecked")
      Map<String, Object> contactInfo = (Map<String, Object>) value;

      Contact.ContactBuilder c =
          Contact.builder()
              .role(contactInfo.get("role").toString())
              .email(contactInfo.get("email").toString())
              .website(contactInfo.get("website").toString())
              .logo(contactInfo.get("logo").toString())
              .individual(contactInfo.get("individual").toString())
              .position(contactInfo.get("position").toString())
              .phone(contactInfo.get("phone").toString())
              .address(contactInfo.get("address").toString());

      if (contactInfo.get("organisationObject") instanceof Map) {
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) contactInfo.get("organisationObject");
        c.organisation(map);
      }
      if (contactInfo.get("nilReason") != null) {
        c.nilReason(contactInfo.get("nilReason").toString());
      }

      Object identifiers = contactInfo.get("identifiers");
      List<Map<String, String>> listOfPartyIdentifier = new ArrayList<>();
      if (identifiers instanceof Map) {
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) identifiers;
        listOfPartyIdentifier = List.of(map);
      } else if (identifiers instanceof List) {
        @SuppressWarnings("unchecked")
        List<Map<String, String>> list = (List<Map<String, String>>) identifiers;

        listOfPartyIdentifier = list;
      }
      if (!listOfPartyIdentifier.isEmpty()) {
        c.identifier(
            listOfPartyIdentifier.stream()
                .map(
                    identifierProperties ->
                        PartyIdentifier.builder()
                            .code(identifierProperties.get("code"))
                            .codeSpace(identifierProperties.get("codeSpace"))
                            .link(identifierProperties.get("link"))
                            .build())
                .toList());
      }

      resourceByRole.add(c.build());
    } else {
      resourceByRole.add((Contact) value);
    }
  }

  private void handleKeywordHierarchyProperties(String name, Object value) {
    if (value instanceof Map) {
      @SuppressWarnings("unchecked")
      Map<String, ArrayList<String>> map = (Map<String, ArrayList<String>>) value;
      keywordHierarchyByThesaurus.put(
          name, new KeywordHierarchy(map.get("default"), map.get("key")));
    }
  }

  private void handleKeywordNumberProperties(String name, Object value) {
    if (value instanceof List) {
      @SuppressWarnings("unchecked")
      List<Integer> list = (List<Integer>) value;

      numberOfKeywordByThesaurus.put(
          name,
          list.size()); // This should not happen or indicate that a thesaurus is used 2 times in
      // the same record.
    } else {
      numberOfKeywordByThesaurus.put(name, Integer.parseInt(value.toString()));
    }
  }

  private void handleKeywordByTypeProperties(
      Map<String, List<Keyword>> keywordField, String name, Object value) {
    List<Keyword> keywordForType =
        copyKeywordByType().computeIfAbsent(name, k -> new ArrayList<Keyword>());

    if (value instanceof Map) {
      Object listOfKeywords = ((Map) value).get("keywords"); // XML
      if (listOfKeywords instanceof List) {
        ((List<?>) listOfKeywords)
            .forEach(
                k -> {
                  if (k instanceof String && StringUtils.isNotEmpty(k.toString())) {
                    keywordForType.add(
                        Keyword.builder().property(DEFAULT_TEXT, k.toString()).build());
                  } else if (k instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> map = (Map<String, String>) k;
                    keywordForType.add(Keyword.builder().properties(map).build());
                  }
                });
      }
    } else if (value instanceof List) {
      @SuppressWarnings("unchecked")
      List<HashMap<String, String>> list = (List<HashMap<String, String>>) value;
      list.stream().map(Keyword::new).forEach(keywordForType::add);
    }
  }

  private void handleLinkUrlProperties(String name, Object value) {
    List<String> linkList = linkByProtocols.computeIfAbsent(name, k -> new ArrayList<>());
    if (value instanceof List) {
      @SuppressWarnings("unchecked")
      List<String> list = (List<String>) value;
      linkList.addAll(list);
    } else {
      linkList.add(value.toString());
    }
  }

  private void handleListOfUuidProperties(
      Map<String, List<String>> field, String name, Object value) {
    List<String> listOfUuids = field.computeIfAbsent(name, k -> new ArrayList<>());
    if (value instanceof List) {
      @SuppressWarnings("unchecked")
      List<String> list = (List<String>) value;
      listOfUuids.addAll(list);
    } else {
      listOfUuids.add(value.toString());
    }
  }

  private void handleRecordLinkProperties(String name, Object value) {
    ArrayList<String> recordLinkForField =
        associatedResourceFields.computeIfAbsent(name, k -> new ArrayList<>());
    if (value instanceof List) {
      @SuppressWarnings("unchecked")
      List<String> list = (List<String>) value;
      recordLinkForField.addAll(list);
    } else if (value instanceof String) {
      recordLinkForField.add((String) value);
    }
  }

  private void handleConformProperties(String name, Object value) {
    conformsTo.put(name, value instanceof Boolean ? ((Boolean) value).toString() : (String) value);
  }

  private void handleCodelistProperties(String name, Object value) {
    List<Codelist> codelist = copyCodelist().computeIfAbsent(name, k -> new ArrayList<>());
    if (value instanceof List) {
      @SuppressWarnings("unchecked")
      List<HashMap<String, String>> list = (List<HashMap<String, String>>) value;
      codelist.addAll(list.stream().map(Codelist::new).toList());
    } else if (value instanceof Map) {
      @SuppressWarnings("unchecked")
      Map<String, String> map = (Map<String, String>) value;
      codelist.add(new Codelist(map));
    }
  }
}
