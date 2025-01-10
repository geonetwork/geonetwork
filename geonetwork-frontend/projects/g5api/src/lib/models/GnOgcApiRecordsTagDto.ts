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
import type { GnOgcApiRecordsExternalDocumentationDto } from './GnGnOgcApiRecordsExternalDocumentationDto';
import {
  GnOgcApiRecordsExternalDocumentationDtoFromJSON,
  GnOgcApiRecordsExternalDocumentationDtoFromJSONTyped,
  GnOgcApiRecordsExternalDocumentationDtoToJSON,
} from './GnGnOgcApiRecordsExternalDocumentationDto';

/**
 *
 * @export
 * @interface GnOgcApiRecordsTagDto
 */
export interface GnOgcApiRecordsTagDto {
  /**
   *
   * @type {string}
   * @memberof GnOgcApiRecordsTagDto
   */
  name: string;
  /**
   *
   * @type {string}
   * @memberof GnOgcApiRecordsTagDto
   */
  description?: string;
  /**
   *
   * @type {GnOgcApiRecordsExternalDocumentationDto}
   * @memberof GnOgcApiRecordsTagDto
   */
  externalDocs?: GnOgcApiRecordsExternalDocumentationDto;
}

/**
 * Check if a given object implements the GnOgcApiRecordsTagDto interface.
 */
export function instanceOfGnOgcApiRecordsTagDto(
  value: object
): value is GnOgcApiRecordsTagDto {
  if (!('name' in value) || value['name'] === undefined) return false;
  return true;
}

export function GnOgcApiRecordsTagDtoFromJSON(
  json: any
): GnOgcApiRecordsTagDto {
  return GnOgcApiRecordsTagDtoFromJSONTyped(json, false);
}

export function GnOgcApiRecordsTagDtoFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): GnOgcApiRecordsTagDto {
  if (json == null) {
    return json;
  }
  return {
    name: json['name'],
    description: json['description'] == null ? undefined : json['description'],
    externalDocs:
      json['externalDocs'] == null
        ? undefined
        : GnOgcApiRecordsExternalDocumentationDtoFromJSON(json['externalDocs']),
  };
}

export function GnOgcApiRecordsTagDtoToJSON(
  value?: GnOgcApiRecordsTagDto | null
): any {
  if (value == null) {
    return value;
  }
  return {
    name: value['name'],
    description: value['description'],
    externalDocs: GnOgcApiRecordsExternalDocumentationDtoToJSON(
      value['externalDocs']
    ),
  };
}
