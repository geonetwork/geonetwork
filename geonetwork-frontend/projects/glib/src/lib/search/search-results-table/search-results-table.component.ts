import { Component, effect, inject, input, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
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
import { MultiSelectModule } from 'primeng/multiselect';

enum ExportFormat {
  CSV = 'CSV',
  JSON = 'JSON',
}

interface Column {
  field: string;
  header: string;
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
    MultiSelectModule,
    FormsModule,
  ],
})
export class SearchResultsTableComponent extends SearchBaseComponent {
  fields = input.required<string[]>(); // fields to display as table mode
  isAllowingColumnSelection = input<boolean>(false);
  columns: Column[];
  selectedColumns = signal<Column[]>([]);
  labels = input<string[]>();
  currentSortField: string;
  currentSortOrder: number;
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

  override ngOnInit() {
    super.ngOnInit();
    this.columns = this.fields().map((col, index) => {
      let label = this.labels() ? this.labels()![index] : col;
      return { field: col, header: label };
    });
    this.selectedColumns.set(this.columns);
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

  sort(sortEvent: any) {
    //Updated p-table sorting icons.
    this.currentSortField = sortEvent.field;
    this.currentSortOrder = sortEvent.order;
    //Determine sorting order: 1 for ASCENDING , -1 for DESCENDING
    const order = sortEvent.order > 0 ? 'asc' : 'desc';
    //Build sort field name used in the query
    const sortField = this.searchService.getSortableField(sortEvent.field);
    //if undefined, this field is not suitable for sorting (should not occur if HTML template is correct)
    if (sortField != undefined) {
      let sort: any = {};
      sort[sortField] = order;
      this.search.setSort([sort]);
    }
  }

  protected readonly ResourceTypeLayout = ResourceTypeLayout;
}
