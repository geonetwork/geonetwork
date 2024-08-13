import { Pipe, PipeTransform } from '@angular/core';
import { JSONPath } from 'jsonpath-plus';

@Pipe({
  name: 'gJsonpath',
  standalone: true,
})
export class GJsonpathPipe implements PipeTransform {
  transform(json: any, path?: string): any {
    if (!path) {
      return;
    }
    return JSONPath({ path, json });
  }
}
