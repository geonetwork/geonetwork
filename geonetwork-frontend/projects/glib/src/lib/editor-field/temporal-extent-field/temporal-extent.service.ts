import { Injectable } from '@angular/core';
import { DateRangeDetails } from 'g5api';

@Injectable({
  providedIn: 'root',
})
export class TemporalExtentService {
  constructor() {}

  private isMsTimestamp(dateStr?: string): boolean {
    return !!dateStr && /^\d+$/.test(dateStr) && dateStr.length !== 4;
  }

  private parseDate(dateStr?: string): Date | undefined {
    if (!dateStr) return;
    const timestamp = this.isMsTimestamp(dateStr)
      ? parseInt(dateStr, 10)
      : Date.parse(dateStr);
    return isNaN(timestamp) ? undefined : new Date(timestamp);
  }

  isValid(from?: string, to?: string): boolean {
    return !!this.parseDate(from) && !!this.parseDate(to);
  }

  buildDateRangeDetails(from?: string, to?: string): DateRangeDetails {
    const formatDate = (date?: string) =>
      this.isMsTimestamp(date)
        ? String(new Date(parseInt(date!, 10)).getFullYear())
        : date;

    return {
      start: { date: formatDate(from) },
      end: { date: formatDate(to) },
    };
  }

  getCalendarRange(from?: string, to?: string): Date[] {
    return [this.parseDate(from), this.parseDate(to)] as Date[];
  }
}
