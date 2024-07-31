import {
  Component,
  inject,
  Input,
  OnChanges,
  OnInit,
  signal,
  SimpleChanges,
  ViewEncapsulation,
} from '@angular/core';
import { Configuration, DefaultConfig } from 'gapi';
import { API_CONFIGURATION, SearchAggLayout, SearchService } from 'glib';
import { TablePageEvent } from 'primeng/table';

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
export class GcSearchResultsTableComponent implements OnInit, OnChanges {
  @Input() apiUrl: string;
  @Input() listOfFilter: string;
  @Input() listOfFields: string;
  @Input() listOfLabels: string;
  @Input() landingPage: string;
  @Input() landingPageLinkOn: string;
  @Input() selectionMode: 'single' | 'multiple' | undefined;
  @Input() scrollHeight: string;
  @Input() searchId = Math.random().toString().slice(2, 5);

  apiConfiguration = inject(API_CONFIGURATION);

  filters = signal<string[]>([]);
  fields = signal<string[]>([]);
  labels = signal<string[]>([]);

  ngOnChanges(changes: SimpleChanges): void {
    Object.keys(changes).forEach(prop => {
      if (prop == 'apiUrl') {
        this.apiConfiguration.set(
          new Configuration({ basePath: changes['apiUrl'].currentValue })
        );
      } else if (prop == 'listOfFields' || prop == 'listOfLabels') {
        this[prop == 'listOfFields' ? 'fields' : 'labels'].set(
          changes[prop].currentValue.split(',')
        );
      }
    });
  }

  ngOnInit() {
    this.apiConfiguration.set(new Configuration({ basePath: this.apiUrl }));
    this.listOfFilter && this.filters.set(this.listOfFilter.split(','));
    this.listOfFields && this.fields.set(this.listOfFields.split(','));
    this.listOfLabels && this.labels.set(this.listOfLabels.split(','));
  }

  protected readonly SearchAggLayout = SearchAggLayout;
}
