import { SearchQuerySetterDirective } from './search-query-setter.directive';
import { TestBed } from '@angular/core/testing';

describe('SearchQuerySetterDirective', () => {
  it('should create an instance', () => {
    TestBed.runInInjectionContext(() => {
      const directive = new SearchQuerySetterDirective();
      expect(directive).toBeTruthy();
    });
  });
});
