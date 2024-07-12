import {
  Component,
  HostAttributeToken,
  inject,
  input,
  OnInit,
} from '@angular/core';
import { SearchService } from '../search.service';
import { SearchStoreType } from '../search.state';
import { PaginatorModule, PaginatorState } from 'primeng/paginator';

@Component({
  selector: 'g-search-paging',
  standalone: true,
  imports: [PaginatorModule],
  templateUrl: './search-paging.component.html',
  styleUrl: './search-paging.component.css',
})
export class SearchPagingComponent implements OnInit {
  scope = inject(new HostAttributeToken('scope'));
  size = input<number>();
  searchService = inject(SearchService);
  search: SearchStoreType;

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope);
    this.size() && this.search.setPageSize(this.size() as number);
  }

  onPageChange($event: PaginatorState) {
    if (
      $event.first !== undefined &&
      $event.rows !== undefined &&
      !isNaN(Number($event.first)) &&
      !isNaN(Number($event.rows))
    ) {
      this.search.setPage($event.first, $event.rows);
    }
  }
}
