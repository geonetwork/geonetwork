import { Injectable, Pipe, PipeTransform } from '@angular/core';
import { JSONPathJS } from 'jsonpath-js';

@Pipe({
  name: 'gJsonpath',
  standalone: true,
})
@Injectable({ providedIn: 'root' })
export class GJsonpathPipe implements PipeTransform {
  transform(json: any, path?: string): any {
    if (!path) {
      return;
    }

    try {
      const query = new JSONPathJS(path);
      return query.find(json);
    } catch (e) {
      console.error(
        `Error occurred with JSONPath ${path}. Check the expression.`,
        e
      );
    }
  }
}
