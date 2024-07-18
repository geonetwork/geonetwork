import { SearchApi } from 'gapi';
import { SearchService } from './search.service';
import { SearchRequestParameters } from './search.state.model';
import { SearchRequest } from '@elastic/elasticsearch/lib/api/types';

export const fakeSearchService: Pick<SearchService, keyof SearchService> = {
  store: {},
  searchApi: new SearchApi(),
  register(searchId: string, searchStore) {
    searchStore.init('main', {});
    this.store['main'] = searchStore;
  },
  getSearch(searchId: string) {
    return this.store[searchId];
  },
  search(searchRequestParameters: SearchRequestParameters) {
    return new Promise((resolve, reject) => {
      () => {
        console.log('searchRequestParameters', searchRequestParameters);
        return {
          hits: {
            total: { value: 10 },
            hits: [
              {
                _id: '1',
                _source: {
                  resourceTitleObject: { default: 'title' },
                  uuid: '1',
                },
              },
            ],
          },
        };
      };
    });
  },
  page(searchRequestParameters: SearchRequestParameters) {
    return new Promise((resolve, reject) => {});
  },
  buildPageQuery(parameters: any) {
    return {};
  },
  buildQuery(parameters: any): SearchRequest {
    return {};
  },
};
