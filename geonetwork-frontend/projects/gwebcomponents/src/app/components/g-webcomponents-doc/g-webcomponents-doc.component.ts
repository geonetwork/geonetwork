import { Component, computed, input, OnInit, signal } from '@angular/core';
import {
  AutoComplete,
  AutoCompleteCompleteEvent,
  AutoCompleteSelectEvent,
} from 'primeng/autocomplete';
import { Fieldset } from 'primeng/fieldset';
import { DropdownModule } from 'primeng/dropdown';
import { InputText } from 'primeng/inputtext';
import { InputSwitch } from 'primeng/inputswitch';
import { GcDataResultsTableComponent } from '../gc-data-results-table/gc-data-results-table.component';
import { GWebcomponentsDocEmbedComponent } from '../g-webcomponents-doc-embed/g-webcomponents-doc-embed.component';
import { GWebcomponentConfigurationComponent } from '../g-webcomponent-configuration/g-webcomponent-configuration.component';
import { GcSearchComponent } from '../gc-search-results/gc-search.component';
import { GcSearchResultsTableComponent } from '../gc-search-results-table/gc-search-results-table.component';
import { Select } from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { GcNewRecordPanelComponent } from '../gc-new-record-panel/gc-new-record-panel.component';
import { IndexRecordToJSON } from 'g5api';

interface field {
  label: string;
  path: string;
  order: number;
}
export const API_URL_LIST = [
  { url: 'https://metawal.wallonie.be/geonetwork/srv/api' },
  { url: 'https://metadata.vlaanderen.be/srv/api' },
  { url: 'https://www.nationaalgeoregister.nl/geonetwork/srv/api' },
  { url: 'https://sextant.ifremer.fr/geonetwork/srv/api' },
  { url: 'https://catalogue.ec.gc.ca/geonetwork/srv/api' },
  { url: 'https://apps.titellus.net/geonetwork/srv/api' },
  { url: 'https://www.geocatalogue.fr/geonetwork/srv/api' },
  { url: 'http://localhost:8080/geonetwork/srv/api' },
  { url: 'http://localhost:4200/geonetwork/srv/api' },
];

export const SELECTION_MODE_LIST = [
  { label: 'none', type: undefined },
  { label: 'single', type: 'single' },
  { label: 'multiple', type: 'multiple' },
];

export const FILTER_LIST = [
  'OrgForResourceObject.default',
  'custodianOrgForResourceObject.default',
  'cl_topic.default',
  'cl_status.default',
  'cl_spatialRepresentationType.default',
  'resolutionScaleDenominator',
  'creationYearForResource',
  'th_gemet.default',
  'resourceType',
  'isPublishedToAll',
];

export const CSV_SOURCE_FILE_LIST = [
  'https://static.data.gouv.fr/resources/journees-europeennes-du-patrimoine/20160914-151814/Journees_europeennes_du_patrimoine_20160914.csv',
  'https://www.odwb.be/api/explore/v2.1/catalog/datasets/pw_agenda/exports/csv?lang=fr&timezone=Europe%2FBrussels&use_labels=true&delimiter=,',
  'https://www.data.gouv.fr/fr/datasets/r/ca621361-ec32-458a-8c40-e27bdcf58b16',
];

@Component({
  selector: 'g-webcomponents-doc',
  templateUrl: './g-webcomponents-doc.component.html',
  imports: [
    Fieldset,
    DropdownModule,
    InputText,
    InputSwitch,
    GcDataResultsTableComponent,
    GWebcomponentsDocEmbedComponent,
    GWebcomponentConfigurationComponent,
    GcSearchComponent,
    AutoComplete,
    GcSearchResultsTableComponent,
    Select,
    FormsModule,
    GcNewRecordPanelComponent,
  ],
})
export class GWebcomponentsDocComponent implements OnInit {
  webcomponentId = input('');

  apiUrl = API_URL_LIST[0].url;

  selectedSourceFile = signal(CSV_SOURCE_FILE_LIST[0]);

  allowDataTableExport = signal(false);
  allowResultsTableExport = signal(false);
  allowColumnsSelection = signal(false);

  selectionMode = SELECTION_MODE_LIST[0].type;

