import { Component, model } from '@angular/core';
import {
  API_URL_LIST,
  CSV_SOURCE_FILE_LIST,
  FILTER_LIST,
  SELECTION_MODE_LIST,
} from '../g-webcomponents-doc/g-webcomponents-doc.component';

@Component({
  selector: 'g-webcomponent-configuration',
  templateUrl: './g-webcomponent-configuration.component.html',
  styleUrl: './g-webcomponent-configuration.component.css',
})
export class GWebcomponentConfigurationComponent {
  apiUrl = model<string>();
  fullTextSearch = model<boolean>();
  searchFilter = model<string>();
  pageSize = model<number>();
  selectedFilters = model<string[]>();
  selectionMode = model<string>();
  protected readonly apiUrlList = API_URL_LIST;
  protected readonly selectionModeList = SELECTION_MODE_LIST;
  protected readonly filterList = FILTER_LIST;
}
