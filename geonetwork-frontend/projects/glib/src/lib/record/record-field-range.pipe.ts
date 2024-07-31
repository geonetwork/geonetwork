import { inject, Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from '@angular/common';

interface RangeField {
  gte: number;
  lte: number;
}

@Pipe({
  name: 'gRecordFieldRange',
  standalone: true,
  pure: false,
})
export class RecordFieldRange implements PipeTransform {
  date = inject(DatePipe);

  transform(range: RangeField, format?: string): string {
    if (!range) {
      return '';
    }
    if (range.gte === range.lte) {
      return <string>this.date.transform(range.gte, format);
    }
    return `${this.date.transform(range.gte, format)} to ${this.date.transform(range.lte, format)}`;
  }
}
