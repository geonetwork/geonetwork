export interface AppConfig {
  langDetector: LangDetector;
  nodeDetector: NodeDetector;
  serviceDetector: ServiceDetector;
  baseURLDetector: BaseUrldetector;
  mods: Mods;
  sextant: Sextant;
}

export interface LangDetector {
  fromHtmlTag: boolean;
  regexp: string;
  default: string;
}

export interface NodeDetector {
  regexp: string;
  default: string;
}

export interface ServiceDetector {
  regexp: string;
  default: string;
}

export interface BaseUrldetector {
  regexp: string;
  default: string;
}

export interface Mods {
  global: Global;
  footer: Footer;
  header: Header;
  cookieWarning: CookieWarning;
  home: Home;
  search: Search;
  map: Map;
  geocoder: Geocoder;
  recordview: Recordview;
  editor: Editor;
  directory: Directory;
  admin: Admin;
  authentication: Authentication;
  page: Page;
  workflowHelper: WorkflowHelper;
}

export interface Global {
  hotkeys: boolean;
  humanizeDates: boolean;
  dateFormat: string;
  timezone: string;
}

export interface Footer {
  enabled: boolean;
  showSocialBarInFooter: boolean;
  showApplicationInfoAndLinksInFooter: boolean;
  footerCustomMenu: any[];
  rssFeeds: RssFeed[];
}

export interface RssFeed {
  url: string;
  label: string;
}

export interface Header {
  enabled: boolean;
  languages: Languages;
  isLogoInHeader: boolean;
  logoInHeaderPosition: string;
  fluidHeaderLayout: boolean;
  showGNName: boolean;
  isHeaderFixed: boolean;
  showPortalSwitcher: boolean;
  topCustomMenu: any[];
}

export interface Languages {
  eng: string;
  fre: string;
}

export interface CookieWarning {
  enabled: boolean;
  cookieWarningMoreInfoLink: string;
  cookieWarningRejectLink: string;
}

export interface Home {
  enabled: boolean;
  appUrl: string;
  showSearch: boolean;
  showSocialBarInFooter: boolean;
  showMosaic: boolean;
  showMaps: boolean;
  facetConfig: FacetConfig;
  info: Info[];
  fluidLayout: boolean;
}

export interface FacetConfig {}

export interface Info {
  type: string;
  title?: string;
  active?: boolean;
  params?: Params;
}

export interface Params {
  isTemplate: string;
  sortBy: string;
  sortOrder: string;
  from: number;
  to: number;
}

export interface Search {
  enabled: boolean;
  appUrl: string;
  hitsperpageValues: number[];
  paginationInfo: PaginationInfo;
  queryBase: string;
  queryBaseOptions: QueryBaseOptions;
  queryExactMatch: string;
  queryTitle: string;
  queryTitleExactMatch: string;
  searchOptions: SearchOptions;
  languageStrategy: string;
  languageWhitelist: any[];
  scoreConfig: ScoreConfig;
  autocompleteConfig: AutocompleteConfig;
  moreLikeThisSameType: boolean;
  moreLikeThisConfig: MoreLikeThisConfig;
  facetTabField: string;
  isVegaEnabled: boolean;
  facetConfig: FacetConfig;
  filters: any;
  sortbyValues: SortbyValue[];
  sortBy: string;
  resultViewTpls: ResultViewTpl[];
  resultTemplate: string;
  searchResultContact: string;
  formatter: Formatter;
  downloadFormatter: DownloadFormatter[];
  grid: Grid;
  linkTypes: LinkTypes;
  isFilterTagsDisplayedInSearch: boolean;
  searchMapPlacement: string;
  showStatusFooterFor: string;
  showBatchDropdown: boolean;
  usersearches: Usersearches;
  savedSelection: SavedSelection;
  addWMSLayersToMap: AddWmslayersToMap;
}

export interface PaginationInfo {
  hitsPerPage: number;
}

export interface QueryBaseOptions {
  default_operator: string;
}

export interface SearchOptions {
  fullText: boolean;
  titleOnly: boolean;
  exactMatch: boolean;
  language: boolean;
}

export interface ScoreConfig {}

export interface AutocompleteConfig {}

export interface MoreLikeThisConfig {}

export interface SortbyValue {
  sortBy: string;
  sortOrder: string;
}

export interface ResultViewTpl {
  tplUrl: string;
  tooltip: string;
  icon: string;
  related: string[];
  source?: ViewSource;
}

export interface ViewSource {
  exclude: string[];
}

export interface Formatter {
  list: List[];
}

export interface List {
  label: string;
  url: string;
}

export interface DownloadFormatter {
  label: string;
  url: string;
  class: string;
}

