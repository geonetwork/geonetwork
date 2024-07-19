import { Directive, effect, inject, input, OnInit } from '@angular/core';
import { SearchStore } from './search.state';
import { SearchService } from './search.service';
import { API_CONFIGURATION } from '../config/config.loader';

@Directive({
  selector: '[gSearchContext]',
  standalone: true,
  providers: [SearchStore],
})
export class SearchContextDirective implements OnInit {
  scope = input<string>('', { alias: 'gSearchContext' });
  aggregations = input<any>({});

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
  }

  ngOnInit(): void {
    this.#searchStore.init(this.scope(), this.aggregations());
    this.#searchService.register(this.scope(), this.#searchStore);
  }
}
