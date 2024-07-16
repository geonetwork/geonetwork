import {
  Directive,
  HostAttributeToken,
  HostListener,
  inject, input,
  OnInit,
} from '@angular/core';
import { SearchService } from './search.service';
import { SearchStoreType } from './search.state';

@Directive({
  selector: '[gSearchQuerySetter]',
  standalone: true,
})
export class SearchQuerySetterDirective implements OnInit {
  scope = input<string>('', {alias: 'gSearchQuerySetter'});
  searchService = inject(SearchService);
  search: SearchStoreType;

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope());
  }

  @HostListener('keyup', ['$event'])
  public onKeyup(event: KeyboardEvent): void {
    const value = (event.target as HTMLInputElement).value;
    this.search.setFullTextQuery(value);
  }
}
