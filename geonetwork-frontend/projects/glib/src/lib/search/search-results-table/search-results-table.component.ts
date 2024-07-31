import { Component, effect, input } from '@angular/core';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { TableModule, TablePageEvent } from 'primeng/table';
import { Button } from 'primeng/button';
import { JsonPipe, NgTemplateOutlet } from '@angular/common';
import { GJsonpathPipe } from '../../shared/g-jsonpath.pipe';
import { RecordFieldOverviewComponent } from '../../record/record-field-overview/record-field-overview.component';
import {
  RecordFieldResourceTypeComponent,
  ResourceTypeLayout,
} from '../../record/record-field-resource-type/record-field-resource-type.component';
import { RecordFieldLinksComponent } from '../../record/record-field-links/record-field-links.component';
import { RecordFieldKeywordsComponent } from '../../record/record-field-keywords/record-field-keywords.component';

@Component({
  selector: 'g-search-results-table',
  templateUrl: './search-results-table.component.html',
  styleUrl: './search-results-table.component.css',
  standalone: true,
  imports: [
    TableModule,
    Button,
    JsonPipe,
    GJsonpathPipe,
    RecordFieldOverviewComponent,
    NgTemplateOutlet,
    RecordFieldResourceTypeComponent,
    RecordFieldLinksComponent,
    RecordFieldKeywordsComponent,
  ],
})
export class SearchResultsTableComponent extends SearchBaseComponent {
  fields = input.required<string[]>(); // fields to display as table mode
  labels = input<string[]>();
  selectionMode = input<'single' | 'multiple' | undefined>();
  scrollHeight = input('flex');
  landingPage = input<string>('');
  landingPageLinkOn = input<string>();
  apiUrl = input<string>('');

  selectedRecords: any[] = [];

  #validateInputs() {
    if (
      this.landingPageLinkOn() &&
      this.fields()?.indexOf(this.landingPageLinkOn() || '') === -1
    ) {
      console.error(
        'landingPageLinkOn field not found in list of fields : ' +
          this.fields()?.join(', ')
      );
    }
  }

  buildLandingPageLink(uuid: string) {
    return (
      this.landingPage()
        ?.replace('${uuid}', uuid)
        .replace('${apiUrl}', this.apiUrl()) || ''
    );
  }

  constructor() {
    super();

    this.#validateInputs();

    effect(() => {
      this.selectionMode();
      this.selectedRecords = [];
    });
  }

  pageChange(event: TablePageEvent) {
    this.search.setPage(event.first, event.rows);
  }

  protected readonly ResourceTypeLayout = ResourceTypeLayout;
}
