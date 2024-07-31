import { Component, input } from '@angular/core';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { SearchAggComponent } from '../search-agg/search-agg.component';
import { KeyValuePipe } from '@angular/common';

@Component({
  selector: 'g-search-aggs-container',
  templateUrl: './search-aggs-container.component.html',
  styleUrl: './search-aggs-container.component.css',
  standalone: true,
  imports: [SearchAggComponent, KeyValuePipe],
})
export class SearchAggsContainerComponent extends SearchBaseComponent {
  exclude = input<string[]>([]);

  isAggregationDisplayed(key: string) {
    return this.exclude().length == 0 || !this.exclude().includes(key);
  }
}
