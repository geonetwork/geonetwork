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
 * @interface GnCodeListValue
 */
export interface GnCodeListValue {
  /**
   *
   * @type {string}
   * @memberof GnCodeListValue
   */
  label?: string;
  /**
   *
   * @type {string}
   * @memberof GnCodeListValue
   */
  definition?: string;
  /**
   *
   * @type {string}
   * @memberof GnCodeListValue
   */
  code?: string;
}

/**
 * Check if a given object implements the GnCodeListValue interface.
 */
export function instanceOfGnCodeListValue(
  value: object
): value is GnCodeListValue {
  return true;
}

export function GnCodeListValueFromJSON(json: any): GnCodeListValue {
  return GnCodeListValueFromJSONTyped(json, false);
}

export function GnCodeListValueFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): GnCodeListValue {
  if (json == null) {
    return json;
  }
  return {
    label: json['label'] == null ? undefined : json['label'],
    definition: json['definition'] == null ? undefined : json['definition'],
    code: json['code'] == null ? undefined : json['code'],
  };
}

export function GnCodeListValueToJSON(value?: GnCodeListValue | null): any {
  if (value == null) {
    return value;
  }
  return {
    label: value['label'],
    definition: value['definition'],
    code: value['code'],
  };
}
