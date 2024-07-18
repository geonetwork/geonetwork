import { Injectable } from '@angular/core';
import { SearchApi } from 'gapi';
import { SearchStoreType } from './search.state';
import {
  QueryDslTermsQueryField,
  SearchRequest,
} from '@elastic/elasticsearch/lib/api/types';
import {
  SearchFilterList,
  SearchRequestParameters,
} from './search.state.model';

export interface SearchRegistry {
  [searchId: string]: SearchStoreType;
}

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  store: SearchRegistry = {};
  searchApi: SearchApi = new SearchApi();

  getSearch(searchId: string): SearchStoreType {
    if (this.store[searchId]) {
      return this.store[searchId];
    } else {
      throw new Error(
        `Search ${searchId} not found. Available search contexts are: ${Object.keys(this.store).join(', ')}`
      );
    }
  }

  register(searchId: string, searchStore: SearchStoreType) {
    if (this.store[searchId]) {
      throw new Error(
        `Search ${searchId} already registered. Choose another search id.`
      );
    }
    this.store[searchId] = searchStore;
  }

  search(searchRequestParameters: SearchRequestParameters) {
    console.log('searchRequestParameters', searchRequestParameters);
    return this.searchApi.search({
      body: this.buildQuery(searchRequestParameters),
    });
  }

  page(searchRequestParameters: SearchRequestParameters) {
    return this.searchApi.search({
      body: this.buildPageQuery(searchRequestParameters),
    });
  }

  buildPageQuery(parameters: SearchRequestParameters) {
    let query = this.buildQuery(parameters);
    delete query.aggregations;
    return query;
  }

  buildQuery(parameters: SearchRequestParameters) {
    let filters: SearchFilterList = parameters.filters;
    const clauses: QueryDslTermsQueryField =
      filters &&
      Object.entries(filters)
        .map(([field, { values }]) => {
          const terms = Object.entries(values)
            .filter(([, value]) => value === 'ON')
            .map(([key]) => key);
          return terms.length > 0 ? { terms: { [field]: terms } } : null;
        })
        .filter(clause => clause !== null);

    let query: SearchRequest = {
      query: {
        bool: {
          must: [
            {
              query_string: {
                query: parameters.fullTextQuery || '*',
              },
            },
            ...((clauses && Object.values(clauses)) || []),
          ],
        },
      },
      from: parameters.from,
      size: parameters.size,
    };

    if (parameters.aggregationConfig) {
      query.aggregations = parameters.aggregationConfig;
    }
    return query;
  }
}