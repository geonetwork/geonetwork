import { TestBed } from '@angular/core/testing';

import { TemporalExtentService } from './temporal-extent.service';

describe('TemporalExtentService', () => {
  let service: TemporalExtentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TemporalExtentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
