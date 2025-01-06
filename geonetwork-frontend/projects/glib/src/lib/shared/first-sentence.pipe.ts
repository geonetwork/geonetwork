import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'firstSentence',
  standalone: true,
})
export class FirstSentencePipe implements PipeTransform {
  transform(text: string | undefined): string {
    return text?.replace(/^((?:.*?)[.?!])\s.*/, '$1') || '';
  }
}
