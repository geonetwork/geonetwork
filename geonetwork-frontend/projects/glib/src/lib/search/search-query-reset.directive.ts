import {
  Directive,
  HostAttributeToken,
  HostListener,
  inject,
  OnInit,
} from '@angular/core';
import { SearchService } from './search.service';
import { SearchStoreType } from './search.state';

@Directive({
  selector: '[gSearchQueryReset]',
  standalone: true,
})
export class SearchQueryResetDirective implements OnInit {
  scope = inject(new HostAttributeToken('gSearchQueryReset'));
  searchService = inject(SearchService);
  search: SearchStoreType;

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope);
  }

  @HostListener('click', ['$event'])
  public onClick(event: KeyboardEvent): void {
    this.search.reset();
  }
}
