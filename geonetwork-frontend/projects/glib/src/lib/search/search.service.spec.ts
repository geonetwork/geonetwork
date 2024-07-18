import { TestBed } from '@angular/core/testing';

import { SearchService } from './search.service';
import { DEFAULT_PAGE_SIZE, SearchStore } from './search.state';
import { SearchRequestParameters } from './search.state.model';
import { SearchApi } from 'gapi';

let DEFAULT_SEARCH_REQUEST: SearchRequestParameters = {
  fullTextQuery: 'africa',
  filters: {},
  aggregationConfig: {},
  from: 0,
  size: DEFAULT_PAGE_SIZE,
};
describe('SearchService', () => {
  let service: SearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should throw error is requested search scope is not registered', () => {
    expect(function () {
      service.getSearch('nonExistingScope');
    }).toThrowError(
      'Search nonExistingScope not found. Available search contexts are: '
    );
  });

  it('should throw error if registering 2 times the same search scope id', () => {
    expect(function () {
      TestBed.runInInjectionContext(() => {
        service.register('main', new SearchStore());
        service.register('main', new SearchStore());
      });
    }).toThrowError(
      'Search main already registered. Choose another search id.'
    );
  });

  it('should register new search scope with initial value', () => {
    TestBed.runInInjectionContext(() => {
      service.register('main', new SearchStore());
    });
    expect(service.getSearch('main')).toBeTruthy();
    expect(service.getSearch('main').id()).toBe('');
    expect(service.getSearch('main').fullTextQuery()).toBe('');
    expect(service.getSearch('main').from()).toBe(0);
    expect(service.getSearch('main').size()).toBe(DEFAULT_PAGE_SIZE);
    expect(service.getSearch('main').pageSize()).toBe(DEFAULT_PAGE_SIZE);
    // expect(service.getSearch('main').isSearching()).toBeFalse();
  });

  it('should build search query', () => {
    TestBed.runInInjectionContext(() => {
      service.register('main', new SearchStore());
    });

    let searchRequest = service.buildQuery(DEFAULT_SEARCH_REQUEST);

    expect(searchRequest).toEqual({
      from: 0,
      size: DEFAULT_PAGE_SIZE,
      aggregations: {},
      query: {
        bool: {
          must: [
            {
              query_string: {
                query: 'africa',
              },
            },
          ],
        },
      },
    });
  });

  it('should build page query without aggregations', () => {
    TestBed.runInInjectionContext(() => {
      service.register('main', new SearchStore());
    });
    let searchPageRequest = service.buildPageQuery({
      from: 11,
    } as SearchRequestParameters);
    expect(searchPageRequest.aggregations).toBeUndefined();
    expect(searchPageRequest.from).toBe(11);
  });

  it('should call search API', () => {
    TestBed.runInInjectionContext(() => {
      service.register('main', new SearchStore());
    });

    let searchApiFn = spyOn(service.searchApi, 'search').and.resolveTo(
      {} as any
    );
    service.search(DEFAULT_SEARCH_REQUEST);
    expect(searchApiFn).toHaveBeenCalled();
  });
});
