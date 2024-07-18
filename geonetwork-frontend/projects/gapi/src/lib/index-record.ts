export module IndexObject {
  export interface RecordIndex {
    docType: string;
    document: string;
    metadataIdentifier: string;
    standardNameObject: StandardNameObject;
    standardVersionObject: StandardVersionObject;
    indexingDate: number;
    dateStamp: string;
    mainLanguage: string;
    otherLanguage: string[];
    otherLanguageId: string[];
    cl_characterSet: Codelist[];
    resourceType: string[];
    resourceTypeNameObject: ResourceTypeNameObject;
    cl_resourceScope: Codelist[];
    cl_function: Codelist[];
    cl_geometricObjectType: Codelist[];
    cl_presentationForm: Codelist[];
    cl_status: Codelist[];
    cl_numberType: Codelist[];
    cl_spatialRepresentationType: Codelist[];
    cl_maintenanceAndUpdateFrequency: Codelist[];
    cl_type: Codelist[];
    cl_accessConstraints: Codelist[];
    cl_useConstraints: Codelist[];
    cl_associationType: Codelist[];
    cl_initiativeType: Codelist[];
    resourceTitleObject: ResourceTitleObject;
    resourceAltTitleObject: ResourceAltTitleObject[];
    publicationDateForResource: string[];
    publicationYearForResource: string;
    publicationMonthForResource: string;
    revisionDateForResource: string[];
    revisionYearForResource: string[];
    revisionMonthForResource: string[];
    creationDateForResource: string[];
    creationYearForResource: string;
    creationMonthForResource: string;
    resourceDate: ResourceDate[];
    resourceTemporalDateRange: ResourceTemporalDateRange[];
    resourceIdentifier: ResourceIdentifier[];
    presentationForm: string;
    resourceEdition: string;
    resourceAbstractObject: ResourceAbstractObject;
    cl_resourceCharacterSet: Codelist[];
    OrgForResourceObject: OrganisationName[];
    pointOfContactOrgForResourceObject: OrganisationName[];
    contactForResource: ContactForResource[];
    sponsorOrgForResourceObject: SponsorOrgForResourceObject;
    publisherOrgForResourceObject: PublisherOrgForResourceObject;
    authorOrgForResourceObject: AuthorOrgForResourceObject;
    coAuthorOrgForResourceObject: CoAuthorOrgForResourceObject;
    resourceCreditObject: ResourceCreditObject[];
    supplementalInformationObject: SupplementalInformationObject;
    hasOverview: string;
    resourceLanguage: string[];
    inspireTheme_syn: string[];
    inspireTheme: string[];
    inspireThemeFirst_syn: string;
    inspireThemeFirst: string;
    inspireAnnexForFirstTheme: string;
    inspireThemeUri: string[];
    inspireAnnex: string[];
    inspireThemeNumber: string;
    hasInspireTheme: string;
    tag: Tag[];
    tagNumber: string;
    isOpenData: string;
    'keywordType-place': KeywordType[];
    'keywordType-theme': KeywordType[];
    'th_otherKeywords-placeNumber': string;
    // "th_otherKeywords-place": ThOtherKeywordsPlace[]
    // allKeywords: AllKeywords
    // th_gemet_tree: ThGemetTree
    spatialRepresentationType: string;
    MD_LegalConstraintsOtherConstraintsObject: MdLegalConstraintsOtherConstraintsObject[];
    MD_LegalConstraintsUseLimitationObject: MdLegalConstraintsUseLimitationObject[];
    licenseObject: LicenseObject[];
    geom: Geom[];
    location: string[];
    indexingErrorMsg: IndexingErrorMsg[];
    specificationConformance: SpecificationConformance[];
    conformTo_INSPIRE: string;
    featureTypes: FeatureType[];
    lineageObject: LineageObject;
    sourceDescriptionObject: SourceDescriptionObject;
    processSteps: ProcessStep[];
    OrgForProcessingObject: OrgForProcessingObject[];
    pointOfContactOrgForProcessingObject: PointOfContactOrgForProcessingObject;
    contactForProcessing: ContactForProcessing[];
    processorOrgForProcessingObject: ProcessorOrgForProcessingObject;
    dqCpt: string;
    OrgForDistributionObject: OrgForDistributionObject;
    distributorOrgForDistributionObject: DistributorOrgForDistributionObject;
    contactForDistribution: ContactForDistribution[];
    linkUrl: string[];
    linkProtocol: string[];
    link: Link[];
    parentUuid: string;
    recordGroup: string;
    recordLink: RecordLink[];
    recordLink_parent: string;
    recordLink_parent_uuid: string;
    recordLink_parent_url: string;
    recordLink_siblingsassociationTypecrossReference_initiativeTypeplatform: string[];
    agg_associated: string[];
    agg_associated_crossReference: string[];
    recordLink_siblingsassociationTypecrossReference_initiativeTypestudy: string[];
    agg_associated_largerWorkCitation: string[];
    recordLink_siblingsassociationTypecrossReference_initiativeTypeprocess: string;
    recordLink_siblingsassociationTypecrossReference_initiativeTypeprocess_uuid: string;
    recordLink_siblingsassociationTypecrossReference_initiativeTypeprocess_url: string;
    recordOwner: string;
    valid_inspire: string;
    uuid: string;
    displayOrder: string;
    groupPublishedId: string[];
    popularity: number;
    userinfo: string;
    groupPublished: string[];
    isPublishedToAll: string;
    record: string;
    draft: string;
    changeDate: string;
    'valid_schematron-rules-datacite': string;
    id: string;
    valid_xsd: string;
    createDate: string;
    isPublishedToIntranet: string;
    owner: string;
    'valid_schematron-rules-iso': string;
    groupOwner: string;
    hasxlinks: string;
    op0: string[];
    featureOfRecord: string;
    op2: string;
    op1: string[];
    isPublishedToGuest: string;
    extra: string;
    documentStandard: string;
    op5: string[];
    valid: string;
    isTemplate: string;
    feedbackCount: string;
    rating: string;
    isHarvested: string;
    userSavedCount: string;
    sourceCatalogue: string;
    indexingError: string;
    overview: Overview[];

    [propName: string]: any;
  }

  export interface StandardNameObject {
    default: string;

    [key: string]: string;
  }

  export interface StandardVersionObject {
    default: string;

    [key: string]: string;
  }

  export interface ResourceTypeNameObject {
    default: string;

    [key: string]: string;
  }

  export interface Codelist {
    key: string;
    default: string;
    link: string;

    [key: string]: string;
  }

  export interface ResourceTitleObject {
    default: string;

    [key: string]: string;
  }

  export interface ResourceAltTitleObject {
    default: string;

    [key: string]: string;
  }

  export interface ResourceDate {
    type: string;
    date: string;
  }

  export interface ResourceTemporalDateRange {
    gte: string;
    lte: string;
  }

  export interface ResourceIdentifier {
    code: string;
    codeSpace: string;
    link: string;
  }

  export interface ResourceAbstractObject {
    default: string;

    [key: string]: string;
  }

  export interface OrganisationName {
    default: string;

    [key: string]: string;
  }

  export interface ContactForResource {
    organisationObject: OrganisationObject;
    role: string;
    email: string;
    website: string;
    logo: string;
    individual: string;
    position: string;
    phone: string;
    address: string;
    identifiers?: Identifier[];
  }

  export interface OrganisationObject {
    default: string;

    [key: string]: string;
  }

  export interface Identifier {
    code: string;
    codeSpace: string;
    link: string;
  }

  export interface SponsorOrgForResourceObject {
    default: string;

    [key: string]: string;
  }

  export interface PublisherOrgForResourceObject {
    default: string;

    [key: string]: string;
  }

  export interface AuthorOrgForResourceObject {
    default: string;

    [key: string]: string;
  }

  export interface CoAuthorOrgForResourceObject {
    default: string;

    [key: string]: string;
  }

  export interface ResourceCreditObject {
    default: string;

    [key: string]: string;
  }

  export interface SupplementalInformationObject {
    default: string;

    [key: string]: string;
  }

  export interface Tag {
    default: string;

    [key: string]: string;
  }

  export interface KeywordType {
    default: string;

    [key: string]: string;
  }

  export interface AllKeywords {
    [key: string]: any;
  }

  export interface Keyword {
    default: string;
    [key: string]: string;
  }

  export interface MdLegalConstraintsOtherConstraintsObject {
    default: string;

    [key: string]: string;
  }

  export interface MdLegalConstraintsUseLimitationObject {
    default: string;
    link: string;

    [key: string]: string;
  }

  export interface LicenseObject {
    default: string;

    [key: string]: string;
  }

  export interface Geom {
    type: string;
    coordinates: number[][][];
  }

  export interface IndexingErrorMsg {
    string: string;
    type: string;
    values: Values;
  }

  export interface Values {}

  export interface SpecificationConformance {
    title: string;
    date: string;
    explanation: string;
    pass: string;
  }

  export interface FeatureType {
    typeName: string;
    definition: string;
    code: string;
    isAbstract: string;
    aliases: string;
    attributeTable: AttributeTable[];
  }

  export interface AttributeTable {
    name: string;
    definition: string;
    code: string;
    link: string;
    type: string;
    cardinality?: string;
  }

  export interface LineageObject {
    default: string;

    [key: string]: string;
  }

  export interface SourceDescriptionObject {
    default: string;

    [key: string]: string;
  }

  export interface ProcessStep {
    descriptionObject: DescriptionObject;
    processor?: Processor[];
  }

  export interface DescriptionObject {
    default: string;

    [key: string]: string;
  }

  export interface Processor {
    organisationObject: OrganisationObject;
    individual?: string;
  }

  export interface OrgForProcessingObject {
    default: string;

    [key: string]: string;
  }

  export interface PointOfContactOrgForProcessingObject {
    default: string;

    [key: string]: string;
  }

  export interface ContactForProcessing {
    organisationObject: OrganisationObject3;
    role: string;
    email: string;
    website: string;
    logo: string;
    individual: string;
    position: string;
    phone: string;
    address: string;
  }

  export interface OrganisationObject3 {
    default: string;

    [key: string]: string;
  }

  export interface ProcessorOrgForProcessingObject {
    default: string;

    [key: string]: string;
  }

  export interface OrgForDistributionObject {
    default: string;

    [key: string]: string;
  }

  export interface DistributorOrgForDistributionObject {
    default: string;

    [key: string]: string;
  }

  export interface ContactForDistribution {
    organisationObject: OrganisationObject4;
    role: string;
    email: string;
    website: string;
    logo: string;
    individual: string;
    position: string;
    phone: string;
    address: string;
  }

  export interface OrganisationObject4 {
    default: string;

    [key: string]: string;
  }

  export interface Link {
    protocol: string;
    mimeType: string;
    urlObject: UrlObject;
    descriptionObject?: DescriptionObject;
    function: string;
    applicationProfile: string;
    group: number;
    nameObject?: NameObject;
  }

  export interface UrlObject {
    default: string;

    [key: string]: string;
  }

  export interface NameObject {
    default: string;

    [key: string]: string;
  }

  export interface RecordLink {
    type: string;
    to: string;
    url: string;
    title: string;
    origin: string;
    associationType?: string;
    initiativeType?: string;
  }

  export interface Overview {
    data: string;
    url: string;
  }
}
