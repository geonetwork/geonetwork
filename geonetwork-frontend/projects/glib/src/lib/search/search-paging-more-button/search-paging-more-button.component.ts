import { Component, computed, input } from '@angular/core';
import { Button } from 'primeng/button';
import { SearchBaseComponent } from '../search-base/search-base.component';

@Component({
  selector: 'g-search-paging-more-button',
  standalone: true,
  imports: [Button],
  templateUrl: './search-paging-more-button.component.html',
  styleUrl: './search-paging-more-button.component.css',
})
export class SearchPagingMoreButtonComponent extends SearchBaseComponent {
  size = input<number>();

  pageSize = computed(() => this.search.pageSize());

  buttonLabel = computed(() =>
    this.search.hasMore()
      ? `Load ${this.search.pageSize()} more results ...`
      : 'No more results'
  );

  override ngOnInit() {
    super.ngOnInit();
    this.size() && this.search.setPageSize(this.size() as number);
  }

  more() {
    this.search.more(this.pageSize());
  }
}
