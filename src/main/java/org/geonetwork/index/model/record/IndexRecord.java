/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */


package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.util.*;
import java.util.stream.Stream;

import static org.geonetwork.index.model.record.IndexRecordFieldNames.HIERARCHY_SUFFIX;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.NUMBER_SUFFIX;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@XmlRootElement(name = "indexRecord")
@XmlAccessorType(XmlAccessType.FIELD)
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
    IndexRecordFieldNames.HAS_BOUNDING_POLYGON,
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
    IndexRecordFieldNames.UUID})
public class IndexRecord {

    /**
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
    List<HashMap<String, String>> resourceAltTitle;

    @JsonProperty(IndexRecordFieldNames.ORDERING_INSTRUCTIONS)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    List<HashMap<String, String>> orderingInstructions;

    @JsonProperty(IndexRecordFieldNames.RESOURCE_LINEAGE)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    List<HashMap<String, String>> resourceLineage;

    @JsonProperty(IndexRecordFieldNames.SOURCE_DESCRIPTION)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    List<HashMap<String, String>> sourceDescription;

    @JsonProperty(IndexRecordFieldNames.PROCESS_STEPS)
    List<ProcessStep> processSteps;

    @JsonProperty(IndexRecordFieldNames.RESOURCE_ABSTRACT)
    Map<String, String> resourceAbstract;

    @JsonProperty(IndexRecordFieldNames.RESOURCE_CREDIT)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    List<HashMap<String, String>> resourceCredit;

    @JsonProperty(IndexRecordFieldNames.SUPPLEMENTAL_INFORMATION)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    List<HashMap<String, String>> supplementalInformation;

    @JsonProperty(IndexRecordFieldNames.PURPOSE)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    List<HashMap<String, String>> purpose;

    @JsonProperty(IndexRecordFieldNames.TAG)
    ArrayList<HashMap<String, String>> tag;

    @JsonProperty(IndexRecordFieldNames.TAG_NUMBER)
    int tagNumber;
    /**
     * <pre>
     *         "MD_LegalConstraintsOtherConstraintsObject": [{
     *             "default": "No limitations to public access",
     *             "langfre": "No limitations to public access",
     *             "link": "http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/noLimitations"
     * </pre>
     */
    @JsonIgnore
    @Singular("constraints")
    final Map<String, List<HashMap<String, String>>> constraints = new HashMap<>();

    @JsonIgnore
    @Singular("useLimitations")
    final Map<String, List<HashMap<String, String>>> useLimitations = new HashMap<>();

    @JsonIgnore
    final Map<String, String> conformsTo = new HashMap<>();

    @JsonProperty(IndexRecordFieldNames.LICENSE)
    @Singular
    final List<HashMap<String, String>> licenses = new ArrayList<>();

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

    @JsonProperty(IndexRecordFieldNames.IS_TEMPLATE)
    private Character isTemplate;

    private String root;

    private String indexingDate;

    private List<indexingErrorMsg> indexingErrorMsg;

    private String dateStamp;

    @JsonProperty(IndexRecordFieldNames.CHANGE_DATE)
    private String changeDate;

    @JsonProperty(IndexRecordFieldNames.CREATE_DATE)
    private String createDate;

    @JsonProperty(IndexRecordFieldNames.RESOURCE_DATE)
    private List<ResourceDate> resourceDate;

    @JsonIgnore
    private final Map<String, ArrayList<String>> resourceDateDetails = new HashMap<>();

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

    @JsonProperty(IndexRecordFieldNames.GROUP_PUBLISHED)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> groupPublished;

    @JsonProperty(IndexRecordFieldNames.GROUP_PUBLISHED_ID)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> groupPublishedId;

    @JsonProperty(IndexRecordFieldNames.POPULARITY)
    private int popularity;

    @JsonProperty(IndexRecordFieldNames.MAIN_LANGUAGE)
    private String mainLanguage;

    @JsonProperty(IndexRecordFieldNames.RESOURCE_TYPE)
    private List<String> resourceType;

    @JsonProperty(IndexRecordFieldNames.RESOURCE_TYPE_NAME)
    private Map<String, String> resourceTypeName;

    @JsonProperty(IndexRecordFieldNames.VALID)
    private Integer valid;

    @JsonProperty(IndexRecordFieldNames.FEEDBACK_COUNT)
    private Integer feedbackCount;

