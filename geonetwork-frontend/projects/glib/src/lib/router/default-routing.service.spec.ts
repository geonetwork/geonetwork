import { TestBed } from '@angular/core/testing';

import { DefaultRoutingService } from './default-routing.service';

describe('DefaultRoutingService', () => {
  let service: DefaultRoutingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DefaultRoutingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
