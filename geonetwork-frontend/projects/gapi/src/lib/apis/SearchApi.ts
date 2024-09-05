/* tslint:disable */
/* eslint-disable */
/**
 * GeoNetwork 4.4.6 OpenAPI Documentation
 * This is the description of the GeoNetwork OpenAPI. Use this API to manage your catalog.
 *
 * The version of the OpenAPI document: 4.4.6
 * Contact: geonetwork-users@lists.sourceforge.net
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import * as runtime from '../runtime';
import type { RelatedItemType } from '../models/index';
import {
  RelatedItemTypeFromJSON,
  RelatedItemTypeToJSON,
} from '../models/index';
import { estypes } from '@elastic/elasticsearch';
import { GnIndexRecord } from '../model-index';

export interface MsearchRequest {
  body: estypes.MsearchRequest;
  bucket?: string;
  relatedType?: Array<RelatedItemType>;
}

export interface SearchRequest {
  body: estypes.SearchRequest;
  bucket?: string;
  relatedType?: Array<RelatedItemType>;
}

/**
 *
 */
export class SearchApi extends runtime.BaseAPI {
  /**
   * The multi search API executes several searches from a single API request. See https://www.elastic.co/guide/en/elasticsearch/reference/current/search-multi-search.html for search parameters, and https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html Query DSL.
   * Executes several searches with a Elasticsearch API request.
   */
  async msearchRaw(
    requestParameters: MsearchRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<estypes.MsearchResponse>> {
    if (requestParameters['body'] == null) {
      throw new runtime.RequiredError(
        'body',
        'Required parameter "body" was null or undefined when calling msearch().'
      );
    }

    const queryParameters: any = {};

    if (requestParameters['bucket'] != null) {
      queryParameters['bucket'] = requestParameters['bucket'];
    }

    if (requestParameters['relatedType'] != null) {
      queryParameters['relatedType'] = requestParameters['relatedType'];
    }

    const headerParameters: runtime.HTTPHeaders = {};

    headerParameters['Content-Type'] = 'application/json';
    headerParameters['Accept'] = 'application/json';

    const response = await this.request(
      {
        path: `/search/records/_msearch`,
        method: 'POST',
        headers: headerParameters,
        query: queryParameters,
        body: requestParameters['body'] as any,
      },
      initOverrides
    );

    if (this.isJsonMime(response.headers.get('content-type'))) {
      return new runtime.JSONApiResponse<estypes.MsearchResponse>(response);
    } else {
      return new runtime.TextApiResponse(response) as any;
    }
  }

  /**
   * The multi search API executes several searches from a single API request. See https://www.elastic.co/guide/en/elasticsearch/reference/current/search-multi-search.html for search parameters, and https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html Query DSL.
   * Executes several searches with a Elasticsearch API request.
   */
  async msearch(
    requestParameters: MsearchRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<estypes.MsearchResponse> {
    const response = await this.msearchRaw(requestParameters, initOverrides);
    return await response.value();
  }

  /**
   * The search API execute a search query with a JSON request body. For more information see https://www.elastic.co/guide/en/elasticsearch/reference/current/search-search.html for search parameters, and https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html JSON Query DSL.
   * Execute a search query and get back search hits that match the query.
   */
  async searchRaw(
    requestParameters: SearchRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<estypes.SearchResponse<GnIndexRecord>>> {
    if (requestParameters['body'] == null) {
      throw new runtime.RequiredError(
        'body',
        'Required parameter "body" was null or undefined when calling search().'
      );
    }

    const queryParameters: any = {};

    if (requestParameters['bucket'] != null) {
      queryParameters['bucket'] = requestParameters['bucket'];
    }

    if (requestParameters['relatedType'] != null) {
      queryParameters['relatedType'] = requestParameters['relatedType'];
    }

    const headerParameters: runtime.HTTPHeaders = {};

    headerParameters['Content-Type'] = 'application/json';
    headerParameters['Accept'] = 'application/json';

    const response = await this.request(
      {
        path: `/search/records/_search`,
        method: 'POST',
        headers: headerParameters,
        query: queryParameters,
        body: requestParameters['body'] as any,
      },
      initOverrides
    );

    if (this.isJsonMime(response.headers.get('content-type'))) {
      return new runtime.JSONApiResponse<estypes.SearchResponse<GnIndexRecord>>(
        response
      );
    } else {
      return new runtime.TextApiResponse(response) as any;
    }
  }

  /**
   * The search API execute a search query with a JSON request body. For more information see https://www.elastic.co/guide/en/elasticsearch/reference/current/search-search.html for search parameters, and https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html JSON Query DSL.
   * Execute a search query and get back search hits that match the query.
   */
  async search(
    requestParameters: SearchRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<estypes.SearchResponse<GnIndexRecord>> {
    const response = await this.searchRaw(requestParameters, initOverrides);
    return await response.value();
  }
}