    @JsonProperty(IndexRecordFieldNames.USER_SAVED_COUNT)
    private Integer userSavedCount;

    @JsonProperty(IndexRecordFieldNames.RATING)
    private Integer rating;

    @JsonProperty(IndexRecordFieldNames.IS_HARVESTED)
    private boolean harvested;

    @JsonProperty(IndexRecordFieldNames.HARVESTER_UUID)
    private String harvesterUuid;

    @JsonProperty(IndexRecordFieldNames.HAS_XLINKS)
    private Boolean hasxlinks;

    @JsonProperty(IndexRecordFieldNames.HAS_OVERVIEW)
    private Boolean hasOverview;

    @JsonProperty(IndexRecordFieldNames.IS_OPEN_DATA)
    private Boolean isOpenData;

    @JsonProperty(IndexRecordFieldNames.INSPIRE_CONFORM_RESOURCE)
    @JsonDeserialize(using = NumericOrStringBooleanSerializer.class)
    private Boolean inspireConformResource;

    private List<String> otherLanguage;

    private List<String> otherLanguageId;

    @JsonProperty(IndexRecordFieldNames.RESOURCE_LANGUAGE)
    private List<String> resourceLanguage;

    @JsonProperty(IndexRecordFieldNames.RESOURCE_IDENTIFIER)
    private ArrayList<ResourceIdentifier> resourceIdentifier;

    private String resourceEdition;

    /**
     * Unused
     */
    private String displayOrder;

    @JsonProperty(IndexRecordFieldNames.ORG)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Map<String, String>> org;

    @JsonProperty(IndexRecordFieldNames.ORG_FOR_RESOURCE)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Map<String, String>> orgForResource;

    @JsonIgnore
    private final Map<String, List<Map<String, String>>> orgForResourceByRole = new HashMap<>();

    // others eg. pointOfContactOrgForResource are in other properties
    // ForResource
    // ForProcessing
    // ForDistribution
    @JsonIgnore
    private final Map<String, List<Contact>> contactByRole = new HashMap<>();

    @JsonProperty(IndexRecordFieldNames.FORMAT)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> formats;

    @JsonProperty(IndexRecordFieldNames.COORDINATE_SYSTEM)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> coordinateSystem;

    @JsonProperty(IndexRecordFieldNames.CRS_DETAILS)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<CrsDetails> coordinateSystemDetails;

    @JsonProperty(IndexRecordFieldNames.OVERVIEW)
    private List<Overview> overview;

    @JsonProperty(IndexRecordFieldNames.RECORD_LINK)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<RecordLink> associatedRecords;

    @JsonProperty(IndexRecordFieldNames.RECORD_OPERATE_ON)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> recordOperateOn;

    @JsonProperty(IndexRecordFieldNames.AGG_ASSOCIATED)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> associatedUuids;

    @JsonIgnore
    @Singular("associatedUuidsByType")
    private final Map<String, ArrayList<String>> associatedUuidsByType = new HashMap<>();

    @JsonProperty(IndexRecordFieldNames.CHILD_UUID)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> childUuid;

    @JsonProperty(IndexRecordFieldNames.PARENT_UUID)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> parentUuid;

    /**
     * Same as parent
     */
    @JsonProperty(IndexRecordFieldNames.RECORD_GROUP)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> recordGroup;

    @JsonProperty(IndexRecordFieldNames.MEASURE)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Measure> measures;

    // "measure_Exactitudeglobale": "83.80 %",
    @JsonIgnore
    @Singular
    private final Map<String, ArrayList<String>> measureFields = new HashMap<>();

    //  "recordLink_sources": [
    //          "Orthophotos 2020",
    //          "Relief de la Wallonie - Mod\\u00E8le Num\\u00E9rique de Terrain (MNT) 2013-2014"
    //  ],
    //  "recordLink_sources_uuid": [
    //          "7369222c-5241-452a-af07-4929506212f9",
    //          "6029e738-f828-438b-b10a-85e67f77af92"
    //  ],
    //  "recordLink_sources_url": [
    @JsonIgnore
    @Singular
    private final Map<String, ArrayList<String>> associatedResourceFields = new HashMap<>();

    @JsonProperty(IndexRecordFieldNames.LINK)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Link> links;

    @JsonProperty(IndexRecordFieldNames.LINK_URL)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> linkUrls;

