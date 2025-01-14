/* tslint:disable */
/* eslint-disable */
/**
 * GeoNetwork 4.4.7 OpenAPI Documentation
 * This is the description of the GeoNetwork OpenAPI. Use this API to manage your catalog.
 *
 * The version of the OpenAPI document: 4.4.7
 * Contact: geonetwork-users@lists.sourceforge.net
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import * as runtime from '../runtime';
import type { AnonymousMapserver, MapServer } from '../models/index';
import {
  AnonymousMapserverFromJSON,
  AnonymousMapserverToJSON,
  MapServerFromJSON,
  MapServerToJSON,
} from '../models/index';

export interface AddMapserverRequest {
  mapServer: MapServer;
}

export interface DeleteMapserverRequest {
  mapserverId: number;
}

export interface DeleteMapserverResourceRequest {
  mapserverId: string;
  metadataUuid: string;
  resource: string;
  metadataTitle?: string;
  metadataAbstract?: string;
}

export interface GetMapserverRequest {
  mapserverId: string;
}

export interface GetMapserverResourceRequest {
  mapserverId: string;
  metadataUuid: string;
  resource: string;
  metadataTitle?: string;
  metadataAbstract?: string;
}

export interface PublishMapserverResourceRequest {
  mapserverId: string;
  metadataUuid: string;
  resource: string;
  metadataTitle?: string;
  metadataAbstract?: string;
}

export interface UpdateMapserverRequest {
  mapserverId: number;
  mapServer: MapServer;
}

export interface UpdateMapserverAuthRequest {
  mapserverId: number;
  username: string;
  password: string;
}

/**
 *
 */
