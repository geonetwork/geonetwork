import { Component, Input, signal, SimpleChanges } from '@angular/core';
import { DefaultConfig } from 'gapi';
import {
  API_CONFIGURATION,
  DEFAULT_PAGE_SIZE,
  DEFAULT_SORT,
  SearchService,
} from 'glib';
import { GcBaseComponent } from './gc-base-component';

@Component({
  selector: 'gc-base-search-component',
  template: '<div></div>',
  providers: [
    { provide: API_CONFIGURATION, useValue: signal(DefaultConfig) },
    SearchService,
  ],
})
export class GcBaseSearchComponent extends GcBaseComponent {
  @Input({ alias: 'search-id' }) searchId = Math.random()
    .toString()
    .slice(2, 5);
  @Input() size = DEFAULT_PAGE_SIZE;
  @Input() filter: string;
  @Input({ alias: 'sort' }) sort: string;
  @Input({ alias: 'full-text-search' }) fullTextSearch: boolean = true;
  @Input({ alias: 'list-of-filter' }) listOfFilter: string;
  @Input({ alias: 'list-of-filter-placeholder' })
  listOfFilterPlaceholders: string;
  @Input({ alias: 'list-of-filter-layout' }) listOfFilterLayouts: string;
  @Input({ alias: 'landing-page' }) landingPage: string;
  @Input({ alias: 'landing-page-link-on' }) landingPageLinkOn: string;
  @Input({ alias: 'selection-mode' }) selectionMode:
    | 'single'
    | 'multiple'
    | undefined;

  currentFilter = signal<string>('');
  currentSize = signal(DEFAULT_PAGE_SIZE);
  currentSort = signal(DEFAULT_SORT);
  fullTextSearchEnabled = signal(true);
  filters = signal<string[]>([]);
  filterPlaceholders = signal<string[]>([]);
  filterLayouts = signal<string[]>([]);

  inputToField: Record<string, string> = {
    listOfFilter: 'filters',
    listOfFilterPlaceholders: 'filterPlaceholders',
    listOfFilterLayouts: 'filterLayouts',
  };

  override ngOnInit() {
    super.ngOnInit();
    this.currentFilter.set(this.filter);
    this.fullTextSearchEnabled.set(this.fullTextSearch);
    this.currentSize.set(this.size);
    this.#parseSortField();
    // this.listOfFilter && this.filters.set(this.listOfFilter.split(','));
  }

  #parseSortField() {
    if (this.sort) {
      try {
        this.currentSort.set(JSON.parse(this.sort));
      } catch (e) {
        this.currentSort.set(
          this.sort.split(',').map(field => {
            const order = field.startsWith('-') ? 'desc' : 'asc';
            return { [field.replace(/^-/, '')]: order };
          })
        );
      }
    }
  }

  override ngOnChanges(changes: SimpleChanges): void {
    super.ngOnChanges(changes);
    Object.keys(changes).forEach(prop => {
      if (prop == 'filter') {
        this['currentFilter'].set(changes[prop].currentValue);
      } else if (prop == 'size') {
        this['currentSize'].set(changes[prop].currentValue);
      } else if (prop == 'sort') {
        this.#parseSortField();
      } else if (prop == 'fullTextSearch') {
        this.fullTextSearchEnabled.set(changes[prop].currentValue);
      } else if (this.inputToField[prop]) {
        (this as any)[this.inputToField[prop]].set(
          typeof changes[prop].currentValue === 'boolean'
            ? changes[prop].currentValue
            : changes[prop].currentValue
                .split(',')
                .filter((v: string) => v !== '')
        );
      }
    });
  }
}
