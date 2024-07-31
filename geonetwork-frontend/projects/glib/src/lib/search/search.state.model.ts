import {
  AggregateName,
  AggregationsAggregate,
  AggregationsAggregationContainer,
  QueryDslFunctionScoreQuery,
  SearchResponse,
  Sort,
} from '@elastic/elasticsearch/lib/api/types';

export enum SearchFilterValueState {
  ON = 'ON',
  OFF = 'OFF',
  NOT = 'NOT',
}

export enum SearchFilterOperator {
  AND = 'AND',
  OR = 'OR',
}

export enum SearchAggRefreshPolicy {
  ON_SEARCH = 'ON_SEARCH',
  NO_REFRESH = 'NO_REFRESH',
}

export interface SearchFilterValue {
  [field: string]: SearchFilterValueState;
}

export interface SearchFilter {
  field: string;
  values: SearchFilterValue;
  operator: SearchFilterOperator;
}

export interface SearchFilterList {
  [field: string]: SearchFilter;
}

export interface Search {
  id: string;
  aggregationConfig: Record<string, AggregationsAggregationContainer>;
  functionScore: QueryDslFunctionScoreQuery | null;
  isSearching?: boolean;
  isReset?: boolean;
  fullTextQuery: string;
  from: number;
  size: number;
  pageSize: number;
  sort: Sort;
  trackTotalHits?: boolean;
  filters: SearchFilterList;
  response: SearchResponse | null;
  aggregation: Record<AggregateName, AggregationsAggregate>;
  error: Error | null;
}

export interface SearchFilterParameters {
  fullTextQuery: string;
  filters: SearchFilterList;
  aggregationConfig: Record<string, AggregationsAggregationContainer>;
  functionScore: QueryDslFunctionScoreQuery | null;
  sort: Sort;
}

export interface SearchRequestPageParameters {
  from: number;
  size: number;
}

export interface SearchRequestParameters
  extends SearchFilterParameters,
    SearchRequestPageParameters {}
