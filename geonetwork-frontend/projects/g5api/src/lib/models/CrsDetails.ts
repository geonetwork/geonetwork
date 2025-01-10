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

import { mapValues } from '../runtime';
/**
 *
 * @export
 * @interface CrsDetails
 */
export interface CrsDetails {
  /**
   *
   * @type {string}
   * @memberof CrsDetails
   */
  code?: string;
  /**
   *
   * @type {string}
   * @memberof CrsDetails
   */
  codeSpace?: string;
  /**
   *
   * @type {string}
   * @memberof CrsDetails
   */
  name?: string;
  /**
   *
   * @type {string}
   * @memberof CrsDetails
   */
  url?: string;
}

/**
 * Check if a given object implements the CrsDetails interface.
 */
export function instanceOfCrsDetails(value: object): value is CrsDetails {
  return true;
}

export function CrsDetailsFromJSON(json: any): CrsDetails {
  return CrsDetailsFromJSONTyped(json, false);
}

export function CrsDetailsFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): CrsDetails {
  if (json == null) {
    return json;
  }
  return {
    code: json['code'] == null ? undefined : json['code'],
    codeSpace: json['codeSpace'] == null ? undefined : json['codeSpace'],
    name: json['name'] == null ? undefined : json['name'],
    url: json['url'] == null ? undefined : json['url'],
  };
}

export function CrsDetailsToJSON(value?: CrsDetails | null): any {
  if (value == null) {
    return value;
  }
  return {
    code: value['code'],
    codeSpace: value['codeSpace'],
    name: value['name'],
    url: value['url'],
  };
}
