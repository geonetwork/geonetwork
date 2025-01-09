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
 * @interface BatchEditParameter
 */
export interface BatchEditParameter {
    /**
     * 
     * @type {string}
     * @memberof BatchEditParameter
     */
    xpath: string;
    /**
     * 
     * @type {string}
     * @memberof BatchEditParameter
     */
    value: string;
    /**
     * 
     * @type {string}
     * @memberof BatchEditParameter
     */
    condition?: string;
}

/**
 * Check if a given object implements the BatchEditParameter interface.
 */
export function instanceOfBatchEditParameter(value: object): value is BatchEditParameter {
    if (!('xpath' in value) || value['xpath'] === undefined) return false;
    if (!('value' in value) || value['value'] === undefined) return false;
    return true;
}

export function BatchEditParameterFromJSON(json: any): BatchEditParameter {
    return BatchEditParameterFromJSONTyped(json, false);
}

export function BatchEditParameterFromJSONTyped(json: any, ignoreDiscriminator: boolean): BatchEditParameter {
    if (json == null) {
        return json;
    }
    return {
        
        'xpath': json['xpath'],
        'value': json['value'],
        'condition': json['condition'] == null ? undefined : json['condition'],
    };
}

export function BatchEditParameterToJSON(value?: BatchEditParameter | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'xpath': value['xpath'],
        'value': value['value'],
        'condition': value['condition'],
    };
}
