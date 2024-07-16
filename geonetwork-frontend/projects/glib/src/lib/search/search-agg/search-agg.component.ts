import { Component, computed, input } from '@angular/core';
import { AsyncPipe, JsonPipe, NgClass } from '@angular/common';
import { AggregationsAggregationContainer } from '@elastic/elasticsearch/lib/api/types';
import { CheckboxModule } from 'primeng/checkbox';
import { FormsModule } from '@angular/forms';
import { TriStateCheckboxModule } from 'primeng/tristatecheckbox';
import { SearchAggItemComponent } from '../search-agg-item/search-agg-item.component';
import {
  SearchFilterOperator,
  SearchFilterValue,
  SearchFilterValueState,
} from '../search.state.model';
import { ChipModule } from 'primeng/chip';
import { SearchModule } from '../search.module';
import { SearchAggMultiselectComponent } from '../search-agg-multiselect/search-agg-multiselect.component';
import { SearchBaseComponent } from '../search-base/search-base.component';

export enum SearchAggLayout {
  CHECKBOX = 'checkbox',
  TRISTATE = 'tristate',
  CHIP = 'chip',
  CARD = 'card',
  MULTISELECT = 'multiselect',
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
    ChipModule,
    SearchModule,
    SearchModule,
    SearchAggMultiselectComponent,
  ],
  templateUrl: './search-agg.component.html',
  styleUrl: './search-agg.component.css',
})
export class SearchAggComponent extends SearchBaseComponent {
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
  protected readonly SearchAggLayout = SearchAggLayout;

  override ngOnInit() {
    super.ngOnInit();
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
      this.search.removeFilterValue(this.searchField, value);
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
