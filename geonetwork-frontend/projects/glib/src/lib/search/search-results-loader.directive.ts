import {
  AfterViewInit,
  Directive,
  EnvironmentInjector,
  inject,
  input,
  OnInit,
  Optional,
  runInInjectionContext,
} from '@angular/core';
import { Dropdown } from 'primeng/dropdown';
import { toObservable } from '@angular/core/rxjs-interop';
import { SearchService } from './search.service';
import { SearchStoreType } from './search.state';

@Directive({
  selector: '[gSearchResultsLoader]',
  standalone: true,
})
export class SearchResultsLoaderDirective implements OnInit {
  injector = inject(EnvironmentInjector);
  scope = input<string>('', { alias: 'gSearchResultsLoader' });
  searchService = inject(SearchService);
  search: SearchStoreType;

  constructor(@Optional() private dropdown: Dropdown) {}

  public ngOnInit(): void {
    this.search = this.searchService.getSearch(this.scope());
    if (this.dropdown) {
      runInInjectionContext(this.injector, () => {
        toObservable(this.search.response).subscribe(() => {
          if (this.search.response() != null) {
            const options = this.search.response()!.hits.hits.map(hit => {
              return {
                label: hit._source!.resourceTitleObject!.default,
                type: hit._source!.resourceType![0],
                value: hit._id,
              };
            });
            this.dropdown.options = options;
            // if (options.length > 0) {
            //   this.dropdown.modelValue.set(options[0].value);
            // }
          }
        });
      });
    }
  }
}
