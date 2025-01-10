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
 * @interface GnResourceIdentifier
 */
export interface GnResourceIdentifier {
  /**
   *
   * @type {string}
   * @memberof GnResourceIdentifier
   */
  code?: string;
  /**
   *
   * @type {string}
   * @memberof GnResourceIdentifier
   */
  codeSpace?: string;
  /**
   *
   * @type {string}
   * @memberof GnResourceIdentifier
   */
  link?: string;
}

/**
 * Check if a given object implements the GnResourceIdentifier interface.
 */
export function instanceOfGnResourceIdentifier(
  value: object
): value is GnResourceIdentifier {
  return true;
}

export function GnResourceIdentifierFromJSON(json: any): GnResourceIdentifier {
  return GnResourceIdentifierFromJSONTyped(json, false);
}

export function GnResourceIdentifierFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): GnResourceIdentifier {
  if (json == null) {
    return json;
  }
  return {
    code: json['code'] == null ? undefined : json['code'],
    codeSpace: json['codeSpace'] == null ? undefined : json['codeSpace'],
    link: json['link'] == null ? undefined : json['link'],
  };
}

export function GnResourceIdentifierToJSON(
  value?: GnResourceIdentifier | null
): any {
  if (value == null) {
    return value;
  }
  return {
    code: value['code'],
    codeSpace: value['codeSpace'],
    link: value['link'],
  };
}
