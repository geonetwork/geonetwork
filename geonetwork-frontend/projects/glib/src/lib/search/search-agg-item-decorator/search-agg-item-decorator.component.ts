import { Component, computed, input } from '@angular/core';
import { elasticsearch } from 'gapi';

@Component({
  selector: 'g-search-agg-item-decorator',
  templateUrl: './search-agg-item-decorator.component.html',
  standalone: true,
})
export class SearchAggItemDecoratorComponent {
  bucket = input.required<any>();
  isActive = input<boolean>(false);
  aggregationConfig =
    input.required<elasticsearch.AggregationsAggregationContainer>();

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
}