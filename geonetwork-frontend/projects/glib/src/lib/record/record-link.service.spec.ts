import { TestBed } from '@angular/core/testing';

import { RecordLinkService } from './record-link.service';

describe('DistributionService', () => {
  let service: RecordLinkService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecordLinkService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