  indexRecordProperties = Object.keys(IndexRecordToJSON({}));

  fullTextSearch = signal(true);
  searchFilter = signal('+isTemplate:n');
  pageSize = signal(15);
  //sort = signal('{"resourceTitleObject.default.keyword": "asc"}');
  sort = signal('resourceTitleObject.default.keyword');

  protected readonly csvSourceFiles = CSV_SOURCE_FILE_LIST;

  isArrayProperty(v: string) {
    const arrayProperties = ['overview'];
    return (
      arrayProperties.indexOf(v) !== -1 ||
      v.startsWith('th_') ||
      v.startsWith('cl_')
    );
  }

  listOfFields = this.indexRecordProperties
    .map((v, index) => {
      return {
        label: v,
        path: v.endsWith('Object')
          ? v + '.default'
          : this.isArrayProperty(v)
            ? v + '[*]'
            : v,
        order: index,
      };
    })
    .sort((a, b) => {
      return a.label.localeCompare(b.label);
    });

  filteredFields: field[] = [];

  jsonPrefix = '$._source';

  listOfFieldsSimple: field[] = [
    { label: 'Aperçu', path: this.jsonPrefix + '.overview[*]', order: 1 },
    {
      label: 'Titre',
      path: this.jsonPrefix + '.resourceTitleObject.default',
      order: 2,
    },
    { label: 'Type', path: this.jsonPrefix + '.resourceType[0]', order: 3 },
    { label: 'Mot clé', path: this.jsonPrefix + '.th_gemet[*]', order: 4 },
    {
      label: 'Status',
      path: this.jsonPrefix + '.cl_status[0].default',
      order: 5,
    },
    {
      label: 'Légende',
      path: this.jsonPrefix + ".link[?(@.function == 'legend')]",
      order: 6,
    },
    {
      label: 'Visualiser',
      path: this.jsonPrefix + ".link[?(@.protocol == 'OGC:WMS')]",
      order: 7,
    },
    {
      label: 'Gestionnaire',
      path: this.jsonPrefix + '.custodianOrgForResourceObject[*].default',
      order: 8,
    },
    {
      label: 'Crédit',
      path: this.jsonPrefix + '.resourceCreditObject.default',
      order: 9,
    },
  ];

  listOfFieldsAsText = signal(
    this.listOfFieldsSimple
      .map(f => {
        return f.path;
      })
      .join(',')
  );
  listOfLabelsAsText = signal(
    this.listOfFieldsSimple
      .map(f => {
        return f.label;
      })
      .join(',')
  );

  selectedFilters = signal(
    [
      'OrgForResourceObject.default',
      'cl_topic.default',
      'cl_status.default',
      'resourceType',
    ].join(',')
  );
  selectedFilterPlaceholders = signal(
    ['Organisation', 'Topic', 'Status', 'Type'].join(',')
  );
  listOfFilters = computed(() => {
    return this.selectedFilters();
    // return this.selectedFilters() ? this.selectedFilters().join(',') : '';
  });
  listOfFilterPlaceholders = computed(() => {
    return this.selectedFilterPlaceholders();
    // return this.selectedFilterLabels() ? this.selectedFilterLabels().join(',') : '';
  });
  listOfFilterLayouts = signal('MULTISELECT,DROPDOWN');

  autocompleteField(event: AutoCompleteCompleteEvent) {
    let query = event.query.split(',').at(-1)?.toLowerCase() || '';
    this.filteredFields = this.listOfFields.filter(f => {
      return f.label.toLowerCase().indexOf(query) !== -1;
    });
  }

  autocompleteKeyboardEvent(event: KeyboardEvent) {
    this.listOfFieldsAsText.set((<HTMLInputElement>event.target).value);
  }

  autocompleteAddValue(event: AutoCompleteSelectEvent) {
    this.listOfFieldsAsText.set(
      [this.listOfFieldsAsText(), event.value.path].filter(i => !!i).join(',')
    );
    this.listOfLabelsAsText.set(
      [this.listOfLabelsAsText(), event.value.label].filter(i => !!i).join(',')
    );
  }

  ngOnInit() {}
}
