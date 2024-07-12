import { TestBed } from '@angular/core/testing';

import { GlibService } from './glib.service';

describe('GlibService', () => {
  let service: GlibService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GlibService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
