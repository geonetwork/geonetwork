import { elasticsearch } from 'gapi';
import { IndexRecord } from 'g5api';

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
  aggregationConfig: Record<
    string,
    elasticsearch.AggregationsAggregationContainer
  >;
  functionScore: elasticsearch.QueryDslFunctionScoreQuery | null;
  isSearching?: boolean;
  isReset?: boolean;
  fullTextQuery: string;
  from: number;
  size: number;
  pageSize: number;
  sort: elasticsearch.Sort;
  trackTotalHits?: boolean;
  filters: SearchFilterList;
  filter: string;
  response: elasticsearch.SearchResponse<IndexRecord> | null;
  aggregation: Record<
    elasticsearch.AggregateName,
    elasticsearch.AggregationsAggregate
  >;
  error: Error | null;
  routing: boolean;
}

export interface SearchFilterParameters {
  fullTextQuery: string;
  filters: SearchFilterList;
  aggregationConfig: Record<
    string,
    elasticsearch.AggregationsAggregationContainer
  >;
  functionScore: elasticsearch.QueryDslFunctionScoreQuery | null;
  sort: elasticsearch.Sort;
  filter: string;
}

export interface SearchRequestPageParameters {
  from: number;
  size: number;
}

export interface SearchRequestParameters
  extends SearchFilterParameters,
    SearchRequestPageParameters {}
