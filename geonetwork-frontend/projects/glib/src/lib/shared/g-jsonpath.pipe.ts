import { Pipe, PipeTransform } from '@angular/core';
import { JSONPath } from 'jsonpath-plus';

@Pipe({
  name: 'gJsonpath',
  standalone: true,
})
export class GJsonpathPipe implements PipeTransform {
  transform(json: any, path: string): any {
    return JSONPath({ path, json });
  }
}
