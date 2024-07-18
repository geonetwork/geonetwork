import { Directive, HostListener, inject, input, OnInit } from '@angular/core';
import { SearchService } from './search.service';
import { SearchStoreType } from './search.state';

@Directive({
  selector: '[gSearchQueryReset]',
  standalone: true,
})
export class SearchQueryResetDirective implements OnInit {
  scope = input<string>('', { alias: 'gSearchQueryReset' });
  searchService = inject(SearchService);
  search: SearchStoreType;

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope());
  }

  @HostListener('click', ['$event'])
  public onClick(event: KeyboardEvent): void {
    this.search.reset();
  }
}
