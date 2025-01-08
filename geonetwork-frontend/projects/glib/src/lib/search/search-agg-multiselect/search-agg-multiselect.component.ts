import { Component, input } from '@angular/core';
import { elasticsearch } from 'gapi';
import { MultiSelectChangeEvent, MultiSelectModule } from 'primeng/multiselect';
import {
  SearchFilterOperator,
  SearchFilterValueState,
} from '../search.state.model';
import { SearchAggItemDecoratorComponent } from '../search-agg-item-decorator/search-agg-item-decorator.component';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { DropdownModule } from 'primeng/dropdown';

@Component({
  selector: 'g-search-agg-multiselect',
  templateUrl: './search-agg-multiselect.component.html',
  standalone: true,
  imports: [MultiSelectModule, SearchAggItemDecoratorComponent, DropdownModule],
})
export class SearchAggMultiselectComponent extends SearchBaseComponent {
  buckets = input.required<any>();
  field = input.required<string>();
  placeholder = input<string>();
  aggregationConfig =
    input.required<elasticsearch.AggregationsAggregationContainer>();

  handleMultiSelectChange(event: MultiSelectChangeEvent) {
    const isSelected =
      event.itemValue &&
      event.value.find((item: any) => {
        return item.key === event.itemValue.key;
      }) !== undefined;

    const values: { [key: string]: SearchFilterValueState } = {};
    if (event.itemValue) {
      values[event.itemValue.key] = isSelected
        ? SearchFilterValueState.ON
        : SearchFilterValueState.OFF;
    } else if (event.value.length > 0) {
      event.value.forEach((item: any) => {
        values[item.key] = SearchFilterValueState.ON;
      });
    } else {
      // No items selected
      this.search.removeFilter(this.field());
      return;
    }

    this.search.addFilter({
      field: this.field(),
      values: values,
      operator: SearchFilterOperator.OR,
    });
  }

  handleMultiSelectClear() {
    this.search.removeFilter(this.field());
  }
}
