import {
  Component,
  computed,
  inject,
  input,
  OnInit,
  output,
  signal,
} from '@angular/core';
import { CheckboxModule } from 'primeng/checkbox';
import { FormsModule } from '@angular/forms';
import { SearchService } from '../search.service';
import { SearchStoreType } from '../search.state';
import { SearchAggLayout } from '../search-agg/search-agg.component';
import { ChipModule } from 'primeng/chip';
import { NgClass, NgOptimizedImage } from '@angular/common';
import { AggregationsAggregationContainer } from '@elastic/elasticsearch/lib/api/types';
import {
  SearchFilterValue,
  SearchFilterValueState,
} from '../search.state.model';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'g-search-agg-item',
  standalone: true,
  imports: [
    CheckboxModule,
    FormsModule,
    ChipModule,
    NgClass,
    CardModule,
    NgOptimizedImage,
  ],
  templateUrl: './search-agg-item.component.html',
  styleUrl: './search-agg-item.component.css',
})
export class SearchAggItemComponent implements OnInit {
  scope = input.required<string>();
  bucket = input.required<any>();
  field = input.required<string>();
  layout = input.required<SearchAggLayout>();
  aggregationConfig = input.required<AggregationsAggregationContainer>();

  checked = signal(false);
  icon = computed(() => {
    const decorator = this.aggregationConfig().meta?.['decorator'];
    if (decorator && decorator.type === 'icon') {
      return decorator.prefix + decorator.map?.[this.bucket().key];
    }
    return '';
  });
  image = computed(() => {
    const decorator = this.aggregationConfig().meta?.['decorator'];
    if (decorator && decorator.type === 'img') {
      return decorator.map?.[this.bucket().key];
    }
    return '';
  });
  onAggItemChange = output<SearchFilterValue>();
  searchService = inject(SearchService);
  search: SearchStoreType;
  isActive = computed(
    () =>
      this.search.isFilterActive(this.field(), this.bucket().key) ===
      SearchFilterValueState.ON
  );
  protected readonly SearchAggLayout = SearchAggLayout;

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope());
    this.checked.set(
      this.search.isFilterActive(this.field(), this.bucket().key) ===
        SearchFilterValueState.ON
    );
  }

  handleCheck(event: any) {
    this.onAggItemChange.emit({
      [this.bucket().key]: this.checked()
        ? SearchFilterValueState.ON
        : SearchFilterValueState.OFF,
    });
  }

  switchValue() {
    this.onAggItemChange.emit({
      [this.bucket().key]: this.isActive()
        ? SearchFilterValueState.OFF
        : SearchFilterValueState.ON,
    });
  }
}
