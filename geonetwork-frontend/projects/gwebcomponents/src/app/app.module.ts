import { GDataResultsTableComponent } from './components/g-data-results-table/g-data-results-table.component';
import { CUSTOM_ELEMENTS_SCHEMA, Injector, NgModule } from '@angular/core';
import { createCustomElement } from '@angular/elements';
import {
  DataTableComponent,
  SearchContextDirective,
  SearchInputComponent,
  SearchResultsComponent,
} from 'glib';
import { CommonModule } from '@angular/common';
import { GnAngularjsComponent } from './components/gn-angularjs/gn-angularjs.component';
import { GWebcomponentsDocComponent } from './components/g-webcomponents-doc/g-webcomponents-doc.component';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { GSearchComponent } from './components/g-search-results/g-search.component';

@NgModule({
  declarations: [
    GDataResultsTableComponent,
    GnAngularjsComponent,
    GWebcomponentsDocComponent,
    GSearchComponent,
  ],
  imports: [
    CommonModule,
    DataTableComponent,
    DropdownModule,
    FormsModule,
    Button,
    SearchContextDirective,
    SearchInputComponent,
    SearchResultsComponent,
  ],
  providers: [],
  bootstrap: [GDataResultsTableComponent, GSearchComponent],
  exports: [GDataResultsTableComponent, GSearchComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {
  constructor(private injector: Injector) {
    const elData = createCustomElement(GDataResultsTableComponent, {
      injector,
    });
    customElements.define('g-data-results-table-component', elData);
    const el = createCustomElement(GSearchComponent, { injector });
    customElements.define('g-search-component', el);
  }

  ngDoBootstrap() {}
}
