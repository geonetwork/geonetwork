import { Component, input } from '@angular/core';
import { AggregationsAggregationContainer } from '@elastic/elasticsearch/lib/api/types';
import { MultiSelectChangeEvent, MultiSelectModule } from 'primeng/multiselect';
import {
  SearchFilterOperator,
  SearchFilterValueState,
} from '../search.state.model';
import { SearchAggItemDecoratorComponent } from '../search-agg-item-decorator/search-agg-item-decorator.component';
import { SearchBaseComponent } from '../search-base/search-base.component';

@Component({
  selector: 'g-search-agg-multiselect',
  templateUrl: './search-agg-multiselect.component.html',
  standalone: true,
  styleUrl: './search-agg-multiselect.component.css',
  imports: [MultiSelectModule, SearchAggItemDecoratorComponent],
})
export class SearchAggMultiselectComponent extends SearchBaseComponent {
  buckets = input.required<any>();
  field = input.required<string>();
  aggregationConfig = input.required<AggregationsAggregationContainer>();

  handleMutliSelectChange(event: MultiSelectChangeEvent) {
    const isSelected =
      event.value.find((item: any) => {
        return item.key === event.itemValue.key;
      }) !== undefined;

    this.search.addFilter({
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
    this.search.removeFilter(this.field());
  }
}
