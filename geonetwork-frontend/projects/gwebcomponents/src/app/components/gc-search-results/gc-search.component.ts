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
import {
  API_CONFIGURATION,
  SearchService,
  SearchAggLayout,
  ResultsLayout,
  DEFAULT_PAGE_SIZE,
} from 'glib';

@Component({
  selector: 'gc-search',
  templateUrl: './gc-search.component.html',
  styleUrl: './gc-search.component.css',
  encapsulation: ViewEncapsulation.ShadowDom,
  providers: [
    { provide: API_CONFIGURATION, useValue: signal(DefaultConfig) },
    SearchService,
  ],
})
export class GcSearchComponent implements OnInit, OnChanges {
  @Input() apiUrl: string;
  @Input() searchId = Math.random().toString().slice(2, 5);
  @Input() size = 30; //DEFAULT_PAGE_SIZE;
  @Input() filter: string;
  @Input() layout = ResultsLayout.CARD;
  @Input() layoutClass = 'grid grid-cols-3 gap-4 grid-flow-row';
  @Input() landingPage: string;
  @Input() landingPageLinkPath: string;

  apiConfiguration = inject(API_CONFIGURATION);
  currentFilter = signal<string>('');
  currentSize = signal(DEFAULT_PAGE_SIZE);

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
      }
    });
  }

  ngOnInit() {
    this.apiConfiguration.set(new Configuration({ basePath: this.apiUrl }));
    this.currentFilter.set(this.filter);
    this.currentSize.set(this.size);
  }

  protected readonly SearchAggLayout = SearchAggLayout;
}
