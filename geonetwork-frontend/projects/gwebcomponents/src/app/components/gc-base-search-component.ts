import { Component, Input, signal, SimpleChanges } from '@angular/core';
import { DefaultConfig } from 'gapi';
import { API_CONFIGURATION, DEFAULT_PAGE_SIZE, SearchService } from 'glib';
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
  @Input({ alias: 'full-text-search' }) fullTextSearch: boolean = true;
  @Input({ alias: 'list-of-filter' }) listOfFilter: string;
  @Input({ alias: 'landing-page' }) landingPage: string;
  @Input({ alias: 'landing-page-link-on' }) landingPageLinkOn: string;
  @Input({ alias: 'selection-mode' }) selectionMode:
    | 'single'
    | 'multiple'
    | undefined;

  currentFilter = signal<string>('');
  currentSize = signal(DEFAULT_PAGE_SIZE);
  fullTextSearchEnabled = signal(true);
  filters = signal<string[]>([]);

  inputToField: Record<string, string> = {
    listOfFilter: 'filters',
  };

  override ngOnInit() {
    super.ngOnInit();
    this.currentFilter.set(this.filter);
    this.fullTextSearchEnabled.set(this.fullTextSearch);
    this.currentSize.set(this.size);
    this.listOfFilter && this.filters.set(this.listOfFilter.split(','));
  }

  override ngOnChanges(changes: SimpleChanges): void {
    super.ngOnChanges(changes);
    Object.keys(changes).forEach(prop => {
      if (prop == 'filter') {
        this['currentFilter'].set(changes[prop].currentValue);
      } else if (prop == 'size') {
        this['currentSize'].set(changes[prop].currentValue);
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
