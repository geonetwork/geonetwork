import { Component, input } from '@angular/core';
import { elasticsearch } from 'gapi';
import {
  SearchFilterOperator,
  SearchFilterValueState,
} from '../search.state.model';
import { SearchAggItemDecoratorComponent } from '../search-agg-item-decorator/search-agg-item-decorator.component';
import { SearchBaseComponent } from '../search-base/search-base.component';
import {
  SelectButtonModule,
  SelectButtonOptionClickEvent,
} from 'primeng/selectbutton';

@Component({
  selector: 'g-search-agg-select-button',
  templateUrl: './search-agg-select-button.component.html',
  standalone: true,
  imports: [SearchAggItemDecoratorComponent, SelectButtonModule],
})
export class SearchAggSelectButtonComponent extends SearchBaseComponent {
  buckets = input.required<any>();
  field = input.required<string>();
  multiple = input(true);
  aggregationConfig =
    input.required<elasticsearch.AggregationsAggregationContainer>();

  handleChange(event: SelectButtonOptionClickEvent) {
    const isSelected =
      this.search.isFilterActive(this.field(), event.option.key) ===
      SearchFilterValueState.ON;
    this.search.addFilter(
      {
        field: this.field(),
        values: {
          [event.option.key]: isSelected
            ? SearchFilterValueState.OFF
            : SearchFilterValueState.ON,
        },
        operator: SearchFilterOperator.OR,
      },
      !this.multiple()
    );
  }

  protected readonly SearchFilterValueState = SearchFilterValueState;
}