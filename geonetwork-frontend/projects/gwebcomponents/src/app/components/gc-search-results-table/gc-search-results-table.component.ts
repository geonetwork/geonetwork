import {
  Component,
  Input,
  signal,
  SimpleChanges,
  ViewEncapsulation,
} from '@angular/core';
import { DefaultConfig } from 'gapi';
import { API_CONFIGURATION, SearchAggLayout, SearchService } from 'glib';
import { GcBaseSearchComponent } from '../gc-base-search-component';

@Component({
  selector: 'gc-search-results-table',
  templateUrl: './gc-search-results-table.component.html',
  styleUrl: './gc-search-results-table.component.css',
  encapsulation: ViewEncapsulation.ShadowDom,
  providers: [
    { provide: API_CONFIGURATION, useValue: signal(DefaultConfig) },
    SearchService,
  ],
})
export class GcSearchResultsTableComponent extends GcBaseSearchComponent {
  @Input({ alias: 'list-of-field' }) listOfField: string;
  @Input({ alias: 'list-of-label' }) listOfLabel: string;
  @Input({ alias: 'scroll-height' }) scrollHeight: string;
  @Input() export: boolean = false;
  @Input() columnSelection: boolean = false;

  fields = signal<string[]>([]);
  labels = signal<string[]>([]);

  isAllowingExport = signal<boolean>(this.export);
  isAllowingColumnSelection = signal<boolean>(this.columnSelection);

  override ngOnInit() {
    super.ngOnInit();
    this.inputToField = {
      ...this.inputToField,
      ...{
        listOfField: 'fields',
        listOfLabel: 'labels',
        export: 'isAllowingExport',
        columnSelection: 'isAllowingColumnSelection'
      },
    };
    this.listOfField && this.fields.set(this.listOfField.split(','));
    this.listOfLabel && this.labels.set(this.listOfLabel.split(','));
    this.isAllowingExport.set(this.export);
    this.isAllowingColumnSelection.set(this.columnSelection);
  }

  protected readonly SearchAggLayout = SearchAggLayout;
}