    @JsonProperty(IndexRecordFieldNames.LINK_PROTOCOL)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> linkProtocols;

    @JsonIgnore
    @Singular
    private final Map<String, List<String>> linkByProtocols = new HashMap<>();

    @JsonProperty(IndexRecordFieldNames.RESOLUTION_SCALE_DENOMINATOR)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Float> resolutionScaleDenominator;

    @JsonProperty(IndexRecordFieldNames.RESOLUTION_DISTANCE)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> resolutionDistance;

    /**
     * <pre>
     *   resourceTemporalDateRange: [{
     *         gte: "2017-02-03T14:00:00",
     *         lte: "2017-02-03T14:00:00"
     * </pre>
     */
    private List<DateRange> resourceTemporalDateRange;
    private List<DateRange> resourceTemporalExtentDateRange;

    @JsonProperty(IndexRecordFieldNames.RESOURCE_TEMPORAL_EXTENT_DATE_RANGE_DETAILS)
    private List<DateRangeDetails> resourceTemporalExtentDetails;
    private List<VerticalRange> resourceVerticalRange;

    @JsonProperty(IndexRecordFieldNames.LOCATION)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> locations;

    @JsonProperty(IndexRecordFieldNames.HAS_BOUNDING_POLYGON)
    private boolean hasBoundingPolygon;

    @JsonProperty(IndexRecordFieldNames.EXTENT_IDENTIFIER)
    private List<Map<String, String>> extentIdentifier;

    @JsonProperty(IndexRecordFieldNames.EXTENT_DESCRIPTION)
    private List<Map<String, String>> extentDescription;

    @JsonProperty(IndexRecordFieldNames.SHAPE_PARSING_ERROR)
    private List<String> shapeParsingError;

    @JsonProperty(IndexRecordFieldNames.SHAPE)
    @JsonDeserialize(using = NodeTreeAsStringDeserializer.class)
    private List<String> shapes;

    @JsonProperty(IndexRecordFieldNames.GEOM)
    @JsonDeserialize(using = NodeTreeAsStringDeserializer.class)
    private List<String> geometries;

    @JsonProperty(IndexRecordFieldNames.SERVICE_TYPE)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> serviceType;

    @JsonProperty(IndexRecordFieldNames.SERVICE_TYPE_VERSION)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> serviceTypeVersion;

    @JsonIgnore
    @Singular("codelist")
    private Map<String, ArrayList<Codelist>> codelists = new HashMap<>();

    /**
     * <pre>
     *         "keywordType-theme": [{
     *             "default": "Régional",
     *             "langfre": "Régional",
     *             "link": "http://inspire.ec.europa.eu/metadata-codelist/SpatialScope/regional"
     *         },
     *      </pre>
     */
    @JsonIgnore
    @Singular("keywordByType")
    private Map<String, ArrayList<Keyword>> keywordByType = new HashMap<>();

    /**
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
    private final Map<String, ArrayList<Keyword>> keywordByThesaurus = new HashMap<>();

    @JsonIgnore
    @Singular("numberOfKeywordByThesaurus")
    private final Map<String, Integer> numberOfKeywordByThesaurus = new HashMap<>();

    /**
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

    @JsonProperty(IndexRecordFieldNames.SPECIFICATION_CONFORMANCE)
    @Singular("specificationConformance")
    private List<SpecificationConformance> specificationConformance;

    @JsonProperty(IndexRecordFieldNames.FEATURE_TYPES)
    private List<FeatureType> featureTypes;

    @JsonProperty(IndexRecordFieldNames.HASFEATURECAT)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> hasfeaturecat;

    @JsonProperty(IndexRecordFieldNames.HASSOURCE)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> hassource;

    /**
     * Record to be loaded in the index.
     * //  public IndexRecord(AbstractMetadata r) {
     * //    boolean isDraft = r instanceof MetadataDraft;
     * //
     * //    this.setId(r.getUuid() + (isDraft ? "-draft" : ""));
     * //
     * //    // Based on https://github.com/geonetwork/core-geonetwork/blob/4.0.x/core/src/main/java/org/fao/geonet/kernel/datamanager/base/BaseMetadataIndexer.java#L325
     * //    // XML document as string to be further processed in XSLT
     * //    this.setDocument(r.getData());
     * //
     * //    this.setInternalId(r.getId());
     * //    this.setMetadataIdentifier(r.getUuid());
     * //    this.setSchema(r.getDataInfo().getSchemaId());
     * //    this.setDocType(IndexDocumentType.metadata);
     * //    this.setDraft(isDraft ? "y" : "n");
     * //    this.setIsTemplate(r.getDataInfo().getType().code);
     * //
     * //    this.setCreateDate(r.getDataInfo().getCreateDate().getDateAndTime());
     * //    this.setChangeDate(r.getDataInfo().getChangeDate().getDateAndTime());
     * //
     * //    this.setOwner(r.getSourceInfo().getOwner());
     * //    this.setGroupOwner(r.getSourceInfo().getGroupOwner());
     * //    // this.setRecordOwner("admin admin");
     * //    //userinfo: "admin|admin|admin|Administrator",
     * //    this.setSourceCatalogue(r.getSourceInfo().getSourceId());
     * //    this.setHarvested(r.getHarvestInfo().isHarvested());
     * //    this.setHarvesterUuid(r.getHarvestInfo().getUuid());
     * //    this.setLogo("");
     * //    this.setPublishedToAll(true);
     * //
     * //    this.setPopularity(r.getDataInfo().getPopularity());
     * //    this.setRating(r.getDataInfo().getRating());
     * //  }
     */

