import { Injectable } from '@angular/core';
import { DateRangeDetails } from 'g5api';

@Injectable({
  providedIn: 'root',
})
export class TemporalExtentService {
  constructor() {}

  parseDate(dateStr: string | undefined): Date | undefined {
    if (!dateStr) return;
    const timestamp = Date.parse(dateStr);
    return isNaN(timestamp) ? undefined : new Date(timestamp);
  }

  isValid(from: string | undefined, to: string | undefined): boolean {
    let fromDate = this.parseDate(from);
    let toDate = this.parseDate(to);
    return !!fromDate && !!toDate;
  }

  getRangeDetails(
    from: string | undefined,
    to: string | undefined
  ): DateRangeDetails {
    return {
      start: {
        date: from,
        // TODO
        // "frame": "#ISO-8601",
        // "indeterminatePosition": "now"
      },
      end: {
        date: to,
      },
    };
  }

  getCalendarRange(from: string | undefined, to: string | undefined) {
    return [this.parseDate(from), this.parseDate(to)] as Date[];
  }
}
