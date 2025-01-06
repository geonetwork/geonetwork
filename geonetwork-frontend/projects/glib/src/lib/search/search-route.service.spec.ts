import { TestBed } from '@angular/core/testing';

import { SearchRouteService } from './search-route.service';

describe('SearchRouteService', () => {
  let service: SearchRouteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchRouteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
