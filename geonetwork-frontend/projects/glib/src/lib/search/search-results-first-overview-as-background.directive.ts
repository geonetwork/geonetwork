import { computed, Directive, inject, input, OnInit } from '@angular/core';
import { SearchService } from './search.service';
import { SearchStoreType } from './search.state';
import { elasticsearch } from 'gapi';
import { IndexRecord, Overview } from 'g5api';

@Directive({
  selector: '[gSearchResultsFirstOverviewAsBackground]',
  standalone: true,
  host: { '[style.background-image]': 'backgroundImageUrl()' },
})
export class SearchResultsFirstOverviewAsBackgroundDirective implements OnInit {
  scope = input<string>('', {
    alias: 'gSearchResultsFirstOverviewAsBackground',
  });
  searchService = inject(SearchService);
  search: SearchStoreType;

  backgroundImageUrl = computed(() => {
    const overviewList =
      this.search
        .response()
        ?.hits.hits.map((h: elasticsearch.SearchHit<IndexRecord>) => {
          return h._source?.overview;
        })
        .reduce((acc: Overview[], value: Overview[] | undefined) => {
          return value ? acc.concat(value) : acc;
        }, []) || [];

    return overviewList.length > 1
      ? `url(${overviewList[overviewList.length - 1].url})`
      : '';
  });

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope());
  }
}
