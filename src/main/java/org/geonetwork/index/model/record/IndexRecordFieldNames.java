/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

/** Index record field names. */
public class IndexRecordFieldNames {

  public static final String SOURCE = "_source";
  public static final String DOC_TYPE = "docType";
  public static final String DOCUMENT = "document";
  public static final String SCHEMA = "schema";
  public static final String ID = "id";
  public static final String METADATA_IDENTIFIER = "metadataIdentifier";
  public static final String UUID = "uuid";
  public static final String OWNER = "owner";
  public static final String GROUP_OWNER = "groupOwner";
  public static final String OP_PREFIX = "op";
  public static final String CATEGORY = "cat";

  /** The standard name of the record eg. ISO 19115. */
  public static final String STANDARD_NAME = "standardNameObject";

  /** The standard version of the record eg. 19115-1. */
  public static final String STANDARD_VERSION = "standardVersionObject";

  public static final String IS_HARVESTED = "isHarvested";
  public static final String SOURCE_CATALOGUE = "sourceCatalogue";
  public static final String HARVESTER_UUID = "harvesterUuid";
  public static final String HAS_OVERVIEW = "hasOverview";
  public static final String OVERVIEW = "overview";
  public static final String IS_OPEN_DATA = "isOpenData";
  public static final String INSPIRE_CONFORM_RESOURCE = "inspireConformResource";
  public static final String INSPIRE_REPORT_URL = "inspireReportUrl";
  public static final String INSPIRE_VALIDATION_DATE = "inspireValidationDate";
  public static final String INSPIRE_VALID = "valid_inspire";
  public static final String GROUP_PUBLISHED_ID = "groupPublishedId";
  public static final String GROUP_PUBLISHED = "groupPublished";
  public static final String POPULARITY = "popularity";
  public static final String RATING = "rating";
  public static final String FEEDBACK_COUNT = "feedbackCount";
  public static final String USER_SAVED_COUNT = "userSavedCount";
  public static final String HAS_XLINKS = "hasxlinks";
  public static final String VALID = "valid";
  public static final String USER_INFO = "userinfo";
  public static final String DRAFT = "draft";
  public static final String IS_TEMPLATE = "isTemplate";
  public static final String IS_PUBLISHED_TO_ALL = "isPublishedToAll";
  public static final String IS_PUBLISHED_TO_INTRANET = "isPublishedToIntranet";
  public static final String IS_PUBLISHED_TO_GUEST = "isPublishedToGuest";

  /** Record title. */
  public static final String RESOURCE_TITLE = "resourceTitleObject";

  /** Alternate title. */
  public static final String RESOURCE_ALT_TITLE = "resourceAltTitleObject";

  /**
   * Resource abstract.
   *
   * <p>Keep the abstract short and use purpose and supplemental information for more detailed
   * information.
   */
  public static final String RESOURCE_ABSTRACT = "resourceAbstractObject";

  /**
   * Resource credit. Sometimes used to credit the source of the data.
   *
   * <p>Alternative is to use contact with proper role (which will be used to compute the citation).
   */
  public static final String RESOURCE_CREDIT = "resourceCreditObject";

  public static final String ORDERING_INSTRUCTIONS = "orderingInstructionsObject";

  /** Supplemental information. */
  public static final String SUPPLEMENTAL_INFORMATION = "supplementalInformationObject";

  /** Purpose. */
  public static final String PURPOSE = "purposeObject";

