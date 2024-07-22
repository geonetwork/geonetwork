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
import { GnAngularjsComponent } from './components/gn-angularjs/gn-angularjs.component';
import { GWebcomponentsDocComponent } from './components/g-webcomponents-doc/g-webcomponents-doc.component';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { GcSearchComponent } from './components/gc-search-results/gc-search.component';
import { GcSearchResultsTableComponent } from './components/gc-search-results-table/gc-search-results-table.component';
import { ToolbarModule } from 'primeng/toolbar';

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
    GnAngularjsComponent,
    GWebcomponentsDocComponent,
    GcSearchComponent,
    GcSearchResultsTableComponent,
  ],
  imports: [
    CommonModule,
    DataTableComponent,
    DropdownModule,
    FormsModule,
    Button,
    SearchContextDirective,
    SearchAggComponent,
    SearchInputComponent,
    SearchResultsComponent,
    SearchResultsTableComponent,
    ToolbarModule,
  ],
  bootstrap: [GcDataResultsTableComponent, GcSearchComponent],
  exports: [GcDataResultsTableComponent, GcSearchComponent],
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
