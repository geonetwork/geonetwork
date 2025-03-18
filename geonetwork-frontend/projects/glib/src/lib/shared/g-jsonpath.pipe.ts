import { Injectable, Pipe, PipeTransform } from '@angular/core';
import { JSONPath } from 'jsonpath-plus';

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
    if (path.indexOf('related') !== 0) {
      path = '_source.' + path;
    }
    return JSONPath({ path, json });
  }
}
