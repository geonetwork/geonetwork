import { inject, Pipe, PipeTransform } from '@angular/core';
import { API_CONFIGURATION } from '../config/config.loader';

@Pipe({
  name: 'gUrlPlaceholder',
  standalone: true,
})
export class UrlPlaceholderPipe implements PipeTransform {
  apiConfiguration = inject(API_CONFIGURATION);

  transform(value?: string, uuid?: string): unknown {
    return (
      value
        ?.replace('${uuid}', uuid || '')
        .replace('${apiUrl}', this.apiConfiguration().basePath) || ''
    );
  }
}
