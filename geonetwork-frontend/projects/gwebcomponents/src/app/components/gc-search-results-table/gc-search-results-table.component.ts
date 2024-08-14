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
import { API_CONFIGURATION, DEFAULT_PAGE_SIZE, SearchAggLayout, SearchService } from 'glib';

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
  @Input() size = DEFAULT_PAGE_SIZE;
  @Input() filter: string;
  @Input() listOfFilter: string;
  @Input() listOfFields: string;
  @Input() listOfLabels: string;
  @Input() landingPage: string;
  @Input() landingPageLinkOn: string;
  @Input() selectionMode: 'single' | 'multiple' | undefined;
  @Input() scrollHeight: string;
  @Input() searchId = Math.random().toString().slice(2, 5);

  apiConfiguration = inject(API_CONFIGURATION);
  currentFilter = signal<string>('');
  currentSize = signal(DEFAULT_PAGE_SIZE);

  filters = signal<string[]>([]);
  fields = signal<string[]>([]);
  labels = signal<string[]>([]);

  #inputToField: Record<string, string>  = {
    'listOfFilter': 'filters',
    'listOfFields': 'fields',
    'listOfLabels': 'labels'
  }

  ngOnChanges(changes: SimpleChanges): void {
    Object.keys(changes).forEach(prop => {
      if (prop == 'apiUrl') {
        this.apiConfiguration.set(
          new Configuration({ basePath: changes['apiUrl'].currentValue })
        );
      } else if (prop == 'filter') {
        this['currentFilter'].set(changes[prop].currentValue);
      } else if (prop == 'size') {
        this['currentSize'].set(changes[prop].currentValue);
      } else if (this.#inputToField[prop]) {
        (this as any)[this.#inputToField[prop]].set(
          changes[prop].currentValue.split(',')
        );
      }
    });
  }

  ngOnInit() {
    this.apiConfiguration.set(new Configuration({ basePath: this.apiUrl }));
    this.currentFilter.set(this.filter);
    this.currentSize.set(this.size);
    this.listOfFilter && this.filters.set(this.listOfFilter.split(','));
    this.listOfFields && this.fields.set(this.listOfFields.split(','));
    this.listOfLabels && this.labels.set(this.listOfLabels.split(','));
  }

  protected readonly SearchAggLayout = SearchAggLayout;
}
