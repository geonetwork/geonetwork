import { GDataResultsTableComponent } from './components/g-data-results-table/g-data-results-table.component';
import { CUSTOM_ELEMENTS_SCHEMA, Injector, NgModule } from '@angular/core';
import { createCustomElement } from '@angular/elements';
import { DataTableComponent } from 'glib';
import { CommonModule } from '@angular/common';
import { GnAngularjsComponent } from './components/gn-angularjs/gn-angularjs.component';
import { GWebcomponentsDocComponent } from './components/g-webcomponents-doc/g-webcomponents-doc.component';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { Button } from 'primeng/button';

@NgModule({
  declarations: [
    GDataResultsTableComponent,
    GnAngularjsComponent,
    GWebcomponentsDocComponent,
  ],
  imports: [
    CommonModule,
    DataTableComponent,
    DropdownModule,
    FormsModule,
    Button,
  ],
  providers: [],
  bootstrap: [GDataResultsTableComponent],
  exports: [GDataResultsTableComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {
  constructor(private injector: Injector) {
    const el = createCustomElement(GDataResultsTableComponent, { injector });
    customElements.define('g-data-results-table-component', el);
  }

  ngDoBootstrap() {}
}
