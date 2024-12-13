import { TestBed } from '@angular/core/testing';

import { ConfigService } from './config.service';

describe('ConfigService', () => {
  let service: ConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConfigService);
  });

  it('should parse a single filter expression correctly', () => {
    const filters = 'protocol:OGC:.*';
    const result = service.parseFilterExpression(filters);
    expect(result).equal([{ field: 'protocol', regex: /OGC:.*/, not: false }]);
  });

  it('should parse multiple filter expressions correctly', () => {
    const filters =
      'protocol:OGC:.*|ESRI:.* AND function:legend|featureCatalogue';
    const result = service.parseFilterExpression(filters);
    expect(result).equal([
      { field: 'protocol', regex: /OGC:.*|ESRI:.*/, not: false },
      { field: 'function', regex: /legend|featureCatalogue/, not: false },
    ]);
  });

  it('should parse negated filter expressions correctly', () => {
    const filters = '-protocol:OGC:.*|ESRI:.*';
    const result = service.parseFilterExpression(filters);
    expect(result).equal([
      { field: 'protocol', regex: /OGC:.*|ESRI:.*/, not: true },
    ]);
  });

  it('should return an empty array for an empty filter string', () => {
    const filters = '';
    const result = service.parseFilterExpression(filters);
    expect(result).equal([]);
  });

  it('should return an empty array for a filter string with only AND', () => {
    const filters = ' AND ';
    const result = service.parseFilterExpression(filters);
    expect(result).equal([]);
  });
});