    @JsonIgnore
    @Singular("operation")
    private final Map<String, ArrayList<Integer>> operations = new HashMap<>();

    @Singular
    private final Map<String, ArrayList<String>> otherProperties = new HashMap<>();

    private static boolean isDateField(String name) {
        return name.endsWith("Date" + IndexRecordFieldNames.FOR_RESOURCE_SUFFIX)
            || name.endsWith("Year" + IndexRecordFieldNames.FOR_RESOURCE_SUFFIX)
            || name.endsWith("Month" + IndexRecordFieldNames.FOR_RESOURCE_SUFFIX);
    }

    private static boolean isKeywordHierarchyField(String name) {
        return name.startsWith(IndexRecordFieldNames.KEYWORD_BY_THESAURUS_PREFIX) && name.endsWith(HIERARCHY_SUFFIX);
    }

    private static boolean isKeywordNumberField(String name) {
        return name.startsWith(IndexRecordFieldNames.KEYWORD_BY_THESAURUS_PREFIX) && name.endsWith(NUMBER_SUFFIX);
    }

    /**
     * Unwrapped all other properties and all fields stored in Map on serialization.
     */
    @JsonAnyGetter
    public Map<String, Object> getOtherProperties() {
        Map<String, Object> allFieldInAMap = new HashMap<>();
        Stream.of(otherProperties,
                codelists,
                keywordByType,
                keywordByThesaurus,
                keywordHierarchyByThesaurus,
                numberOfKeywordByThesaurus,
                constraints,
                useLimitations,
                associatedUuidsByType,
                orgForResourceByRole,
                contactByRole,
                measureFields,
                associatedResourceFields,
                resourceDateDetails,
                linkByProtocols,
                operations)
            .filter(Objects::nonNull)
            .forEach(allFieldInAMap::putAll);
        return allFieldInAMap;
    }

