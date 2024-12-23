import { Component, CUSTOM_ELEMENTS_SCHEMA, model } from '@angular/core';
import {
  API_URL_LIST,
  CSV_SOURCE_FILE_LIST,
  FILTER_LIST,
  SELECTION_MODE_LIST,
} from '../g-webcomponents-doc/g-webcomponents-doc.component';
import { SearchAggLayout } from 'glib';
import { Fieldset } from 'primeng/fieldset';
import { InputSwitch } from 'primeng/inputswitch';
import { DropdownModule } from 'primeng/dropdown';
import { InputText } from 'primeng/inputtext';
import { Slider } from 'primeng/slider';
import { FormsModule } from '@angular/forms';
import { KeyValuePipe } from '@angular/common';
import { Select } from 'primeng/select';

@Component({
  selector: 'g-webcomponent-configuration',
  templateUrl: './g-webcomponent-configuration.component.html',
  imports: [
    Fieldset,
    InputSwitch,
    DropdownModule,
    InputText,
    Slider,
    FormsModule,
    KeyValuePipe,
    Select,
  ],
})
export class GWebcomponentConfigurationComponent {
  apiUrl = model<string>();
  fullTextSearch = model<boolean>();
  searchFilter = model<string>();
  pageSize = model<number>();
  sort = model<string>();
  selectedFilters = model<string>();
  selectedFilterPlaceholders = model<string>();
  selectedFilterLayouts = model<string>();
  selectionMode = model<string>();
  protected readonly apiUrlList = API_URL_LIST;
  protected readonly selectionModeList = SELECTION_MODE_LIST;
  protected readonly filterList = FILTER_LIST;
  protected readonly SearchAggLayout = SearchAggLayout;
}
