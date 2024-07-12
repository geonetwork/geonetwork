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
  selector: '[gSearchQuerySetter]',
  standalone: true,
})
export class SearchQuerySetterDirective implements OnInit {
  scope = inject(new HostAttributeToken('gSearchQuerySetter'));
  searchService = inject(SearchService);
  search: SearchStoreType;
  // freeTextInput: ElementRef<HTMLInputElement>;
  //
  // constructor(private el: ElementRef) {
  //   this.freeTextInput = el;
  // }

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope);
  }

  @HostListener('keyup', ['$event'])
  public onKeyup(event: KeyboardEvent): void {
    const value = (event.target as HTMLInputElement).value;
    this.search.setFullTextQuery(value);
  }
}
