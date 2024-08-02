/* tslint:disable */
/* eslint-disable */
/**
 * GeoNetwork 4.4.5 OpenAPI Documentation
 * This is the description of the GeoNetwork OpenAPI. Use this API to manage your catalog.
 *
 * The version of the OpenAPI document: 4.4.5
 * Contact: geonetwork-users@lists.sourceforge.net
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { mapValues } from '../runtime';
import type { Entry } from './Entry';
import { EntryFromJSON, EntryFromJSONTyped, EntryToJSON } from './Entry';

/**
 *
 * @export
 * @interface Codelist
 */
export interface Codelist {
  /**
   *
   * @type {string}
   * @memberof Codelist
   */
  alias?: string;
  /**
   *
   * @type {Array<Entry>}
   * @memberof Codelist
   */
  entry?: Array<Entry>;
  /**
   *
   * @type {string}
   * @memberof Codelist
   */
  name?: string;
}

/**
 * Check if a given object implements the Codelist interface.
 */
export function instanceOfCodelist(value: object): value is Codelist {
  return true;
}

export function CodelistFromJSON(json: any): Codelist {
  return CodelistFromJSONTyped(json, false);
}

export function CodelistFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): Codelist {
  if (json == null) {
    return json;
  }
  return {
    alias: json['alias'] == null ? undefined : json['alias'],
    entry:
      json['entry'] == null
        ? undefined
        : (json['entry'] as Array<any>).map(EntryFromJSON),
    name: json['name'] == null ? undefined : json['name'],
  };
}

export function CodelistToJSON(value?: Codelist | null): any {
  if (value == null) {
    return value;
  }
  return {
    alias: value['alias'],
    entry:
      value['entry'] == null
        ? undefined
        : (value['entry'] as Array<any>).map(EntryToJSON),
    name: value['name'],
  };
}