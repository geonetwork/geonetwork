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

import { mapValues } from '../runtime';
import type { FeatureType } from './FeatureType';
import {
  FeatureTypeFromJSON,
  FeatureTypeFromJSONTyped,
  FeatureTypeToJSON,
} from './FeatureType';
import type { MultilingualValue } from './MultilingualValue';
import {
  MultilingualValueFromJSON,
  MultilingualValueFromJSONTyped,
  MultilingualValueToJSON,
} from './MultilingualValue';
import type { Description } from './Description';
import {
  DescriptionFromJSON,
  DescriptionFromJSONTyped,
  DescriptionToJSON,
} from './Description';

/**
 *
 * @export
 * @interface FCRelatedMetadataItem
 */
export interface FCRelatedMetadataItem {
  /**
   *
   * @type {Description}
   * @memberof FCRelatedMetadataItem
   */
  description: Description;
  /**
   *
   * @type {FeatureType}
   * @memberof FCRelatedMetadataItem
   */
  featureType: FeatureType;
  /**
   *
   * @type {string}
   * @memberof FCRelatedMetadataItem
   */
  hash?: string;
  /**
   *
   * @type {string}
   * @memberof FCRelatedMetadataItem
   */
  id?: string;
  /**
   *
   * @type {string}
   * @memberof FCRelatedMetadataItem
   */
  idx?: string;
  /**
   *
   * @type {Array<string>}
   * @memberof FCRelatedMetadataItem
   */
  mdType: Array<string>;
  /**
   *
   * @type {string}
   * @memberof FCRelatedMetadataItem
   */
  origin?: string;
  /**
   *
   * @type {MultilingualValue}
   * @memberof FCRelatedMetadataItem
   */
  title: MultilingualValue;
  /**
   *
   * @type {string}
   * @memberof FCRelatedMetadataItem
   */
  type?: string;
  /**
   *
   * @type {MultilingualValue}
   * @memberof FCRelatedMetadataItem
   */
  url?: MultilingualValue;
}

/**
 * Check if a given object implements the FCRelatedMetadataItem interface.
 */
export function instanceOfFCRelatedMetadataItem(
  value: object
): value is FCRelatedMetadataItem {
  if (!('description' in value) || value['description'] === undefined)
    return false;
  if (!('featureType' in value) || value['featureType'] === undefined)
    return false;
  if (!('mdType' in value) || value['mdType'] === undefined) return false;
  if (!('title' in value) || value['title'] === undefined) return false;
  return true;
}

export function FCRelatedMetadataItemFromJSON(
  json: any
): FCRelatedMetadataItem {
  return FCRelatedMetadataItemFromJSONTyped(json, false);
}

export function FCRelatedMetadataItemFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): FCRelatedMetadataItem {
  if (json == null) {
    return json;
  }
  return {
    description: DescriptionFromJSON(json['description']),
    featureType: FeatureTypeFromJSON(json['featureType']),
    hash: json['hash'] == null ? undefined : json['hash'],
    id: json['id'] == null ? undefined : json['id'],
    idx: json['idx'] == null ? undefined : json['idx'],
    mdType: json['mdType'],
    origin: json['origin'] == null ? undefined : json['origin'],
    title: MultilingualValueFromJSON(json['title']),
    type: json['type'] == null ? undefined : json['type'],
    url:
      json['url'] == null ? undefined : MultilingualValueFromJSON(json['url']),
  };
}

export function FCRelatedMetadataItemToJSON(
  value?: FCRelatedMetadataItem | null
): any {
  if (value == null) {
    return value;
  }
  return {
    description: DescriptionToJSON(value['description']),
    featureType: FeatureTypeToJSON(value['featureType']),
    hash: value['hash'],
    id: value['id'],
    idx: value['idx'],
    mdType: value['mdType'],
    origin: value['origin'],
    title: MultilingualValueToJSON(value['title']),
    type: value['type'],
    url: MultilingualValueToJSON(value['url']),
  };
}