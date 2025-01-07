import { GcDataResultsTableComponent } from './components/gc-data-results-table/gc-data-results-table.component';
import { CUSTOM_ELEMENTS_SCHEMA, Injector, NgModule } from '@angular/core';
import { createCustomElement } from '@angular/elements';
import {
  DataTableComponent,
  DataUploadComponent,
  NewRecordPanelComponent,
  SearchAggComponent,
  SearchBarSimpleComponent,
  SearchContextDirective,
  SearchInputComponent,
  SearchResultsComponent,
  SearchResultsTableComponent,
} from 'glib';
import { CommonModule } from '@angular/common';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { GcSearchComponent } from './components/gc-search-results/gc-search.component';
import { GcSearchResultsTableComponent } from './components/gc-search-results-table/gc-search-results-table.component';
import { ToolbarModule } from 'primeng/toolbar';
import { FloatLabelModule } from 'primeng/floatlabel';
import { MultiSelectModule } from 'primeng/multiselect';
import { FieldsetModule } from 'primeng/fieldset';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputTextModule } from 'primeng/inputtext';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { Button } from 'primeng/button';
import { SliderModule } from 'primeng/slider';
import { InputSwitchModule } from 'primeng/inputswitch';
import { GcNewRecordPanelComponent } from './components/gc-new-record-panel/gc-new-record-panel.component';

const CUSTOM_ELEMENTS = [
  {
    component: GcDataResultsTableComponent,
    selector: 'gc-data-results-table-component',
  },
  {
    component: GcSearchComponent,
    selector: 'gc-search-component',
  },
  {
    component: GcSearchResultsTableComponent,
    selector: 'gc-search-results-table-component',
  },
  {
    component: GcNewRecordPanelComponent,
    selector: 'gc-new-record-panel-component',
  },
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    DataTableComponent,
    DropdownModule,
    FormsModule,
    SearchContextDirective,
    SearchAggComponent,
    SearchInputComponent,
    SearchResultsComponent,
    SearchResultsTableComponent,
    ToolbarModule,
    FloatLabelModule,
    MultiSelectModule,
    FieldsetModule,
    InputGroupModule,
    InputTextModule,
    AutoCompleteModule,
    Button,
    SliderModule,
    InputSwitchModule,
    SearchBarSimpleComponent,
    DataUploadComponent,
    NewRecordPanelComponent,
  ],
  bootstrap: [],
  exports: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {
  constructor(private injector: Injector) {
    console.log(CUSTOM_ELEMENTS.length + ' components to create...');
    CUSTOM_ELEMENTS.forEach(({ component, selector }) => {
      const el = createCustomElement(component, { injector });
      customElements.define(selector, el);
      console.log('Custom element created:', selector);
    });
  }

  ngDoBootstrap() {}
}
