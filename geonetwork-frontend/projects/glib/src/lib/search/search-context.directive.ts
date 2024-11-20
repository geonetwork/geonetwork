import { Directive, effect, inject, input, OnInit } from '@angular/core';
import {
  DEFAULT_PAGE_SIZE,
  DEFAULT_SCORE,
  DEFAULT_SORT,
  SearchStore,
} from './search.state';
import { SearchService } from './search.service';
import { API_CONFIGURATION } from '../config/config.loader';
import { elasticsearch } from 'gapi';

@Directive({
  selector: '[gSearchContext]',
  standalone: true,
  providers: [SearchStore],
})
export class SearchContextDirective implements OnInit {
  scope = input<string>('', { alias: 'gSearchContext' });
  aggregations = input<any>({});
  score = input<any>(DEFAULT_SCORE);
  size = input<number>(DEFAULT_PAGE_SIZE);
  sort = input<elasticsearch.Sort>(DEFAULT_SORT);
  filter = input<string>('');

  #searchStore = inject(SearchStore);
  #searchService = inject(SearchService);
  #apiConfiguration = inject(API_CONFIGURATION);

  constructor() {
    // On API configuration change, reset the search
    effect(
      () => {
        this.#apiConfiguration() && this.#searchStore.reset();
      },
      { allowSignalWrites: true }
    );
    effect(
      () => {
        this.filter() && this.#searchStore.setFilter(this.filter());
      },
      { allowSignalWrites: true }
    );
    effect(
      () => {
        this.size() && this.#searchStore.setPageSize(this.size());
      },
      { allowSignalWrites: true }
    );
    effect(
      () => {
        this.sort() && this.#searchStore.setSort(this.sort());
      },
      { allowSignalWrites: true }
    );
  }

  ngOnInit(): void {
    this.#searchStore.init(
      this.scope(),
      this.aggregations(),
      this.score(),
      this.size(),
      this.sort(),
      this.filter()
    );
    this.#searchService.register(this.scope(), this.#searchStore);
  }
}
