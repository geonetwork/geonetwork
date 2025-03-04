import { Injectable } from '@angular/core';
import { VerticalRange } from 'g5api';

@Injectable({
  providedIn: 'root',
})
export class VerticalExtentService {
  constructor() {}

  buildVerticalExtent(gte: number, lte: number): VerticalRange {
    return {
      gte: gte,
      lte: lte,
      unit: 'm',
    };
  }
}
