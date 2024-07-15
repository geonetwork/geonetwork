import { Component, inject, input, OnInit } from '@angular/core';
import { AggregationsAggregationContainer } from '@elastic/elasticsearch/lib/api/types';
import { SearchService } from '../search.service';
import { SearchStoreType } from '../search.state';
import {MultiSelectChangeEvent, MultiSelectModule} from 'primeng/multiselect';
import {
  SearchFilterOperator,
  SearchFilterValueState,
} from '../search.state.model';
import {SearchAggItemDecoratorComponent} from "../search-agg-item-decorator/search-agg-item-decorator.component";

@Component({
  selector: 'g-search-agg-multiselect',
  templateUrl: './search-agg-multiselect.component.html',
  standalone: true,
  styleUrl: './search-agg-multiselect.component.css',
  imports: [MultiSelectModule, SearchAggItemDecoratorComponent],
})
export class SearchAggMultiselectComponent implements OnInit {
  scope = input.required<string>();
  buckets = input.required<any>();
  field = input.required<string>();
  aggregationConfig = input.required<AggregationsAggregationContainer>();

  #searchService = inject(SearchService);
  #search: SearchStoreType;

  ngOnInit() {
    this.#search = this.#searchService.getSearch(this.scope());
  }

  handleMutliSelectChange(event: MultiSelectChangeEvent) {
    const isSelected =
      event.value.find((item: any) => {
        return item.key === event.itemValue.key;
      }) !== undefined;

    this.#search.addFilter({
      field: this.field(),
      values: {
        [event.itemValue.key]: isSelected
          ? SearchFilterValueState.ON
          : SearchFilterValueState.OFF,
      },
      operator: SearchFilterOperator.OR,
    });
  }

  handleMutliSelectClear() {
    this.#search.removeFilter(this.field());
  }
}
