/* tslint:disable */
/* eslint-disable */
/**
 * GeoNetwork API
 * This API exposes endpoints to GeoNetwork API.
 *
 * The version of the OpenAPI document: 5.0.0
 * Contact: geonetwork-users@lists.sourceforge.net
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import * as runtime from '../runtime';
import type { ApiUserSigninPostRequest } from '../models/index';
import {
  ApiUserSigninPostRequestFromJSON,
  ApiUserSigninPostRequestToJSON,
} from '../models/index';

export interface ApiUserSigninPostOperationRequest {
  apiUserSigninPostRequest?: ApiUserSigninPostRequest;
}

/**
 *
 */
export class LoginEndpointApi extends runtime.BaseAPI {
  /**
   */
  async apiUserSigninPostRaw(
    requestParameters: ApiUserSigninPostOperationRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<void>> {
    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    headerParameters['Content-Type'] = 'application/json';

    const response = await this.request(
      {
        path: `/api/user/signin`,
        method: 'POST',
        headers: headerParameters,
        query: queryParameters,
        body: ApiUserSigninPostRequestToJSON(
          requestParameters['apiUserSigninPostRequest']
        ),
      },
      initOverrides
    );

    return new runtime.VoidApiResponse(response);
  }

  /**
   */
  async apiUserSigninPost(
    requestParameters: ApiUserSigninPostOperationRequest = {},
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<void> {
    await this.apiUserSigninPostRaw(requestParameters, initOverrides);
  }
}
