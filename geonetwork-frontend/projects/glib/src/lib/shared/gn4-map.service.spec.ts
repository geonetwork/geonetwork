import { TestBed } from '@angular/core/testing';

import { Gn4MapService } from './gn4-map.service';

describe('Gn4MapService', () => {
  let service: Gn4MapService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Gn4MapService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
