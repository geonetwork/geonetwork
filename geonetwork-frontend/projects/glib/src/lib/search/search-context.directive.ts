import { Directive, effect, inject, input, model, OnInit } from '@angular/core';
import {
  DEFAULT_PAGE_SIZE,
  DEFAULT_SCORE,
  DEFAULT_SORT,
  SearchStore,
} from './search.state';
import { SearchService } from './search.service';
import { API_CONFIGURATION } from '../config/config.loader';
import { elasticsearch } from 'gapi';
import { IndexRecord } from 'g5api';

@Directive({
  selector: '[gSearchContext]',
  standalone: true,
  providers: [SearchStore],
})
export class SearchContextDirective implements OnInit {
  scope = input<string>('', { alias: 'gSearchContext' });
  routing = input<boolean>(false);
  aggregations = input<any>({});
  score = input<any>(DEFAULT_SCORE);
  size = input<number>(DEFAULT_PAGE_SIZE);
  sort = input<elasticsearch.Sort>(DEFAULT_SORT);
  filter = input<string>('');
  response = model<elasticsearch.SearchResponse<IndexRecord> | null>();

  #searchStore = inject(SearchStore);
  #searchService = inject(SearchService);
  #apiConfiguration = inject(API_CONFIGURATION);

  constructor() {
    // On API configuration change, reset the search
    effect(() => {
      this.#apiConfiguration() && this.#searchStore.reset();
    });
    effect(() => {
      this.filter() && this.#searchStore.setFilter(this.filter());
    });
    effect(() => {
      this.size() && this.#searchStore.setPageSize(this.size());
    });
    effect(() => {
      this.sort() && this.#searchStore.setSort(this.sort());
    });
    effect(() => {
      this.response.set(this.#searchStore.response());
    });
  }

  ngOnInit(): void {
    this.#searchStore.init(
      this.scope(),
      this.aggregations(),
      this.score(),
      this.size(),
      this.sort(),
      this.filter(),
      this.routing()
    );
    this.#searchService.register(this.scope(), this.#searchStore);
  }
}
