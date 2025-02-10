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
import type { MetadataResourceExternalManagementProperties } from './MetadataResourceExternalManagementProperties';
import {
  MetadataResourceExternalManagementPropertiesFromJSON,
  MetadataResourceExternalManagementPropertiesFromJSONTyped,
  MetadataResourceExternalManagementPropertiesToJSON,
} from './MetadataResourceExternalManagementProperties';

/**
 *
 * @export
 * @interface MetadataResource
 */
export interface MetadataResource {
  /**
   *
   * @type {string}
   * @memberof MetadataResource
   */
  id?: string;
  /**
   *
   * @type {number}
   * @memberof MetadataResource
   */
  size?: number;
  /**
   *
   * @type {string}
   * @memberof MetadataResource
   */
  version?: string;
  /**
   *
   * @type {string}
   * @memberof MetadataResource
   */
  url?: string;
  /**
   *
   * @type {MetadataResourceExternalManagementProperties}
   * @memberof MetadataResource
   */
  metadataResourceExternalManagementProperties?: MetadataResourceExternalManagementProperties;
  /**
   *
   * @type {Date}
   * @memberof MetadataResource
   */
  lastModification?: Date;
  /**
   *
   * @type {string}
   * @memberof MetadataResource
   */
  metadataUuid?: string;
  /**
   *
   * @type {boolean}
   * @memberof MetadataResource
   */
  approved?: boolean;
  /**
   *
   * @type {number}
   * @memberof MetadataResource
   */
  metadataId?: number;
  /**
   *
   * @type {string}
   * @memberof MetadataResource
   */
  filename?: string;
  /**
   *
   * @type {string}
   * @memberof MetadataResource
   */
  visibility?: MetadataResourceVisibilityEnum;
}

/**
 * @export
 */
export const MetadataResourceVisibilityEnum = {
  Public: 'public',
  Private: 'private',
} as const;
export type MetadataResourceVisibilityEnum =
  (typeof MetadataResourceVisibilityEnum)[keyof typeof MetadataResourceVisibilityEnum];

/**
 * Check if a given object implements the MetadataResource interface.
 */
export function instanceOfMetadataResource(
  value: object
): value is MetadataResource {
  return true;
}

export function MetadataResourceFromJSON(json: any): MetadataResource {
  return MetadataResourceFromJSONTyped(json, false);
}

export function MetadataResourceFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): MetadataResource {
  if (json == null) {
    return json;
  }
  return {
    id: json['id'] == null ? undefined : json['id'],
    size: json['size'] == null ? undefined : json['size'],
    version: json['version'] == null ? undefined : json['version'],
    url: json['url'] == null ? undefined : json['url'],
    metadataResourceExternalManagementProperties:
      json['metadataResourceExternalManagementProperties'] == null
        ? undefined
        : MetadataResourceExternalManagementPropertiesFromJSON(
            json['metadataResourceExternalManagementProperties']
          ),
    lastModification:
      json['lastModification'] == null
        ? undefined
        : new Date(json['lastModification']),
    metadataUuid:
      json['metadataUuid'] == null ? undefined : json['metadataUuid'],
    approved: json['approved'] == null ? undefined : json['approved'],
    metadataId: json['metadataId'] == null ? undefined : json['metadataId'],
    filename: json['filename'] == null ? undefined : json['filename'],
    visibility: json['visibility'] == null ? undefined : json['visibility'],
  };
}

export function MetadataResourceToJSON(value?: MetadataResource | null): any {
  if (value == null) {
    return value;
  }
  return {
    id: value['id'],
    size: value['size'],
    version: value['version'],
    url: value['url'],
    metadataResourceExternalManagementProperties:
      MetadataResourceExternalManagementPropertiesToJSON(
        value['metadataResourceExternalManagementProperties']
      ),
    lastModification:
      value['lastModification'] == null
        ? undefined
        : value['lastModification'].toISOString(),
    metadataUuid: value['metadataUuid'],
    approved: value['approved'],
    metadataId: value['metadataId'],
    filename: value['filename'],
    visibility: value['visibility'],
  };
}
