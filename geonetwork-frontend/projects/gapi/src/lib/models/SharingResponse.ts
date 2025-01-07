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
import type { GroupPrivilege } from './GroupPrivilege';
import {
  GroupPrivilegeFromJSON,
  GroupPrivilegeFromJSONTyped,
  GroupPrivilegeToJSON,
} from './GroupPrivilege';

/**
 *
 * @export
 * @interface SharingResponse
 */
export interface SharingResponse {
  /**
   *
   * @type {string}
   * @memberof SharingResponse
   */
  groupOwner?: string;
  /**
   *
   * @type {string}
   * @memberof SharingResponse
   */
  owner?: string;
  /**
   *
   * @type {Array<GroupPrivilege>}
   * @memberof SharingResponse
   */
  privileges?: Array<GroupPrivilege>;
}

/**
 * Check if a given object implements the SharingResponse interface.
 */
export function instanceOfSharingResponse(
  value: object
): value is SharingResponse {
  return true;
}

export function SharingResponseFromJSON(json: any): SharingResponse {
  return SharingResponseFromJSONTyped(json, false);
}

export function SharingResponseFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): SharingResponse {
  if (json == null) {
    return json;
  }
  return {
    groupOwner: json['groupOwner'] == null ? undefined : json['groupOwner'],
    owner: json['owner'] == null ? undefined : json['owner'],
    privileges:
      json['privileges'] == null
        ? undefined
        : (json['privileges'] as Array<any>).map(GroupPrivilegeFromJSON),
  };
}

export function SharingResponseToJSON(value?: SharingResponse | null): any {
  if (value == null) {
    return value;
  }
  return {
    groupOwner: value['groupOwner'],
    owner: value['owner'],
    privileges:
      value['privileges'] == null
        ? undefined
        : (value['privileges'] as Array<any>).map(GroupPrivilegeToJSON),
  };
}