export interface Grid {
  related: string[];
}

export interface LinkTypes {
  links: string[];
  downloads: string[];
  layers: string[];
  maps: string[];
}

export interface Usersearches {
  enabled: boolean;
  includePortals: boolean;
  displayFeaturedSearchesPanel: boolean;
}

export interface SavedSelection {
  enabled: boolean;
}

export interface AddWmslayersToMap {
  urlLayerParam: string;
}

export interface Map {
  enabled: boolean;
  appUrl: string;
  externalViewer: ExternalViewer;
  is3DModeAllowed: boolean;
  singleTileWMS: boolean;
  isSaveMapInCatalogAllowed: boolean;
  isExportMapAsImageEnabled: boolean;
  isAccessible: boolean;
  storage: string;
  bingKey: string;
  listOfServices: ListOfServices;
  wpsSource: string[];
  projection: string;
  projectionList: ProjectionList[];
  switcherProjectionList: SwitcherProjectionList[];
  disabledTools: DisabledTools;
  defaultTool: string;
  defaultToolAfterMapLoad: string;
  graticuleOgcService: GraticuleOgcService;
  'map-viewer': MapViewer;
  'map-search': MapSearch;
  'map-editor': MapEditor;
  'map-thumbnail': MapThumbnail;
  autoFitOnLayer: boolean;
}

export interface ExternalViewer {
  enabled: boolean;
  enabledViewAction: boolean;
  baseUrl: string;
  urlTemplate: string;
  openNewWindow: boolean;
  valuesSeparator: string;
}

export interface ListOfServices {
  wms: any[];
  wmts: any[];
  wps: any[];
}

export interface ProjectionList {
  code: string;
  label: string;
}

export interface SwitcherProjectionList {
  code: string;
  label: string;
}

export interface DisabledTools {
  processes: boolean;
  addLayers: boolean;
  projectionSwitcher: boolean;
  layers: boolean;
  legend: boolean;
  filter: boolean;
  contexts: boolean;
  print: boolean;
  mInteraction: boolean;
  graticule: boolean;
  mousePosition: boolean;
  syncAllLayers: boolean;
  drawVector: boolean;
  scaleLine: boolean;
}

export interface GraticuleOgcService {}

export interface MapViewer {
  context: string;
  extent: number[];
  layers: any[];
}

export interface MapSearch {
  context: string;
  extent: number[];
  layers: any[];
  geodesicExtents: boolean;
}

export interface MapEditor {
  context: string;
  extent: number[];
  layers: any[];
}

export interface MapThumbnail {
  context: string;
  extent: number[];
  layers: any[];
}

export interface Geocoder {
  enabled: boolean;
  appUrl: string;
}

export interface Recordview {
  isSocialbarEnabled: boolean;
  showStatusWatermarkFor: string;
  showStatusTopBarFor: string;
  showCitation: ShowCitation;
  sortKeywordsAlphabetically: boolean;
  mainThesaurus: string[];
  locationThesaurus: string[];
  internalThesaurus: any[];
  collectionTableConfig: CollectionTableConfig;
  distributionConfig: DistributionConfig;
  relatedFacetConfig: FacetConfig;
}

export interface ShowCitation {
  enabled: boolean;
  if: If;
}

export interface If {
  resourceType: string[];
}

export interface CollectionTableConfig {
  labels: string;
  columns: string;
}

export interface DistributionConfig {
  layout: string;
  sections: DistributionSection[];
}

export interface DistributionSection {
  filter: string;
  title: string;
}

export interface Editor {
  enabled: boolean;
  appUrl: string;
  isUserRecordsOnly: boolean;
  minUserProfileToCreateTemplate: string;
  isFilterTagsDisplayed: boolean;
  fluidEditorLayout: boolean;
  createPageTpl: string;
  editorIndentType: string;
  allowRemoteRecordLink: boolean;
  workflowSearchRecordTypes: string[];
  facetConfig: FacetConfig;
}

export interface Directory {
  sortbyValues: SortbyValue[];
  sortBy: string;
  facetConfig: FacetConfig;
  queryBase: string;
}

export interface Admin {
  enabled: boolean;
  appUrl: string;
  facetConfig: FacetConfig;
}

export interface Authentication {
  enabled: boolean;
  signinUrl: string;
  signoutUrl: string;
}

export interface Page {
  enabled: boolean;
  appUrl: string;
}

export interface WorkflowHelper {
  enabled: boolean;
  workflowAssistApps: WorkflowAssistApp[];
}

export interface WorkflowAssistApp {
  appUrl: string;
  appLabelKey: string;
}

export interface Sextant {}
