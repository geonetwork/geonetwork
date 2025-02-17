import { computed, inject, Injectable } from '@angular/core';
import { GnIndexRecord, SearchApi } from 'gapi';
import { SearchStoreType } from './search.state';
import { elasticsearch } from 'gapi';
import {
  SearchFilterList,
  SearchRequestParameters,
} from './search.state.model';
import { API5_CONFIGURATION, API_CONFIGURATION } from '../config/config.loader';
import { estypes } from '@elastic/elasticsearch';

export interface SearchRegistry {
  [searchId: string]: SearchStoreType;
}

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  store: SearchRegistry = {};

  apiConfiguration = inject(API_CONFIGURATION);
  api5Configuration = inject(API5_CONFIGURATION);

  searchApi = computed(() => {
    return new SearchApi(this.apiConfiguration());
  });

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
      console.log(`Search ${searchId} already registered. Reusing it.`);
      // throw new Error(
      //   `Search ${searchId} already registered. Choose another search id.`
      // );
    }
    this.store[searchId] = searchStore;
  }

  search(
    searchRequestParameters: SearchRequestParameters
  ): Promise<estypes.SearchResponse<GnIndexRecord>> {
    return this.searchApi().search({
      body: this.buildQuery(searchRequestParameters),
    });
  }

  page(
    searchRequestParameters: SearchRequestParameters
  ): Promise<estypes.SearchResponse<GnIndexRecord>> {
    return this.searchApi().search({
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
    const clauses: elasticsearch.QueryDslTermsQueryField =
      filters &&
      Object.entries(filters)
        .map(([field, { values }]) => {
          const terms = Object.entries(values)
            .filter(([, value]) => value === 'ON')
            .map(([key]) => key);
          return terms.length > 0 ? { terms: { [field]: terms } } : null;
        })
        .filter(clause => clause !== null);

    let baseQuery: elasticsearch.QueryDslQueryContainer = {
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
    };

    if (parameters.filter && parameters.filter != '' && baseQuery.bool) {
      baseQuery.bool.filter = [
        {
          query_string: {
            query: parameters.filter,
          },
        },
      ];
    }

    let finalQuery: elasticsearch.QueryDslQueryContainer = baseQuery;
    if (parameters.functionScore) {
      finalQuery = {
        function_score: {
          query: baseQuery,
          ...parameters.functionScore,
        },
      };
    }

    let query: elasticsearch.SearchRequest = {
      query: finalQuery,
      from: parameters.from,
      size: parameters.size,
      sort: parameters.sort,
    };

    if (parameters.aggregationConfig) {
      query.aggregations = parameters.aggregationConfig;
    }

    return query;
  }

  /**
   * Check if field is sortable or not, if sortable: return the field name to used, otherwise return undefined;
   */
  getSortableField(field: string) {
    const isNullOrIsExpression =
      field == null || field.includes('[') || field.includes('(');
    if (isNullOrIsExpression) {
      return undefined;
    } else {
      const isMultilingual = field.includes('Object.');
      if (isMultilingual) {
        return field + '.sort';
      }
    }
    return field;
  }
}
