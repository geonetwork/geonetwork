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
import type { GnCodeListValue } from './GnGnCodeListValue';
import {
  GnCodeListValueFromJSON,
  GnCodeListValueFromJSONTyped,
  GnCodeListValueToJSON,
} from './GnGnCodeListValue';

/**
 *
 * @export
 * @interface GnAttributeTable
 */
export interface GnAttributeTable {
  /**
   *
   * @type {string}
   * @memberof GnAttributeTable
   */
  name?: string;
  /**
   *
   * @type {string}
   * @memberof GnAttributeTable
   */
  definition?: string;
  /**
   *
   * @type {string}
   * @memberof GnAttributeTable
   */
  code?: string;
  /**
   *
   * @type {string}
   * @memberof GnAttributeTable
   */
  link?: string;
  /**
   *
   * @type {string}
   * @memberof GnAttributeTable
   */
  type?: string;
  /**
   *
   * @type {string}
   * @memberof GnAttributeTable
   */
  cardinality?: string;
  /**
   *
   * @type {Array<GnCodeListValue>}
   * @memberof GnAttributeTable
   */
  values?: Array<GnCodeListValue>;
}

/**
 * Check if a given object implements the GnAttributeTable interface.
 */
export function instanceOfGnAttributeTable(
  value: object
): value is GnAttributeTable {
  return true;
}

export function GnAttributeTableFromJSON(json: any): GnAttributeTable {
  return GnAttributeTableFromJSONTyped(json, false);
}

export function GnAttributeTableFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): GnAttributeTable {
  if (json == null) {
    return json;
  }
  return {
    name: json['name'] == null ? undefined : json['name'],
    definition: json['definition'] == null ? undefined : json['definition'],
    code: json['code'] == null ? undefined : json['code'],
    link: json['link'] == null ? undefined : json['link'],
    type: json['type'] == null ? undefined : json['type'],
    cardinality: json['cardinality'] == null ? undefined : json['cardinality'],
    values:
      json['values'] == null
        ? undefined
        : (json['values'] as Array<any>).map(GnCodeListValueFromJSON),
  };
}

export function GnAttributeTableToJSON(value?: GnAttributeTable | null): any {
  if (value == null) {
    return value;
  }
  return {
    name: value['name'],
    definition: value['definition'],
    code: value['code'],
    link: value['link'],
    type: value['type'],
    cardinality: value['cardinality'],
    values:
      value['values'] == null
        ? undefined
        : (value['values'] as Array<any>).map(GnCodeListValueToJSON),
  };
}
