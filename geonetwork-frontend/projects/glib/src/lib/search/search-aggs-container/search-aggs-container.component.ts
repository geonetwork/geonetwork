import { Component, input } from '@angular/core';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { SearchAggComponent } from '../search-agg/search-agg.component';
import { KeyValuePipe } from '@angular/common';
import { SearchAggSelectButtonComponent } from '../search-agg-selectbutton/search-agg-select-button.component';

@Component({
  selector: 'g-search-aggs-container',
  templateUrl: './search-aggs-container.component.html',
  styleUrl: './search-aggs-container.component.css',
  standalone: true,
  imports: [SearchAggComponent, KeyValuePipe, SearchAggSelectButtonComponent],
})
export class SearchAggsContainerComponent extends SearchBaseComponent {
  exclude = input<string[]>([]);
  type = input<string | undefined>();

  isAggregationDisplayed(key: string) {
    const aggregationPlacement =
      this.search.aggregationConfig()[key].meta?.placement;
    if (this.type()) {
      return aggregationPlacement == this.type();
    } else if (this.exclude().length == 0) {
      return aggregationPlacement === undefined;
    } else {
      return (
        aggregationPlacement === undefined && !this.exclude().includes(key)
      );
    }
  }
}
