import { inject, Injectable } from '@angular/core';
import { Params, Router } from '@angular/router';
import { Location } from '@angular/common';
import {
  SearchFilter,
  SearchFilterList,
  SearchFilterOperator,
  SearchFilterValue,
  SearchFilterValueState,
  SearchRequestParameters,
} from './search.state.model';

@Injectable({
  providedIn: 'root',
})
export class SearchRouteService {
  router = inject(Router);
  location = inject(Location);

  setRoute(store: SearchRequestParameters, pageSize: number) {
    let urlParams = [];
    if (store.fullTextQuery) {
      urlParams.push(`q=${store.fullTextQuery}`);
    }
    if (store.filters) {
      urlParams = urlParams.concat(
        Object.entries(store.filters)
          .filter(([field, filter]) => Object.keys(filter.values).length > 0)
          .map(
            ([field, filter]) =>
              `${field}="${Object.keys(filter.values).join('" OR "')}"`
          )
      );
    }
    if (store.from !== 0) {
      urlParams.push(`from=${store.from}`);
    }
    if (store.size !== pageSize) {
      urlParams.push(`size=${store.size}`);
    }
    this.location.go('/search', urlParams.filter(v => v !== '').join('&'));
  }

  convertRouteParamsToSearch(params: Params, pageSize: number): any {
    const filter: SearchFilterList = {};
    const nonFilterParams = ['from', 'size', 'q'];

    Object.entries(params).forEach(([key, value]) => {
      if (!nonFilterParams.includes(key)) {
        const values = value.slice(1, -1).split('" OR "');
        const listOfFilter: SearchFilterValue = {};
        values.forEach((val: string) => {
          listOfFilter[val] = SearchFilterValueState.ON;
        });
        filter[key] = {
          field: key,
          values: listOfFilter,
          operator: SearchFilterOperator.OR,
        };
      }
    });

    return {
      from: parseInt(params['from']) || 0,
      size: parseInt(params['size']) || pageSize,
      fullTextQuery: params['q'] || '',
      filters: filter,
    };
  }

  buildFilterQueryParams(filter: SearchFilter) {
    return { [filter.field]: `"${Object.keys(filter.values).join('" OR "')}"` };
  }
}
