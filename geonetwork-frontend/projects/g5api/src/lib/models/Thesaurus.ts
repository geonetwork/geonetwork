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
 * @interface Thesaurus
 */
export interface Thesaurus {
  /**
   *
   * @type {string}
   * @memberof Thesaurus
   */
  id?: string;
  /**
   *
   * @type {string}
   * @memberof Thesaurus
   */
  title?: string;
  /**
   *
   * @type {{ [key: string]: string; }}
   * @memberof Thesaurus
   */
  multilingualTitle?: { [key: string]: string };
  /**
   *
   * @type {string}
   * @memberof Thesaurus
   */
  theme?: string;
  /**
   *
   * @type {string}
   * @memberof Thesaurus
   */
  link?: string;
  /**
   *
   * @type {Array<object>}
   * @memberof Thesaurus
   */
  keywords?: Array<object>;
}

/**
 * Check if a given object implements the Thesaurus interface.
 */
export function instanceOfThesaurus(value: object): value is Thesaurus {
  return true;
}

export function ThesaurusFromJSON(json: any): Thesaurus {
  return ThesaurusFromJSONTyped(json, false);
}

export function ThesaurusFromJSONTyped(
  json: any,
  ignoreDiscriminator: boolean
): Thesaurus {
  if (json == null) {
    return json;
  }
  return {
    id: json['id'] == null ? undefined : json['id'],
    title: json['title'] == null ? undefined : json['title'],
    multilingualTitle:
      json['multilingualTitle'] == null ? undefined : json['multilingualTitle'],
    theme: json['theme'] == null ? undefined : json['theme'],
    link: json['link'] == null ? undefined : json['link'],
    keywords: json['keywords'] == null ? undefined : json['keywords'],
  };
}

export function ThesaurusToJSON(value?: Thesaurus | null): any {
  if (value == null) {
    return value;
  }
  return {
    id: value['id'],
    title: value['title'],
    multilingualTitle: value['multilingualTitle'],
    theme: value['theme'],
    link: value['link'],
    keywords: value['keywords'],
  };
}
