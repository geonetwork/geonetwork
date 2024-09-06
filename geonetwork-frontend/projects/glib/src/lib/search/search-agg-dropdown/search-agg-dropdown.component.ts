import { Component, input } from '@angular/core';
import { SearchAggItemDecoratorComponent } from '../search-agg-item-decorator/search-agg-item-decorator.component';
import { SearchBaseComponent } from '../search-base/search-base.component';
import {
  SearchFilterOperator,
  SearchFilterValueState,
} from '../search.state.model';
import { AggregationsAggregationContainer } from '@elastic/elasticsearch/lib/api/types';
import { MultiSelectModule } from 'primeng/multiselect';
import { PrimeTemplate } from 'primeng/api';
import { DropdownChangeEvent, DropdownModule } from 'primeng/dropdown';

@Component({
  selector: 'g-search-agg-dropdown',
  templateUrl: './search-agg-dropdown.component.html',
  styleUrl: './search-agg-dropdown.component.css',
  imports: [
    MultiSelectModule,
    PrimeTemplate,
    SearchAggItemDecoratorComponent,
    DropdownModule,
  ],
  standalone: true,
})
export class SearchAggDropdownComponent extends SearchBaseComponent {
  buckets = input.required<any>();
  field = input.required<string>();
  placeholder = input<string>();
  aggregationConfig = input.required<AggregationsAggregationContainer>();

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
