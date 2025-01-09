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
 * @interface GnMetadataResourceExternalManagementProperties
 */
export interface GnMetadataResourceExternalManagementProperties {
    /**
     * 
     * @type {string}
     * @memberof GnMetadataResourceExternalManagementProperties
     */
    id?: string;
    /**
     * 
     * @type {string}
     * @memberof GnMetadataResourceExternalManagementProperties
     */
    url?: string;
    /**
     * 
     * @type {string}
     * @memberof GnMetadataResourceExternalManagementProperties
     */
    validationStatus?: GnMetadataResourceExternalManagementPropertiesValidationStatusEnum;
}


/**
 * @export
 */
export const GnMetadataResourceExternalManagementPropertiesValidationStatusEnum = {
    Unknown: 'UNKNOWN',
    Valid: 'VALID',
    Incomplete: 'INCOMPLETE'
} as const;
export type GnMetadataResourceExternalManagementPropertiesValidationStatusEnum = typeof GnMetadataResourceExternalManagementPropertiesValidationStatusEnum[keyof typeof GnMetadataResourceExternalManagementPropertiesValidationStatusEnum];


/**
 * Check if a given object implements the GnMetadataResourceExternalManagementProperties interface.
 */
export function instanceOfGnMetadataResourceExternalManagementProperties(value: object): value is GnMetadataResourceExternalManagementProperties {
    return true;
}

export function GnMetadataResourceExternalManagementPropertiesFromJSON(json: any): GnMetadataResourceExternalManagementProperties {
    return GnMetadataResourceExternalManagementPropertiesFromJSONTyped(json, false);
}

export function GnMetadataResourceExternalManagementPropertiesFromJSONTyped(json: any, ignoreDiscriminator: boolean): GnMetadataResourceExternalManagementProperties {
    if (json == null) {
        return json;
    }
    return {
        
        'id': json['id'] == null ? undefined : json['id'],
        'url': json['url'] == null ? undefined : json['url'],
        'validationStatus': json['validationStatus'] == null ? undefined : json['validationStatus'],
    };
}

export function GnMetadataResourceExternalManagementPropertiesToJSON(value?: GnMetadataResourceExternalManagementProperties | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'id': value['id'],
        'url': value['url'],
        'validationStatus': value['validationStatus'],
    };
}
