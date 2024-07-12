export const MISSING_CONFIG_ERROR = 'Configuration not yet loaded.';

export const APP_CONFIG = 'app.config';

export const DEFAULT_APP_CONFIG = {
  langDetector: {
    fromHtmlTag: false,
    regexp: '^(?:/.+)?/.+/([a-z]{2,3})/.+',
    default: 'eng',
  },
  nodeDetector: {
    regexp: '^(?:/.+)?/(.+)/[a-z]{2,3}/.+',
    default: 'srv',
  },
  serviceDetector: {
    regexp: '^(?:/.+)?/.+/[a-z]{2,3}/(.+)',
    default: 'catalog.search',
  },
  baseURLDetector: {
    regexp: '^((?:/.+)?)+/.+/[a-z]{2,3}/.+',
    default: '/geonetwork',
  },
  mods: {
    global: {
      hotkeys: true,
      humanizeDates: true,
      dateFormat: 'DD-MM-YYYY',
      timezone: 'Browser', // Default to browser timezone
    },
    footer: {
      enabled: true,
      showSocialBarInFooter: true,
      showApplicationInfoAndLinksInFooter: true,
      footerCustomMenu: [], // List of static pages identifiers to display
      rssFeeds: [
        {
          // List of rss feeds links to display when the OGC API Records service is enabled
          url: 'f=rss&sortby=-createDate&limit=30',
          label: 'lastCreatedRecords',
        },
        // , {
        //   url: "f=rss&sortby=-publicationDateForResource&limit=30",
        //   label: "lastPublishedRecords"
        // }
      ],
    },
    header: {
      enabled: true,
      languages: {
        arm: 'hy',
        aze: 'az',
        eng: 'en',
        cat: 'ca',
        chi: 'zh',
        cze: 'cs',
        dan: 'da',
        geo: 'ka',
        ger: 'de',
        fre: 'fr',
        spa: 'es',
        ice: 'is',
        ita: 'it',
        dut: 'nl',
        kor: 'ko',
        por: 'pt',
        rum: 'ro',
        rus: 'ru',
        slo: 'sk',
        fin: 'fi',
        ukr: 'uk',
        swe: 'sv',
        wel: 'cy',
      },
      isLogoInHeader: false,
      logoInHeaderPosition: 'left',
      fluidHeaderLayout: true,
      showGNName: true,
      isHeaderFixed: false,
      showPortalSwitcher: true,
      topCustomMenu: [], // List of static pages identifiers to display
    },
    cookieWarning: {
      enabled: true,
      cookieWarningMoreInfoLink: '',
      cookieWarningRejectLink: '',
    },
    home: {
      enabled: true,
      appUrl: '../../{{node}}/{{lang}}/catalog.search#/home',
      showSearch: true,
      showSocialBarInFooter: true,
      showMosaic: true,
      showMaps: true,
      facetConfig: {
        'th_httpinspireeceuropaeutheme-theme_tree.key': {
          terms: {
            field: 'th_httpinspireeceuropaeutheme-theme_tree.key',
            size: 34,
            // "order" : { "_key" : "asc" }
          },
          meta: {
            decorator: {
              type: 'icon',
              prefix: 'fa fa-2x pull-left gn-icon iti-',
              expression: 'http://inspire.ec.europa.eu/theme/(.*)',
            },
            orderByTranslation: true,
          },
        },
        'cl_topic.key': {
          terms: {
            field: 'cl_topic.key',
            size: 20,
          },
          meta: {
            decorator: {
              type: 'icon',
              prefix: 'fa fa-2x pull-left gn-icon-',
            },
            orderByTranslation: true,
          },
        },
        // 'OrgForResource': {
        //   'terms': {
        //     'field': 'OrgForResourceObject',
        //     'include': '.*',
        //     'missing': '- No org -',
        //     'size': 15
        //   }
        // },
        resourceType: {
          terms: {
            field: 'resourceType',
            size: 10,
          },
          meta: {
            decorator: {
              type: 'icon',
              prefix: 'fa fa-2x pull-left gn-icon-',
            },
          },
        },
      },
      info: [
        {
          type: 'search',
          title: 'lastRecords',
          active: true,
          params: {
            isTemplate: 'n',
            sortBy: 'createDate',
            sortOrder: 'desc',
            from: 1,
            to: 12,
          },
        },
        {
          type: 'search',
          title: 'preferredRecords',
          params: {
            isTemplate: 'n',
            sortBy: 'popularity',
            sortOrder: 'desc',
            from: 1,
            to: 12,
          },
        },
        {
          type: 'featuredUserSearches',
        },
        {
          type: 'Comments',
        },
      ],
      fluidLayout: true,
    },
    search: {
      enabled: true,
      appUrl: '../../{{node}}/{{lang}}/catalog.search#/search',
      hitsperpageValues: [30, 60, 120],
      paginationInfo: {
        hitsPerPage: 30,
      },
      // Full text on all fields
      // 'queryBase': '${any}',
      // Full text but more boost on title match
      // * Search in languages depending on the strategy selected
      queryBase:
        'any.${searchLang}:(${any}) OR any.common:(${any}) OR resourceTitleObject.${searchLang}:(${any})^2 OR resourceTitleObject.\\*:"${any}"^6',
      queryBaseOptions: {
        default_operator: 'AND',
      },
      // TODO: Exact match should not even analyze
      // so we could create an exact field not analyzed in the index maybe?
      queryExactMatch:
        'any.${searchLang}:"${any})" OR any.common:"${any}" OR resourceTitleObject.\\*:"${any}"^2',
      // * Force UI language - in this case set languageStrategy to searchInUILanguage
      // and disable language options in searchOptions
      // 'queryBase': 'any.${uiLang}:(${any}) any.common:(${any}) resourceTitleObject.${uiLang}:(${any})^2',
      // * Search in French fields (with french analysis)
      // 'queryBase': 'any.langfre:(${any}) any.common:(${any}) resourceTitleObject.langfre:(${any})^2',
      queryTitle: 'resourceTitleObject.\\*:(${any})',
      queryTitleExactMatch: 'resourceTitleObject.\\*:"${any}"',
      searchOptions: {
        fullText: true,
        titleOnly: true,
        exactMatch: true,
        language: true,
      },
      // The language strategy define how to search on multilingual content.
      // It also applies to aggregation using ${aggLanguage} substitute.
      // Language strategy can be:
      // * searchInUILanguage: search in UI languages
      // eg. full text field is any.langfre if French, aggLanguage is uiLanguage.
      // * searchInAllLanguages: search using any.* fields, aggLanguage is default
      // (no analysis is done, more records are returned)
      // * searchInDetectedLanguage: restrict the search to the language detected
      // based on user search. aggLanguage is detectedLanguage.
      // If language detection fails, search in all languages and aggLanguage is uiLanguage
      // * searchInThatLanguage: Force a language using searchInThatLanguage:fre
      // 'languageStrategy': 'searchInThatLanguage:fre',
      // aggLanguage is forcedLanguage.
      languageStrategy: 'searchInAllLanguages',
      // Limit language detection to some languages only.
      // If empty, the list of languages in catalogue records is used
      // and if none found, mods.header.languages is used.
      languageWhitelist: [],
      // Score query may depend on where we are in the app?
      scoreConfig: {
        // Score experiments:
        // a)Score down old records
        // {
        //   "gauss": {
        //     "dateStamp": {
        //       "scale":  "200d"
        //     }
        //   }
        // }
        // b)Promote grids!
        // "boost": "5",
        // "functions": [
        //   {
        //     "filter": { "match": { "cl_spatialRepresentationType.key": "vector" } },
        //     "random_score": {},
        //     "weight": 23
        //   },
        //   {
        //     "filter": { "match": { "cl_spatialRepresentationType.key": "grid" } },
        //     "weight": 42
        //   }
        // ],
        // "max_boost": 42,
        // "score_mode": "max",
        // "boost_mode": "multiply",
        // "min_score" : 42
        // "script_score" : {
        //   "script" : {
        //     "source": "_score"
        //     // "source": "Math.log(2 + doc['rating'].value)"
        //   }
        // }
        boost: '5',
        functions: [
          {
            filter: { match: { resourceType: 'series' } },
            weight: 1.5,
          },
          // Boost down member of a series
          {
            filter: { exists: { field: 'parentUuid' } },
            weight: 0.3,
          },
          // Boost down obsolete and superseded records
          {
            filter: { match: { 'cl_status.key': 'obsolete' } },
            weight: 0.2,
          },
          {
            filter: { match: { 'cl_status.key': 'superseded' } },
            weight: 0.3,
          },
          // {
          //   "filter": { "match": { "cl_resourceScope": "service" } },
          //   "weight": 0.8
          // },
          // Start boosting down records more than 3 months old
          {
            gauss: {
              changeDate: {
                scale: '365d',
                offset: '90d',
                decay: 0.5,
              },
            },
          },
        ],
        score_mode: 'multiply',
      },
      autocompleteConfig: {
        query: {
          bool: {
            must: [
              {
                multi_match: {
                  query: '',
                  type: 'bool_prefix',
                  fields: [
                    'resourceTitleObject.${searchLang}^6',
                    'resourceAbstractObject.${searchLang}^.5',
                    'tag',
                    'uuid',
                    'resourceIdentifier',
                    // "anytext",
                    // "anytext._2gram",
                    // "anytext._3gram"
                  ],
                },
              },
            ],
          },
        },
        _source: ['resourceTitle*', 'resourceType'],
        size: 20,
      },
      moreLikeThisSameType: true,
      moreLikeThisConfig: {
        more_like_this: {
          fields: [
            'resourceTitleObject.default',
            'resourceAbstractObject.default',
            'tag.raw',
          ],
          like: null,
          min_term_freq: 1,
          min_word_length: 3,
          max_query_terms: 35,
          // "analyzer": "english",
          minimum_should_match: '70%',
        },
      },
      facetTabField: '',
      // Enable vega only if using vega facet type
      // See https://github.com/geonetwork/core-geonetwork/pull/5349
      isVegaEnabled: true,
      facetConfig: {
        resourceType: {
          terms: {
            field: 'resourceType',
          },
          meta: {
            decorator: {
              type: 'icon',
              prefix: 'fa fa-fw gn-icon-',
            },
          },
        },
        // Use .default for not multilingual catalogue with one language only UI.
        // 'cl_spatialRepresentationType.default': {
        //   'terms': {
        //     'field': 'cl_spatialRepresentationType.default',
        //     'size': 10
        //   }
        // },
        // Use .key for codelist for multilingual catalogue.
        // The codelist translation needs to be loaded in the client app. See GnSearchModule.js
        'cl_spatialRepresentationType.key': {
          terms: {
            field: 'cl_spatialRepresentationType.key',
            size: 10,
          },
        },
        format: {
          terms: {
            field: 'format',
          },
          meta: {
            collapsed: true,
          },
        },
        availableInServices: {
          filters: {
            //"other_bucket_key": "others",
            // But does not support to click on it
            filters: {
              availableInViewService: {
                query_string: {
                  query: '+linkProtocol:/OGC:WMS.*/',
                },
              },
              availableInDownloadService: {
                query_string: {
                  query: '+linkProtocol:/OGC:WFS.*/',
                },
              },
            },
          },
          meta: {
            decorator: {
              type: 'icon',
              prefix: 'fa fa-fw ',
              map: {
                availableInViewService: 'fa-globe',
                availableInDownloadService: 'fa-download',
              },
            },
          },
        },
        // GEMET configuration for non multilingual catalog
        'th_gemet_tree.key': {
          terms: {
            field: 'th_gemet_tree.key',
            size: 100,
            order: { _key: 'asc' },
            include: '[^^]+^?[^^]+',
            // Limit to 2 levels
          },
        },
        // GEMET configuration for multilingual catalog
        // The key is translated on client side by loading
        // required concepts
        // 'th_gemet_tree.key': {
        //   'terms': {
        //     'field': 'th_gemet_tree.key',
        //     'size': 100,
        //     "order" : { "_key" : "asc" },
        //     "include": "[^\^]+^?[^\^]+"
        //     // Limit to 2 levels
        //   }
        // },
        // (Experimental) A tree field which contains a URI
        // eg. http://www.ifremer.fr/thesaurus/sextant/theme#52
        // but with a translation which contains a hierarchy with a custom separator
        // /Regulation and Management/Technical and Management Zonations/Sensitive Zones
        // 'th_sextant-theme_tree.key': {
        //   'terms': {
        //     'field': 'th_sextant-theme_tree.key',
        //     'size': 100,
        //     "order" : { "_key" : "asc" }
        //   },
        //   'meta': {
        //     'translateOnLoad': true,
        //     'treeKeySeparator': '/'
        //   }
        // },

        'th_httpinspireeceuropaeumetadatacodelistPriorityDataset-PriorityDataset_tree.default':
          {
            terms: {
              field:
                'th_httpinspireeceuropaeumetadatacodelistPriorityDataset-PriorityDataset_tree.default',
              size: 100,
              order: { _key: 'asc' },
            },
          },
        'th_httpinspireeceuropaeutheme-theme_tree.key': {
          terms: {
            field: 'th_httpinspireeceuropaeutheme-theme_tree.key',
            size: 34,
            // "order" : { "_key" : "asc" }
          },
          meta: {
            decorator: {
              type: 'icon',
              prefix: 'fa fa-fw gn-icon iti-',
              expression: 'http://inspire.ec.europa.eu/theme/(.*)',
            },
          },
        },
        tag: {
          terms: {
            field: 'tag.${aggLang}',
            include: '.*',
            size: 10,
          },
          meta: {
            caseInsensitiveInclude: true,
          },
        },
        'th_regions_tree.default': {
          terms: {
            field: 'th_regions_tree.default',
            size: 100,
            order: { _key: 'asc' },
            //"include": "EEA.*"
          },
        },
        // "resolutionScaleDenominator": {
        //   "terms": {
        //     "field": "resolutionScaleDenominator",
        //     "size": 20,
        //     "order": {
        //       "_key": "asc"
        //     }
        //   }
        // },
        resolutionScaleDenominator: {
          histogram: {
            field: 'resolutionScaleDenominator',
            interval: 10000,
            keyed: true,
            min_doc_count: 1,
          },
          meta: {
            collapsed: true,
          },
        },
        // "serviceType": {
        //   'collapsed': true,
        //   "terms": {
        //     "field": "serviceType",
        //     "size": 10
        //   }
        // },
        // "resourceTemporalDateRange": {
        //   "date_histogram": {
        //     "field": "resourceTemporalDateRange",
        //     "fixed_interval": "1900d",
        //     "min_doc_count": 1
        //   }
        // },
        creationYearForResource: {
          histogram: {
            field: 'creationYearForResource',
            interval: 5,
            keyed: true,
            min_doc_count: 1,
          },
          meta: {
            collapsed: true,
          },
        },
        // "creationYearForResource": {
        //   "terms": {
        //     "field": "creationYearForResource",
        //     "size": 10,
        //     "order": {
        //       "_key": "desc"
        //     }
        //   }
        // },
        OrgForResource: {
          terms: {
            field: 'OrgForResourceObject.${aggLang}',
            // field: "OrgForResourceObject.default",
            // field: "OrgForResourceObject.langfre",
            include: '.*',
            size: 20,
          },
          meta: {
            // Always display filter even no more elements
            // This can be used when all facet values are loaded
            // with a large size and you want to provide filtering.
            // 'displayFilter': true,
            caseInsensitiveInclude: true,
            // decorator: {
            //   type: 'img',
            //   map: {
            //     'EEA': 'https://upload.wikimedia.org/wikipedia/en/thumb/7/79/EEA_agency_logo.svg/220px-EEA_agency_logo.svg.png'
            //   }
            // }
          },
        },
        'cl_maintenanceAndUpdateFrequency.key': {
          terms: {
            field: 'cl_maintenanceAndUpdateFrequency.key',
            size: 10,
          },
          meta: {
            collapsed: true,
          },
          // },
          // Don't forget to enable Vega to use interactive graphic facets.
          // See isVegaEnabled property.
          // 'cl_status.key': {
          //   'terms': {
          //     'field': 'cl_status.key',
          //     'size': 10
          //   },
          //   'meta': {
          //     // 'vega': 'bar'
          //     'vega': 'arc'
          //   }
          // },
          //
          // 'resourceTemporalDateRange': {
          //   'gnBuildFilterForRange': {
          //     field: "resourceTemporalDateRange",
          //     buckets: 2021 - 1970,
          //     dateFormat: 'YYYY',
          //     dateSelectMode: 'years',
          //     vegaDateFormat: '%Y',
          //     from: 1970,
          //     to: 2021,
          //     mark: 'area'
          //   },
          //   'meta': {
          //     'vega': 'timeline'
          //   }
          // },
          // 'dateStamp' : {
          //   'auto_date_histogram' : {
          //     'field' : 'dateStamp',
          //     'buckets': 50
          //   },
          //   "meta": {
          //     'userHasRole': 'isReviewerOrMore',
          //     'collapsed': true
          //   }
        },
      },
      filters: null,
      // 'filters': [{
      //     "query_string": {
      //       "query": "-resourceType:service"
      //     }
      //   }],
      sortbyValues: [
        {
          sortBy: 'relevance',
          sortOrder: '',
        },
        {
          sortBy: 'changeDate',
          sortOrder: 'desc',
        },
        {
          sortBy: 'createDate',
          sortOrder: 'desc',
        },
        {
          sortBy: 'resourceTitleObject.default.sort',
          sortOrder: '',
        },
        {
          sortBy: 'rating',
          sortOrder: 'desc',
        },
        {
          sortBy: 'popularity',
          sortOrder: 'desc',
        },
      ],
      sortBy: 'relevance',
      resultViewTpls: [
        {
          tplUrl:
            '../../catalog/components/' +
            'search/resultsview/partials/viewtemplates/grid.html',
          tooltip: 'Grid',
          icon: 'fa-th',
          related: [],
        },
        {
          tplUrl:
            '../../catalog/components/' +
            'search/resultsview/partials/viewtemplates/list.html',
          tooltip: 'List',
          icon: 'fa-bars',
          related: ['parent', 'children', 'services', 'datasets'],
        },
        {
          tplUrl:
            '../../catalog/components/' +
            'search/resultsview/partials/viewtemplates/table.html',
          tooltip: 'Table',
          icon: 'fa-table',
          related: [],
          source: {
            exclude: ['resourceAbstract*', 'Org*', 'contact*'],
          },
        },
      ],
      // Optional. If not set, the first resultViewTpls is used.
      resultTemplate:
        '../../catalog/components/' +
        'search/resultsview/partials/viewtemplates/grid.html',
      searchResultContact: 'OrgForResource',
      formatter: {
        list: [
          {
            label: 'defaultView',
            // Conditional views can be used to configure custom
            // formatter to use depending on metadata properties.
            // 'views': [{
            //   'if': {'standardName': 'ISO 19115-3 - Emodnet Checkpoint - Targeted Data Product'},
            //   'url' : '/formatters/xsl-view?root=div&view=advanced'
            // }, {
            //   'if': {
            //     'standardName': [
            //       'ISO 19115:2003/19139 - EMODNET - BATHYMETRY',
            //       'ISO 19115:2003/19139 - EMODNET - HYDROGRAPHY']
            //   },
            //   'url' : '/formatters/xsl-view?root=div&view=emodnetHydrography'
            // }, {
            //   'if': {'documentStandard': 'iso19115-3.2018'},
            //   'url' : '/dada'
            // }],
            url: '',
          },
          {
            label: 'full',
            url: '/formatters/xsl-view?root=div&view=advanced',
          },
        ],
      },
      downloadFormatter: [
        {
          label: 'exportMEF',
          url: '/formatters/zip?withRelated=false',
          class: 'fa-file-zip-o',
        },
        {
          label: 'exportPDF',
          url: '/formatters/xsl-view?output=pdf&language=${lang}',
          class: 'fa-file-pdf-o',
        },
        {
          label: 'exportXML',
          // 'url' : '/formatters/xml?attachment=false',
          url: '/formatters/xml',
          class: 'fa-file-code-o',
        } /*,
              {
                label: "exportDCAT",
                url: "/geonetwork/api/collections/main/items/${uuid}?f=dcat",
                class: "fa-file-code-o"
              }*/,
      ],
      // Deprecated (use configuration on resultViewTpls)
      grid: {
        related: ['parent', 'children', 'services', 'datasets'],
      },
      linkTypes: {
        links: ['LINK'],
        downloads: ['WWW:DOWNLOAD', 'WWW:OPENDAP', 'WWW:FTP', 'KML'],
        // 'downloadServices': [
        //   'OGC:WFS',
        //   'OGC:WCS',
        //   'ATOM'
        // ],
        layers: [
          'OGC:WMS',
          // 'OGC:WFS',
          'OGC:WMTS',
          'ESRI:REST',
        ],
        maps: ['ows'],
      },
      isFilterTagsDisplayedInSearch: true,
      searchMapPlacement: 'results', // results, facets or none
      showStatusFooterFor: 'historicalArchive,obsolete,superseded',
      showBatchDropdown: true,
      usersearches: {
        enabled: false,
        includePortals: true,
        displayFeaturedSearchesPanel: false,
      },
      savedSelection: {
        enabled: false,
      },
      addWMSLayersToMap: {
        urlLayerParam: '',
      },
    },
    map: {
      enabled: true,
      appUrl: '../../{{node}}/{{lang}}/catalog.search#/map',
      externalViewer: {
        enabled: false,
        enabledViewAction: false,
        baseUrl: 'http://www.example.com/viewer',
        urlTemplate:
          'http://www.example.com/viewer?url=${service.url}&type=${service.type}&layer=${service.title}&lang=${iso2lang}&title=${md.defaultTitle}',
        openNewWindow: false,
        valuesSeparator: ',',
      },
      is3DModeAllowed: false,
      singleTileWMS: true,
      isSaveMapInCatalogAllowed: true,
      isExportMapAsImageEnabled: false,
      isAccessible: false,
      storage: 'sessionStorage',
      bingKey: '',
      listOfServices: {
        wms: [],
        wmts: [],
        wps: [],
      },
      // wpsSource: ["list", "url", "recent"],
      wpsSource: ['url', 'recent'],
      projection: 'EPSG:3857',
      projectionList: [
        {
          code: 'urn:ogc:def:crs:EPSG:6.6:4326',
          label: 'WGS84 (EPSG:4326)',
        },
        {
          code: 'EPSG:3857',
          label: 'Google mercator (EPSG:3857)',
        },
      ],
      switcherProjectionList: [
        {
          code: 'EPSG:3857',
          label: 'Google mercator (EPSG:3857)',
        },
      ],
      disabledTools: {
        processes: false,
        addLayers: false,
        projectionSwitcher: false,
        layers: false,
        legend: false,
        filter: false,
        contexts: false,
        print: false,
        mInteraction: false,
        graticule: false,
        mousePosition: true,
        syncAllLayers: false,
        drawVector: false,
        scaleLine: false,
      },
      defaultTool: 'layers',
      defaultToolAfterMapLoad: 'layers',
      graticuleOgcService: {},
      'map-viewer': {
        context: '../../map/config-viewer.xml',
        extent: [0, 0, 0, 0],
        layers: [],
      },
      'map-search': {
        context: '../../map/config-viewer.xml',
        extent: [0, 0, 0, 0],
        layers: [],
        geodesicExtents: false,
      },
      'map-editor': {
        context: '../../map/config-viewer.xml',
        extent: [0, 0, 0, 0],
        layers: [],
      },
      'map-thumbnail': {
        context: '../../map/config-viewer.xml',
        extent: [0, 0, 0, 0],
        layers: [],
      },
      autoFitOnLayer: false,
    },
    geocoder: {
      enabled: true,
      appUrl: 'https://secure.geonames.org/searchJSON',
    },
    recordview: {
      // To use to redirect to another application for rendering record
      // eg. when embedding simple search results using a web component
      // and redirecting to the catalogue to view metadata record
      // appUrl: "https://sextant.ifremer.fr/Donnees/Catalogue",
      isSocialbarEnabled: true,
      showStatusWatermarkFor: '',
      showStatusTopBarFor: '',
      showCitation: {
        enabled: false,
        // if: {'documentStandard': ['iso19115-3.2018']}
        if: { resourceType: ['series', 'dataset', 'nonGeographicDataset'] },
      },
      sortKeywordsAlphabetically: true,
      mainThesaurus: ['th_gemet', 'th_gemet-theme'],
      locationThesaurus: [
        'th_regions',
        'th_httpinspireeceuropaeumetadatacodelistSpatialScope-SpatialScope',
      ],
      internalThesaurus: [],
      collectionTableConfig: {
        labels: 'title,cl_status,format,download,WMS,WFS,Atom,Links',
        columns:
          'resourceTitle,cl_status[0].key,format,link/protocol:WWW:DOWNLOAD.*,link/protocol:OGC:WMS,link/protocol:OGC:WFS,link/protocol:atom:feed,link/protocol:WWW:LINK.*',
      },
      distributionConfig: {
        // 'layout': 'tabset',
        layout: '',
        sections: [
          {
            filter:
              'protocol:OGC:WMS|OGC:WMTS|ESRI:.*|atom.*|REST|OGC API Maps|OGC API Records',
            title: 'API',
          },
          {
            filter:
              'protocol:OGC:WFS|OGC:WCS|.*DOWNLOAD.*|DB:.*|FILE:.*|OGC API Features|OGC API Coverages',
            title: 'download',
          },
          { filter: 'function:legend', title: 'mapLegend' },
          {
            filter: 'function:featureCatalogue',
            title: 'featureCatalog',
          },
          {
            filter: 'function:dataQualityReport',
            title: 'quality',
          },
          {
            filter:
              '-protocol:OGC.*|REST|ESRI:.*|atom.*|.*DOWNLOAD.*|DB:.*|FILE:.* AND -function:legend|featureCatalogue|dataQualityReport',
            title: 'links',
          },
        ],
      },
      relatedFacetConfig: {
        cl_status: {
          terms: {
            field: 'cl_status.default',
            order: { _key: 'asc' },
          },
        },
        creationYearForResource: {
          terms: {
            field: 'creationYearForResource',
            size: 100,
            order: { _key: 'asc' },
          },
        },
        cl_spatialRepresentationType: {
          terms: {
            field: 'cl_spatialRepresentationType.default',
            order: { _key: 'asc' },
          },
        },
        format: {
          terms: {
            field: 'format',
            order: { _key: 'asc' },
          },
        },
      },
    },
    editor: {
      enabled: true,
      appUrl: '../../{{node}}/{{lang}}/catalog.edit',
      isUserRecordsOnly: false,
      minUserProfileToCreateTemplate: '',
      isFilterTagsDisplayed: false,
      fluidEditorLayout: true,
      createPageTpl:
        '../../catalog/templates/editor/new-metadata-horizontal.html',
      editorIndentType: '',
      allowRemoteRecordLink: true,
      workflowSearchRecordTypes: ['n', 'e'],
      facetConfig: {
        resourceType: {
          terms: {
            field: 'resourceType',
          },
          meta: {
            decorator: {
              type: 'icon',
              prefix: 'fa fa-fw gn-icon-',
            },
          },
        },
        mdStatus: {
          terms: {
            field: 'statusWorkflow',
            size: 20,
          },
          meta: {
            field: 'statusWorkflow',
          },
        },
        'cl_status.key': {
          terms: {
            field: 'cl_status.key',
            size: 15,
          },
        },
        valid: {
          terms: {
            field: 'valid',
            size: 10,
          },
        },
        valid_inspire: {
          terms: {
            field: 'valid_inspire',
            size: 10,
          },
          meta: {
            collapsed: true,
          },
        },
        sourceCatalogue: {
          terms: {
            field: 'sourceCatalogue',
            size: 100,
            include: '.*',
          },
          meta: {
            orderByTranslation: true,
            filterByTranslation: true,
            displayFilter: true,
            collapsed: true,
            // decorator: {
            //   type: "img",
            //   path: "../../images/logos/{key}.png"
            // }
          },
        },
        groupOwner: {
          terms: {
            field: 'groupOwner',
            size: 200,
            include: '.*',
          },
          meta: {
            orderByTranslation: true,
            filterByTranslation: true,
            displayFilter: true,
            collapsed: true,
          },
        },
        recordOwner: {
          terms: {
            field: 'recordOwner',
            size: 5,
            include: '.*',
          },
          meta: {
            collapsed: true,
          },
        },
        isPublishedToAll: {
          terms: {
            field: 'isPublishedToAll',
            size: 2,
          },
          meta: {
            decorator: {
              type: 'icon',
              prefix: 'fa fa-fw ',
              map: {
                false: 'fa-lock',
                true: 'fa-lock-open',
              },
            },
          },
        },
        groupPublishedId: {
          terms: {
            field: 'groupPublishedId',
            size: 200,
            include: '.*',
          },
          meta: {
            orderByTranslation: true,
            filterByTranslation: true,
            displayFilter: true,
            collapsed: true,
          },
        },
        documentStandard: {
          terms: {
            field: 'documentStandard',
            size: 10,
          },
          meta: {
            collapsed: true,
          },
        },
        isHarvested: {
          terms: {
            field: 'isHarvested',
            size: 2,
          },
          meta: {
            collapsed: true,
            decorator: {
              type: 'icon',
              prefix: 'fa fa-fw ',
              map: {
                false: 'fa-folder',
                true: 'fa-cloud',
              },
            },
          },
        },
        isTemplate: {
          terms: {
            field: 'isTemplate',
            size: 5,
          },
          meta: {
            collapsed: true,
            decorator: {
              type: 'icon',
              prefix: 'fa fa-fw ',
              map: {
                n: 'fa-file-text',
                y: 'fa-file',
              },
            },
          },
        },
      },
    },
    directory: {
      sortbyValues: [
        {
          sortBy: 'relevance',
          sortOrder: '',
        },
        {
          sortBy: 'changeDate',
          sortOrder: 'desc',
        },
        {
          sortBy: 'resourceTitleObject.default.sort',
          sortOrder: '',
        },
        {
          sortBy: 'recordOwner',
          sortOrder: '',
        },
        {
          sortBy: 'valid',
          sortOrder: 'desc',
        },
      ],
      sortBy: 'relevance',
      facetConfig: {
        valid: {
          terms: {
            field: 'valid',
            size: 10,
          },
        },
        groupOwner: {
          terms: {
            field: 'groupOwner',
            size: 10,
          },
        },
        recordOwner: {
          terms: {
            field: 'recordOwner',
            size: 10,
          },
        },
        groupPublished: {
          terms: {
            field: 'groupPublished',
            size: 10,
          },
        },
        isHarvested: {
          terms: {
            field: 'isHarvested',
            size: 2,
          },
        },
      },
      // Add some fuzziness when search on directory entries
      // but boost exact match.
      queryBase:
        'any.${searchLang}:(${any}) OR any.common:(${any}) OR resourceTitleObject.${searchLang}:"${any}"^10 OR resourceTitleObject.${searchLang}:(${any})^5 OR resourceTitleObject.${searchLang}:(${any}~2)',
    },
    admin: {
      enabled: true,
      appUrl: '../../{{node}}/{{lang}}/admin.console',
      facetConfig: {
        availableInServices: {
          filters: {
            //"other_bucket_key": "others",
            // But does not support to click on it
            filters: {
              availableInViewService: {
                query_string: {
                  query: '+linkProtocol:/OGC:WMS.*/',
                },
              },
              availableInDownloadService: {
                query_string: {
                  query: '+linkProtocol:/OGC:WFS.*/',
                },
              },
            },
          },
        },
        resourceType: {
          terms: {
            field: 'resourceType',
          },
          meta: {
            vega: 'arc',
          },
        },
        'tag.default': {
          terms: {
            field: 'tag.default',
            size: 10,
          },
          meta: {
            vega: 'arc',
          },
        },
        indexingErrorMsg: {
          terms: {
            field: 'indexingErrorMsg',
            size: 12,
          },
        },
      },
    },
    authentication: {
      enabled: true,
      signinUrl: '../../{{node}}/{{lang}}/catalog.signin',
      signoutUrl: '../../signout',
    },
    page: {
      enabled: true,
      appUrl: '../../{{node}}/{{lang}}/catalog.search#/page',
    },
    workflowHelper: {
      enabled: false,
      workflowAssistApps: [{ appUrl: '', appLabelKey: '' }],
    },
  },
};
