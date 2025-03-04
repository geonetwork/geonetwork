import { TestBed } from '@angular/core/testing';

import { VerticalExtentService } from './vertical-extent.service';

describe('VerticalExtentService', () => {
  let service: VerticalExtentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VerticalExtentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