    /**
     * Collect all other properties in proper Map fields.
     * The getOtherProperties is used to serialize all those properties in the JSON.
     */
    @JsonAnySetter
    public void handleUnrecognizedField(String name, Object value) {
        try {
            IndexRecord.class.getDeclaredField(name);
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
                } else if (name.contains(IndexRecordFieldNames.CONTACT_FOR_PREFIX) || name.equals(IndexRecordFieldNames.CONTACT)) {
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
                    handleKeywordHierarchyProperties(name, (Map<String, ArrayList<String>>) value);
                } else if (name.startsWith(IndexRecordFieldNames.KEYWORD_BY_THESAURUS_PREFIX)) {
                    handleKeywordByTypeProperties(keywordByThesaurus, name, value);
                } else if (name.endsWith(IndexRecordFieldNames.CONSTRAINT_SUFFIX)) {
                    handleConstraintProperties(constraints, name, value);
                } else if (name.endsWith(IndexRecordFieldNames.USE_LIMITATION_SUFFIX)) {
                    handleConstraintProperties(useLimitations, name, value);
                } else if (isDateField(name)) {
                    handleDateProperties(name, value);
                } else {
                    handleOtherProperties(name, value);
                }
            } catch (Exception fieldException) {
                // TODO: Ignore ? and return incomplete document
                System.out.println(String.format(
                    "Error while handling field %s with value %s for record %s. %s",
                    name, value, this.getMetadataIdentifier(), e.getMessage()));
//                throw fieldException;
            }
        }
    }

    private void handleOperationsProperties(String name, Object value) {
        ArrayList<Integer> operationField = operations.computeIfAbsent(name, k -> new ArrayList<Integer>());
        if (value instanceof List) {
            operationField.addAll((Collection) value);
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
        List<String> resourceDateForField = resourceDateDetails.computeIfAbsent(name, k -> new ArrayList<String>());
        if (value instanceof List) {
            resourceDateForField.addAll((Collection) value);
        }
        if (value instanceof String) {
            resourceDateForField.add(value.toString());
        }
    }

    private void handleConstraintProperties(Map<String, List<HashMap<String, String>>> constraints, String name, Object value) {
        List<HashMap<String, String>> constraintByType = constraints.computeIfAbsent(name, k -> new ArrayList<>());
        if (value instanceof List) {
            constraintByType.addAll((List<HashMap<String, String>>) value);
        }
    }

    private void handleOrgForResourceProperties(Map<String, List<Map<String, String>>> orgForResourceByRole, String name, Object value) {
        List<Map<String, String>> resourceByRole = orgForResourceByRole.computeIfAbsent(name, k -> new ArrayList<>());
        if (value instanceof Map) {
            resourceByRole.add((Map<String, String>) value);
        }
    }

    private void handleContactByRoleProperties(Map<String, List<Contact>> contactByRoleList, String name, Object value) {
        List<Contact> resourceByRole = contactByRoleList.computeIfAbsent(name, k -> new ArrayList<>());
        if (value instanceof List) {
            resourceByRole.addAll((List<Contact>) value);
        } else {
            resourceByRole.add((Contact) value);
        }
    }

    private void handleKeywordHierarchyProperties(String name, Map<String, ArrayList<String>> value) {
        keywordHierarchyByThesaurus.put(name, new KeywordHierarchy(value.get("default"), value.get("key")));
    }

    private void handleKeywordNumberProperties(String name, Object value) {
        if (value instanceof List) {
            numberOfKeywordByThesaurus.put(name, ((List<Integer>) value).size());   // This should not happen or indicate that a thesaurus is used 2 times in the same record.
        } else {
            numberOfKeywordByThesaurus.put(name, Integer.parseInt(value.toString()));
        }
    }

    private void handleKeywordByTypeProperties(Map<String, ArrayList<Keyword>> keywordField, String name, Object value) {
        ArrayList<Keyword> keywordForType = keywordField.computeIfAbsent(name, k -> new ArrayList<Keyword>());
        if (value instanceof List) {
            ((List<HashMap<String, String>>) value).stream().map(Keyword::new).forEach(keywordForType::add);
        }
    }

    private void handleLinkUrlProperties(String name, Object value) {
        List<String> linkList = linkByProtocols.computeIfAbsent(name, k -> new ArrayList<String>());
        if (value instanceof List) {
            linkList.addAll((List<String>) value);
        } else {
            linkList.add(value.toString());
        }
    }

    private void handleListOfUuidProperties(Map<String, ArrayList<String>> field, String name, Object value) {
        ArrayList<String> listOfUuids = field.computeIfAbsent(name, k -> new ArrayList<String>());
        if (value instanceof List) {
            listOfUuids.addAll((List<String>) value);
        } else {
            listOfUuids.add(value.toString());
        }
    }

    private void handleRecordLinkProperties(String name, Object value) {
        ArrayList<String> recordLinkForField = associatedResourceFields.computeIfAbsent(name, k -> new ArrayList<String>());
        if (value instanceof List) {
            recordLinkForField.addAll((List<String>) value);
        }
    }

    private void handleConformProperties(String name, Object value) {
        conformsTo.put(name, value instanceof Boolean ? ((Boolean) value).toString() : (String) value);
    }

    private void handleCodelistProperties(String name, Object value) {
        ArrayList<Codelist> codelist = codelists.computeIfAbsent(name, k -> new ArrayList<Codelist>());
        if (value instanceof List) {
            codelist.addAll(((List<HashMap<String, String>>) value).stream().map(Codelist::new).toList());
        }
    }
}
