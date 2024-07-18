import { SearchQueryResetDirective } from './search-query-reset.directive';
import { TestBed } from '@angular/core/testing';

describe('SearchQueryResetDirective', () => {
  it('should create an instance', () => {
    TestBed.runInInjectionContext(() => {
      const directive = new SearchQueryResetDirective();
      expect(directive).toBeTruthy();
    });
  });
});
