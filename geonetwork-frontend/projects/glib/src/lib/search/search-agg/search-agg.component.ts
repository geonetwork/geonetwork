import {
  Component,
  computed,
  HostAttributeToken,
  inject,
  input,
  OnInit,
} from '@angular/core';
import { SearchService } from '../search.service';
import { AsyncPipe, JsonPipe, NgClass } from '@angular/common';
import { AggregationsAggregationContainer } from '@elastic/elasticsearch/lib/api/types';
import { CheckboxModule } from 'primeng/checkbox';
import { FormsModule } from '@angular/forms';
import { TriStateCheckboxModule } from 'primeng/tristatecheckbox';
import { SearchAggItemComponent } from '../search-agg-item/search-agg-item.component';
import { SearchStoreType } from '../search.state';
import {
  SearchFilterOperator,
  SearchFilterValue,
  SearchFilterValueState,
} from '../search.state.model';

export enum SearchAggLayout {
  CHECKBOX = 'checkbox',
  TRISTATE = 'tristate',
  CHIP = 'chip',
  CARD = 'card',
}

type SearchAggLayoutValues = keyof typeof SearchAggLayout;

@Component({
  selector: 'g-search-agg',
  standalone: true,
  imports: [
    JsonPipe,
    AsyncPipe,
    CheckboxModule,
    FormsModule,
    TriStateCheckboxModule,
    SearchAggItemComponent,
    NgClass,
  ],
  templateUrl: './search-agg.component.html',
  styleUrl: './search-agg.component.css',
})
export class SearchAggComponent implements OnInit {
  scope = inject(new HostAttributeToken('scope'));
  searchService = inject(SearchService);
  search: SearchStoreType;

  aggregation = input.required<string>();
  layout = input<SearchAggLayout>();

  cssClass = computed(() => {
    switch (this.layout()) {
      case SearchAggLayout.CHIP:
        return 'card flex flex-wrap items-center';
      default:
        return 'mb-6';
    }
  });
  searchField: string = '';
  aggregationConfig: AggregationsAggregationContainer;
  currentLayout = computed(() => {
    return (
      this.layout() ||
      this.aggregationConfig.meta?.layout ||
      SearchAggLayout.CHECKBOX
    );
  });
  agg = computed(() => {
    const aggregationResponse = this.search.aggregation()?.[this.aggregation()];
    if (aggregationResponse === undefined) {
      console.warn(
        `${this.aggregation()} not found in search aggregations. Add this field to the search aggregation configuration for context ${this.search.id()}.`
      );
    }
    return aggregationResponse;
  });

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope);
    this.aggregationConfig = this.search.getAggregationConfig(
      this.aggregation()
    );
    this.searchField = this.search.getAggregationSearchField(
      this.aggregation()
    );
  }

  filter(filterValue: SearchFilterValue) {
    const value = Object.keys(filterValue)[0];
    const state = filterValue[value];
    // TODO: NOT and tri-state checkbox?
    if (state == SearchFilterValueState.OFF || state === null)
      this.search.removeFilter(this.searchField, value);
    else
      this.search.addFilter({
        field: this.searchField,
        values: {
          [value]: state,
        },
        operator: SearchFilterOperator.OR,
      });
  }
}
