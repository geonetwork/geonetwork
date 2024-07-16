import { Component, input } from '@angular/core';
import { PaginatorModule, PaginatorState } from 'primeng/paginator';
import { SearchBaseComponent } from '../search-base/search-base.component';

@Component({
  selector: 'g-search-paging',
  standalone: true,
  imports: [PaginatorModule],
  templateUrl: './search-paging.component.html',
  styleUrl: './search-paging.component.css',
})
export class SearchPagingComponent extends SearchBaseComponent {
  size = input<number>();

  override ngOnInit() {
    super.ngOnInit();
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
