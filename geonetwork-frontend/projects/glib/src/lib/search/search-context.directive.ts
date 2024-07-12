import {
  Directive,
  HostAttributeToken,
  inject,
  input,
  OnInit,
} from '@angular/core';
import { SearchStore } from './search.state';
import { SearchService } from './search.service';

@Directive({
  selector: '[gSearchContext]',
  standalone: true,
  providers: [SearchStore],
})
export class SearchContextDirective implements OnInit {
  searchId = inject(new HostAttributeToken('gSearchContext'));
  searchStore = inject(SearchStore);

  aggregations = input<any>({});

  private searchService = inject(SearchService);

  ngOnInit(): void {
    this.searchStore.init(this.searchId, this.aggregations());
    this.searchService.register(this.searchId, this.searchStore);
  }
}
