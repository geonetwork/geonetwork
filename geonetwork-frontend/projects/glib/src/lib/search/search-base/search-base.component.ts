import { Component, inject, input, OnInit } from '@angular/core';
import { SearchService } from '../search.service';
import { SearchStoreType } from '../search.state';

@Component({
  selector: 'g-search-base',
  template: '',
  standalone: true,
})
export class SearchBaseComponent implements OnInit {
  // Not working in test scope = inject(new HostAttributeToken('scope'));
  scope = input<string>('');

  searchService = inject(SearchService);

  search: SearchStoreType;

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope());
  }
}
