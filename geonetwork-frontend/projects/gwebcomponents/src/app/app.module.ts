import { GcDataResultsTableComponent } from './components/gc-data-results-table/gc-data-results-table.component';
import { CUSTOM_ELEMENTS_SCHEMA, Injector, NgModule } from '@angular/core';
import { createCustomElement } from '@angular/elements';
import {
  DataTableComponent,
  SearchAggComponent,
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
import { GnAngularjsComponent } from './components/gn-angularjs/gn-angularjs.component';
import { GWebcomponentsDocComponent } from './components/g-webcomponents-doc/g-webcomponents-doc.component';
import { GWebcomponentsDocEmbedComponent } from './components/g-webcomponents-doc-embed/g-webcomponents-doc-embed.component';
import { Button } from 'primeng/button';

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
];

@NgModule({
  declarations: [
    GcDataResultsTableComponent,
    GcSearchComponent,
    GcSearchResultsTableComponent,
    GnAngularjsComponent,
    GWebcomponentsDocComponent,
    GWebcomponentsDocEmbedComponent,
  ],
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
  ],
  bootstrap: [
    GcDataResultsTableComponent,
    GcSearchComponent,
    GcSearchResultsTableComponent,
  ],
  exports: [
    GcDataResultsTableComponent,
    GcSearchComponent,
    GcSearchResultsTableComponent,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {
  constructor(private injector: Injector) {
    CUSTOM_ELEMENTS.forEach(({ component, selector }) => {
      const el = createCustomElement(component, { injector });
      customElements.define(selector, el);
    });
  }

  ngDoBootstrap() {}
}