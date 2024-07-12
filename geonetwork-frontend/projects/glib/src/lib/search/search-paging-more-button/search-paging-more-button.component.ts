import {
  Component,
  computed,
  HostAttributeToken,
  inject,
  input,
  OnInit,
} from '@angular/core';
import { SearchService } from '../search.service';
import { SearchStoreType } from '../search.state';
import { Button } from 'primeng/button';

@Component({
  selector: 'g-search-paging-more-button',
  standalone: true,
  imports: [Button],
  templateUrl: './search-paging-more-button.component.html',
  styleUrl: './search-paging-more-button.component.css',
})
export class SearchPagingMoreButtonComponent implements OnInit {
  scope = inject(new HostAttributeToken('scope'));
  size = input<number>();
  searchService = inject(SearchService);
  search: SearchStoreType;

  pageSize = computed(() => this.search.pageSize());

  buttonLabel = computed(() =>
    this.search.hasMore()
      ? `Load ${this.search.pageSize()} more results`
      : 'No more results'
  );

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope);
    this.size() && this.search.setPageSize(this.size() as number);
  }

  more() {
    this.search.more(this.pageSize());
  }
}
