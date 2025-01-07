import { Component, input } from '@angular/core';
import { SearchAggItemDecoratorComponent } from '../search-agg-item-decorator/search-agg-item-decorator.component';
import { SearchBaseComponent } from '../search-base/search-base.component';
import {
  SearchFilterOperator,
  SearchFilterValueState,
} from '../search.state.model';
import { elasticsearch } from 'gapi';
import { MultiSelectModule } from 'primeng/multiselect';
import { PrimeTemplate } from 'primeng/api';
import { DropdownChangeEvent, DropdownModule } from 'primeng/dropdown';
import { Select } from 'primeng/select';

@Component({
  selector: 'g-search-agg-dropdown',
  templateUrl: './search-agg-dropdown.component.html',
  imports: [
    MultiSelectModule,
    PrimeTemplate,
    SearchAggItemDecoratorComponent,
    DropdownModule,
    Select,
  ],
  standalone: true,
})
export class SearchAggDropdownComponent extends SearchBaseComponent {
  buckets = input.required<any>();
  field = input.required<string>();
  placeholder = input<string>();
  aggregationConfig =
    input.required<elasticsearch.AggregationsAggregationContainer>();

  handleChange(event: DropdownChangeEvent) {
    if (event.value === null) {
      this.search.removeFilter(this.field());
    } else {
      this.search.addFilter(
        {
          field: this.field(),
          values: {
            [event.value.key]: SearchFilterValueState.ON,
          },
          operator: SearchFilterOperator.OR,
        },
        true
      );
    }
  }
}