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
 * @interface GnOgcApiRecordsExternalDocumentationDto
 */
export interface GnOgcApiRecordsExternalDocumentationDto {
  /**
   *
   * @type {string}
   * @memberof GnOgcApiRecordsExternalDocumentationDto
   */
  description?: string;
  /**
   *
   * @type {string}
   * @memberof GnOgcApiRecordsExternalDocumentationDto
   */
  url: string;
}

/**
 * Check if a given object implements the GnOgcApiRecordsExternalDocumentationDto interface.
 */
export function instanceOfGnOgcApiRecordsExternalDocumentationDto(
  value: object
): value is GnOgcApiRecordsExternalDocumentationDto {
  if (!('url' in value) || value['url'] === undefined) return false;
  return true;
}

export function GnOgcApiRecordsExternalDocumentationDtoFromJSON(
  json: any
): GnOgcApiRecordsExternalDocumentationDto {
  return GnOgcApiRecordsExternalDocumentationDtoFromJSONTyped(json, false);
}

export function GnOgcApiRecordsExternalDocumentationDtoFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): GnOgcApiRecordsExternalDocumentationDto {
  if (json == null) {
    return json;
  }
  return {
    description: json['description'] == null ? undefined : json['description'],
    url: json['url'],
  };
}

export function GnOgcApiRecordsExternalDocumentationDtoToJSON(
  value?: GnOgcApiRecordsExternalDocumentationDto | null
): any {
  if (value == null) {
    return value;
  }
  return {
    description: value['description'],
    url: value['url'],
  };
}
