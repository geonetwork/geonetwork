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
 * @interface GnDatasetLayerField
 */
export interface GnDatasetLayerField {
  /**
   *
   * @type {string}
   * @memberof GnDatasetLayerField
   */
  name?: string;
  /**
   *
   * @type {string}
   * @memberof GnDatasetLayerField
   */
  type?: string;
  /**
   *
   * @type {boolean}
   * @memberof GnDatasetLayerField
   */
  nullable?: boolean;
  /**
   *
   * @type {string}
   * @memberof GnDatasetLayerField
   */
  defaultValue?: string;
}

/**
 * Check if a given object implements the GnDatasetLayerField interface.
 */
export function instanceOfGnDatasetLayerField(
  value: object
): value is GnDatasetLayerField {
  return true;
}

export function GnDatasetLayerFieldFromJSON(json: any): GnDatasetLayerField {
  return GnDatasetLayerFieldFromJSONTyped(json, false);
}

export function GnDatasetLayerFieldFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): GnDatasetLayerField {
  if (json == null) {
    return json;
  }
  return {
    name: json['name'] == null ? undefined : json['name'],
    type: json['type'] == null ? undefined : json['type'],
    nullable: json['nullable'] == null ? undefined : json['nullable'],
    defaultValue:
      json['defaultValue'] == null ? undefined : json['defaultValue'],
  };
}

export function GnDatasetLayerFieldToJSON(
  value?: GnDatasetLayerField | null
): any {
  if (value == null) {
    return value;
  }
  return {
    name: value['name'],
    type: value['type'],
    nullable: value['nullable'],
    defaultValue: value['defaultValue'],
  };
}
