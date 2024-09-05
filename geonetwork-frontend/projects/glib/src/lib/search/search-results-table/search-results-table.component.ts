import { Component, effect, inject, input } from '@angular/core';
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
import { UrlPlaceholderPipe } from '../../shared/url-placeholder.pipe';
import Papa from 'papaparse';
import { DownloadService } from '../../shared/download.service';
import { ButtonGroupModule } from 'primeng/buttongroup';

enum ExportFormat {
  CSV = 'CSV',
  JSON = 'JSON',
}

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
    UrlPlaceholderPipe,
    ButtonGroupModule,
  ],
})
export class SearchResultsTableComponent extends SearchBaseComponent {
  fields = input.required<string[]>(); // fields to display as table mode
  labels = input<string[]>();
  selectionMode = input<'single' | 'multiple' | undefined>();
  scrollHeight = input('flex');
  landingPage = input<string>('');
  landingPageLinkOn = input<string>();
  isAllowingExport = input<boolean>(false);
  apiUrl = input<string>('');

  exportFormats = [ExportFormat.CSV];

  selectedRecords: any[] = [];

  gJsonPath = inject(GJsonpathPipe);

  downloadService = inject(DownloadService);

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

  exportTable(format: ExportFormat) {
    if (this.search.response()) {
      const data = [this.fields()].concat(
        this.search.response()!.hits.hits.map(hit => {
          const record = hit._source;
          return this.fields().map(field => {
            // TODO: Would require a CSV field renderer to handle complex fields
            let value = this.gJsonPath.transform(record, field);
            if (field.indexOf('overview') !== -1) {
              return value[0].url;
            }
            return format === ExportFormat.JSON ? value : JSON.stringify(value);
          });
        })
      );
      if (format === ExportFormat.JSON) {
        this.downloadService.downloadAsFile(
          JSON.stringify(data),
          'application/json',
          'search-results.json'
        );
      } else {
        this.downloadService.downloadAsFile(
          Papa.unparse(data),
          'text/csv',
          'search-results.csv'
        );
      }
    }
  }

  protected readonly ResourceTypeLayout = ResourceTypeLayout;
}
