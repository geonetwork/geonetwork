import { Component, input } from '@angular/core';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { SearchInputComponent } from '../search-input/search-input.component';
import {
  SearchAggComponent,
  SearchAggLayout,
} from '../search-agg/search-agg.component';

@Component({
  selector: 'g-search-bar-simple',
  standalone: true,
  imports: [SearchAggComponent, SearchInputComponent],
  templateUrl: './search-bar-simple.component.html',
})
export class SearchBarSimpleComponent extends SearchBaseComponent {
  fullTextSearchEnabled = input(true);
  filters = input<string[]>([]);
  filterPlaceholders = input<string[]>([]);
  filterLayouts = input<string[]>([]);
  protected readonly SearchAggLayout = SearchAggLayout;
}