export class MapserversApi extends runtime.BaseAPI {
  /**
   * Return the id of the newly created mapserver.
   * Add a mapserver
   */
  async addMapserverRaw(
    requestParameters: AddMapserverRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<number>> {
    if (requestParameters['mapServer'] == null) {
      throw new runtime.RequiredError(
        'mapServer',
        'Required parameter "mapServer" was null or undefined when calling addMapserver().'
      );
    }

    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    headerParameters['Content-Type'] = 'application/json';

    const response = await this.request(
      {
        path: `/mapservers`,
        method: 'PUT',
        headers: headerParameters,
        query: queryParameters,
        body: MapServerToJSON(requestParameters['mapServer']),
      },
      initOverrides
    );

    if (this.isJsonMime(response.headers.get('content-type'))) {
      return new runtime.JSONApiResponse<number>(response);
    } else {
      return new runtime.TextApiResponse(response) as any;
    }
  }

  /**
   * Return the id of the newly created mapserver.
   * Add a mapserver
   */
  async addMapserver(
    requestParameters: AddMapserverRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<number> {
    const response = await this.addMapserverRaw(
      requestParameters,
      initOverrides
    );
    return await response.value();
  }

  /**
   * Remove a mapserver
   */
  async deleteMapserverRaw(
    requestParameters: DeleteMapserverRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<void>> {
    if (requestParameters['mapserverId'] == null) {
      throw new runtime.RequiredError(
        'mapserverId',
        'Required parameter "mapserverId" was null or undefined when calling deleteMapserver().'
      );
    }

    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    const response = await this.request(
      {
        path: `/mapservers/{mapserverId}`.replace(
          `{${'mapserverId'}}`,
          encodeURIComponent(String(requestParameters['mapserverId']))
        ),
        method: 'DELETE',
        headers: headerParameters,
        query: queryParameters,
      },
      initOverrides
    );

    return new runtime.VoidApiResponse(response);
  }

  /**
   * Remove a mapserver
   */
  async deleteMapserver(
    requestParameters: DeleteMapserverRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<void> {
    await this.deleteMapserverRaw(requestParameters, initOverrides);
  }

  /**
   * Remove a metadata mapserver resource
   */
  async deleteMapserverResourceRaw(
    requestParameters: DeleteMapserverResourceRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<void>> {
    if (requestParameters['mapserverId'] == null) {
      throw new runtime.RequiredError(
        'mapserverId',
        'Required parameter "mapserverId" was null or undefined when calling deleteMapserverResource().'
      );
    }

    if (requestParameters['metadataUuid'] == null) {
      throw new runtime.RequiredError(
        'metadataUuid',
        'Required parameter "metadataUuid" was null or undefined when calling deleteMapserverResource().'
      );
    }

    if (requestParameters['resource'] == null) {
      throw new runtime.RequiredError(
        'resource',
        'Required parameter "resource" was null or undefined when calling deleteMapserverResource().'
      );
    }

    const queryParameters: any = {};

    if (requestParameters['resource'] != null) {
      queryParameters['resource'] = requestParameters['resource'];
    }

    if (requestParameters['metadataTitle'] != null) {
      queryParameters['metadataTitle'] = requestParameters['metadataTitle'];
    }

    if (requestParameters['metadataAbstract'] != null) {
      queryParameters['metadataAbstract'] =
        requestParameters['metadataAbstract'];
    }

    const headerParameters: runtime.HTTPHeaders = {};

    const response = await this.request(
      {
        path: `/mapservers/{mapserverId}/records/{metadataUuid}`
          .replace(
            `{${'mapserverId'}}`,
            encodeURIComponent(String(requestParameters['mapserverId']))
          )
          .replace(
            `{${'metadataUuid'}}`,
            encodeURIComponent(String(requestParameters['metadataUuid']))
          ),
        method: 'DELETE',
        headers: headerParameters,
        query: queryParameters,
      },
      initOverrides
    );

    return new runtime.VoidApiResponse(response);
  }

  /**
   * Remove a metadata mapserver resource
   */
  async deleteMapserverResource(
    requestParameters: DeleteMapserverResourceRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<void> {
    await this.deleteMapserverResourceRaw(requestParameters, initOverrides);
  }

  /**
   * Get a mapserver
   */
  async getMapserverRaw(
    requestParameters: GetMapserverRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<void>> {
    if (requestParameters['mapserverId'] == null) {
      throw new runtime.RequiredError(
        'mapserverId',
        'Required parameter "mapserverId" was null or undefined when calling getMapserver().'
      );
    }

    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    const response = await this.request(
      {
        path: `/mapservers/{mapserverId}`.replace(
          `{${'mapserverId'}}`,
          encodeURIComponent(String(requestParameters['mapserverId']))
        ),
        method: 'GET',
        headers: headerParameters,
        query: queryParameters,
      },
      initOverrides
    );

    return new runtime.VoidApiResponse(response);
  }

  /**
   * Get a mapserver
   */
  async getMapserver(
    requestParameters: GetMapserverRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<void> {
    await this.getMapserverRaw(requestParameters, initOverrides);
  }

  /**
   * Check metadata mapserver resource is published
   */
  async getMapserverResourceRaw(
    requestParameters: GetMapserverResourceRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<void>> {
    if (requestParameters['mapserverId'] == null) {
      throw new runtime.RequiredError(
        'mapserverId',
        'Required parameter "mapserverId" was null or undefined when calling getMapserverResource().'
      );
    }

    if (requestParameters['metadataUuid'] == null) {
      throw new runtime.RequiredError(
        'metadataUuid',
        'Required parameter "metadataUuid" was null or undefined when calling getMapserverResource().'
      );
    }

    if (requestParameters['resource'] == null) {
      throw new runtime.RequiredError(
        'resource',
        'Required parameter "resource" was null or undefined when calling getMapserverResource().'
      );
    }

    const queryParameters: any = {};

    if (requestParameters['resource'] != null) {
      queryParameters['resource'] = requestParameters['resource'];
    }

    if (requestParameters['metadataTitle'] != null) {
      queryParameters['metadataTitle'] = requestParameters['metadataTitle'];
    }

    if (requestParameters['metadataAbstract'] != null) {
      queryParameters['metadataAbstract'] =
        requestParameters['metadataAbstract'];
    }

    const headerParameters: runtime.HTTPHeaders = {};

    const response = await this.request(
      {
        path: `/mapservers/{mapserverId}/records/{metadataUuid}`
          .replace(
            `{${'mapserverId'}}`,
            encodeURIComponent(String(requestParameters['mapserverId']))
          )
          .replace(
            `{${'metadataUuid'}}`,
            encodeURIComponent(String(requestParameters['metadataUuid']))
          ),
        method: 'GET',
        headers: headerParameters,
        query: queryParameters,
      },
      initOverrides
    );

    return new runtime.VoidApiResponse(response);
  }

  /**
   * Check metadata mapserver resource is published
   */
  async getMapserverResource(
    requestParameters: GetMapserverResourceRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<void> {
    await this.getMapserverResourceRaw(requestParameters, initOverrides);
  }

  /**
   * Mapservers are used by the catalog to publish record attachments (eg. ZIP file with shape) or record associated resources (eg. database table, file on the local network) in a remote mapserver like GeoServer or MapServer. The catalog communicate with the mapserver using GeoServer REST API.
   * Get mapservers
   */
  async getMapserversRaw(
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<Array<AnonymousMapserver>>> {
    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    const response = await this.request(
      {
        path: `/mapservers`,
        method: 'GET',
        headers: headerParameters,
        query: queryParameters,
      },
      initOverrides
    );

    return new runtime.JSONApiResponse(response, jsonValue =>
      jsonValue.map(AnonymousMapserverFromJSON)
    );
  }

  /**
   * Mapservers are used by the catalog to publish record attachments (eg. ZIP file with shape) or record associated resources (eg. database table, file on the local network) in a remote mapserver like GeoServer or MapServer. The catalog communicate with the mapserver using GeoServer REST API.
   * Get mapservers
   */
  async getMapservers(
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<Array<AnonymousMapserver>> {
    const response = await this.getMapserversRaw(initOverrides);
    return await response.value();
  }

  /**
   * Publish a metadata resource in a mapserver
   */
  async publishMapserverResourceRaw(
    requestParameters: PublishMapserverResourceRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<void>> {
    if (requestParameters['mapserverId'] == null) {
      throw new runtime.RequiredError(
        'mapserverId',
        'Required parameter "mapserverId" was null or undefined when calling publishMapserverResource().'
      );
    }

    if (requestParameters['metadataUuid'] == null) {
      throw new runtime.RequiredError(
        'metadataUuid',
        'Required parameter "metadataUuid" was null or undefined when calling publishMapserverResource().'
      );
    }

    if (requestParameters['resource'] == null) {
      throw new runtime.RequiredError(
        'resource',
        'Required parameter "resource" was null or undefined when calling publishMapserverResource().'
      );
    }

    const queryParameters: any = {};

    if (requestParameters['resource'] != null) {
      queryParameters['resource'] = requestParameters['resource'];
    }

    if (requestParameters['metadataTitle'] != null) {
      queryParameters['metadataTitle'] = requestParameters['metadataTitle'];
    }

    if (requestParameters['metadataAbstract'] != null) {
      queryParameters['metadataAbstract'] =
        requestParameters['metadataAbstract'];
    }

    const headerParameters: runtime.HTTPHeaders = {};

    const response = await this.request(
      {
        path: `/mapservers/{mapserverId}/records/{metadataUuid}`
          .replace(
            `{${'mapserverId'}}`,
            encodeURIComponent(String(requestParameters['mapserverId']))
          )
          .replace(
            `{${'metadataUuid'}}`,
            encodeURIComponent(String(requestParameters['metadataUuid']))
          ),
        method: 'PUT',
        headers: headerParameters,
        query: queryParameters,
      },
      initOverrides
    );

    return new runtime.VoidApiResponse(response);
  }

  /**
   * Publish a metadata resource in a mapserver
   */
  async publishMapserverResource(
    requestParameters: PublishMapserverResourceRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<void> {
    await this.publishMapserverResourceRaw(requestParameters, initOverrides);
  }

  /**
   * Update a mapserver
   */
  async updateMapserverRaw(
    requestParameters: UpdateMapserverRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<void>> {
    if (requestParameters['mapserverId'] == null) {
      throw new runtime.RequiredError(
        'mapserverId',
        'Required parameter "mapserverId" was null or undefined when calling updateMapserver().'
      );
    }

    if (requestParameters['mapServer'] == null) {
      throw new runtime.RequiredError(
        'mapServer',
        'Required parameter "mapServer" was null or undefined when calling updateMapserver().'
      );
    }

    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    headerParameters['Content-Type'] = 'application/json';

    const response = await this.request(
      {
        path: `/mapservers/{mapserverId}`.replace(
          `{${'mapserverId'}}`,
          encodeURIComponent(String(requestParameters['mapserverId']))
        ),
        method: 'PUT',
        headers: headerParameters,
        query: queryParameters,
        body: MapServerToJSON(requestParameters['mapServer']),
      },
      initOverrides
    );

    return new runtime.VoidApiResponse(response);
  }

  /**
   * Update a mapserver
   */
  async updateMapserver(
    requestParameters: UpdateMapserverRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<void> {
    await this.updateMapserverRaw(requestParameters, initOverrides);
  }

  /**
   * The remote mapserver REST API may require basic authentication. This operation set the username and password.
   * Update a mapserver authentication
   */
  async updateMapserverAuthRaw(
    requestParameters: UpdateMapserverAuthRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<runtime.ApiResponse<void>> {
    if (requestParameters['mapserverId'] == null) {
      throw new runtime.RequiredError(
        'mapserverId',
        'Required parameter "mapserverId" was null or undefined when calling updateMapserverAuth().'
      );
    }

    if (requestParameters['username'] == null) {
      throw new runtime.RequiredError(
        'username',
        'Required parameter "username" was null or undefined when calling updateMapserverAuth().'
      );
    }

    if (requestParameters['password'] == null) {
      throw new runtime.RequiredError(
        'password',
        'Required parameter "password" was null or undefined when calling updateMapserverAuth().'
      );
    }

    const queryParameters: any = {};

    if (requestParameters['username'] != null) {
      queryParameters['username'] = requestParameters['username'];
    }

    if (requestParameters['password'] != null) {
      queryParameters['password'] = requestParameters['password'];
    }

    const headerParameters: runtime.HTTPHeaders = {};

    const response = await this.request(
      {
        path: `/mapservers/{mapserverId}/auth`.replace(
          `{${'mapserverId'}}`,
          encodeURIComponent(String(requestParameters['mapserverId']))
        ),
        method: 'POST',
        headers: headerParameters,
        query: queryParameters,
      },
      initOverrides
    );

    return new runtime.VoidApiResponse(response);
  }

  /**
   * The remote mapserver REST API may require basic authentication. This operation set the username and password.
   * Update a mapserver authentication
   */
  async updateMapserverAuth(
    requestParameters: UpdateMapserverAuthRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction
  ): Promise<void> {
    await this.updateMapserverAuthRaw(requestParameters, initOverrides);
  }
}