  public static final String RESOURCE_IDENTIFIER = "resourceIdentifier";
  public static final String RESOURCE_LANGUAGE = "resourceLanguage";
  public static final String MAIN_LANGUAGE = "mainLanguage";
  public static final String RESOURCE_DATE = "resourceDate";
  public static final String INDEXING_DATE = "indexingDate";
  public static final String CHANGE_DATE = "changeDate";
  public static final String CREATE_DATE = "createDate";
  public static final String FOR_RESOURCE_SUFFIX = "ForResource";
  public static final String ORG = "OrgObject";
  public static final String ORG_SUFFIX = "OrgObject";
  public static final String ORG_FOR_RESOURCE = "OrgForResourceObject";
  public static final String ORG_FOR_RESOURCE_SUFFIX = "OrgForResourceObject";
  public static final String CONTACT = "contact";
  public static final String CONTACT_FOR_PREFIX = "contactFor";
  public static final String ORG_FOR_PREFIX = "OrgFor";
  public static final String ORGANISATION_NAME = "organisationObject";
  public static final String CONTACT_IDENTIFIERS = "identifiers";
  public static final String MEASURE = "measure";
  public static final String MEASURE_PREFIX = "measure_";
  public static final String RECORD_GROUP = "recordGroup";
  public static final String RECORD_OWNER = "recordOwner";
  public static final String CHILD_UUID = "childUuid";
  public static final String PARENT_UUID = "parentUuid";
  public static final String AGG_ASSOCIATED = "agg_associated";
  public static final String AGG_ASSOCIATED_PREFIX = "agg_associated_";
  public static final String RECORD_OPERATE_ON = "recordOperateOn";
  public static final String RECORD_LINK = "recordLink";
  public static final String RECORD_LINK_PREFIX = "recordLink_";
  public static final String LINK = "link";
  public static final String LINK_URL_PROTOCOL_PREFIX = "linkUrlProtocol";
  public static final String LINK_PROTOCOL = "linkProtocol";
  public static final String LINK_URL = "linkUrl";
  public static final String LOCATION = "location";
  public static final String HAS_BOUNDING_POLYGON = "hasBoundingPolygon";
  public static final String EXTENT_DESCRIPTION = "extentDescriptionObject";
  public static final String EXTENT_IDENTIFIER = "extentIdentifierObject";
  public static final String SHAPE_PARSING_ERROR = "shapeParsingError";
  public static final String SHAPE = "shape";
  public static final String COORDINATE_SYSTEM = "coordinateSystem";
  public static final String CRS_DETAILS = "crsDetails";
  public static final String GEOM = "geom";
  public static final String ALL_KEYWORDS = "allKeywords";
  public static final String TAG = "tag";
  public static final String TAG_NUMBER = "tagNumber";
  public static final String SERVICE_TYPE = "serviceType";
  public static final String SERVICE_TYPE_VERSION = "serviceTypeVersion";
  public static final String FORMAT = "format";
  public static final String RESOURCE_VERTICAL_RANGE = "resourceVerticalRange";
  public static final String RESOURCE_TEMPORAL_EXTENT_DATE_RANGE =
      "resourceTemporalExtentDateRange";
  public static final String RESOURCE_TEMPORAL_EXTENT_DATE_RANGE_DETAILS =
      "resourceTemporalExtentDetails";
  public static final String RESOURCE_TEMPORAL_DATE_RANGE = "resourceTemporalDateRange";
  public static final String REVISION_YEAR_FOR_RESOURCE = "revisionYearForResource";
  public static final String REVISION_MONTH_FOR_RESOURCE = "revisionMonthForResource";
  public static final String DATE_STAMP = "dateStamp";
  public static final String RESOURCE_LINEAGE = "lineageObject";
  public static final String SOURCE_DESCRIPTION = "sourceDescriptionObject";
  public static final String PROCESS_STEPS = "processSteps";
  public static final String SPECIFICATION_CONFORMANCE = "specificationConformance";
  public static final String FEATURE_TYPES = "featureTypes";
  public static final String RESOLUTION_SCALE_DENOMINATOR = "resolutionScaleDenominator";
  public static final String RESOLUTION_DISTANCE = "resolutionDistance";
  public static final String RESOURCE_TYPE = "resourceType";

  /** The resource type name. Complementary information to the resource type. */
  public static final String RESOURCE_TYPE_NAME = "resourceTypeNameObject";
  public static final String STATUS_CHANGE_DATE = "mdStatusChangeDate";
  public static final String LAST_WORKFLOW_STATUS = "mdStatus";

  public static final String HASSOURCE = "hassource";
  public static final String HASFEATURECAT = "hasfeaturecat";
  public static final String KEYWORD_BY_TYPE_PREFIX = "keywordType-";
  public static final String KEYWORD_BY_THESAURUS_PREFIX = "th_";
  public static final String NUMBER_SUFFIX = "Number";
  public static final String HIERARCHY_SUFFIX = "_tree";
  public static final String CONSTRAINT_SUFFIX = "OtherConstraintsObject";
  public static final String USE_LIMITATION_SUFFIX = "UseLimitationObject";
  public static final String LICENSE = "licenseObject";
  public static final String CONFORMS_TO_PREFIX = "conformTo_";
  public static final String INSPIRE = "inspire";

  /** Private constructor to prevent instantiation. */
  private IndexRecordFieldNames() {}

  /** Field names for the process step. */
  public static class ProcessStepField {
    public static final String DESCRIPTION = "descriptionObject";
    public static final String DATE = "date";
    public static final String SOURCE = "source";
    public static final String PROCESSOR = "processor";

    private ProcessStepField() {}
  }

  /** Field name in codelist field. */
  public static class Codelists {
    public static final String PREFIX = "cl_";
    public static final String CHARACTER_SET = PREFIX + "characterSet";
    public static final String RESOURCE_CHARACTER_SET = PREFIX + "resourceCharacterSet";
    public static final String HIERARCHY_LEVEL = PREFIX + "hierarchyLevel";
    public static final String STATUS = PREFIX + "status";
    public static final String TOPIC = PREFIX + "topic";
    public static final String MAINTENANCE_AND_UPDATE_FREQUENCY =
        PREFIX + "maintenanceAndUpdateFrequency";

    private Codelists() {}
  }

  /** Field names for multilingual object fields. */
  public static class CommonField {
    public static final String DEFAULT_TEXT = "default";
    public static final String KEY = "key";
    public static final String LINK = "link";

    private CommonField() {}
  }

  /** Field names for the link object. */
  public static class LinkField {
    public static final String URL = "urlObject";
    public static final String PROTOCOL = "protocol";
    public static final String NAME = "nameObject";
    public static final String DESCRIPTION = "descriptionObject";

    private LinkField() {}
  }
}
