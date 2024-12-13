import { Injectable } from '@angular/core';

export interface Filter {
  field: string;
  regex: RegExp;
  not: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  /**
   * Parse a filter expression into an array of filters.
   * eg.
   * protocol:OGC:.*|ESRI:.*|atom.*
   * -protocol:OGC.*|REST|ESRI:.*|atom.*|.*DOWNLOAD.*|DB:.*|FILE:.* AND -function:legend|featureCatalogue|dataQualityReport
   */
  parseFilterExpression(filters: string): Filter[] {
    const separator = ':';
    return filters
      .split(' AND ')
      .map(function (clause) {
        const filter = clause.split(separator),
          field = filter.shift(),
          not = field && field.startsWith('-');
        if (!field) {
          return undefined;
        }
        return {
          field: not ? field.slice(1) : field,
          regex: new RegExp(filter.join(separator)),
          not: not,
        };
      })
      .filter((filter): filter is Filter => !!filter);
  }

  /**
   * Test if an object matches a set of filters.
   */
  testExpressionFilters(filters: Filter[], object: any): boolean {
    const results: boolean[] = [];
    filters.forEach(function (filter, j) {
      const prop = object[filter.field];
      if (
        prop !== undefined &&
        ((!filter.not && prop.match(filter.regex) != null) ||
          (filter.not && prop.match(filter.regex) == null))
      ) {
        results[j] = true;
      } else {
        results[j] = false;
      }
    });
    return results.reduce(function (prev, curr) {
      return prev && curr;
    });
  }
}
