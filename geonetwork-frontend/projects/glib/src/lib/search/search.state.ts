import { elasticsearch } from 'gapi';
import {
  patchState,
  signalStore,
  withComputed,
  withHooks,
  withMethods,
  withProps,
  withState,
} from '@ngrx/signals';
import { computed, inject } from '@angular/core';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import {
  debounceTime,
  distinctUntilChanged,
  filter,
  from,
  pipe,
  switchMap,
  tap,
} from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { SearchService } from './search.service';
import {
  Search,
  SearchAggRefreshPolicy,
  SearchFilter,
  SearchFilterList,
  SearchFilterOperator,
  SearchFilterParameters,
  SearchFilterValue,
  SearchFilterValueState,
  SearchRequestPageParameters,
  SearchRequestParameters,
} from './search.state.model';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { toObservable } from '@angular/core/rxjs-interop';
import { SearchRouteService } from './search-route.service';
import { AppStore } from '../app.state';

export const DEFAULT_PAGE_SIZE = 10;

export const DEFAULT_SCORE = null;

export const DEFAULT_SORT: elasticsearch.Sort = ['_score'];

const initialSearchState: Search = {
  id: '',
  aggregationConfig: {},
  functionScore: DEFAULT_SCORE,
  isSearching: false,
  isReset: false,
  fullTextQuery: '',
  from: 0,
  size: DEFAULT_PAGE_SIZE,
  pageSize: DEFAULT_PAGE_SIZE,
  sort: DEFAULT_SORT,
  filters: {},
  filter: '',
  response: null,
  aggregation: {},
  error: null,
  routing: false,
};

export const SearchStore = signalStore(
  withState(initialSearchState),
  withProps(({ response }) => ({
    appStore: inject(AppStore),
    router: inject(Router),
    location: inject(Location),
    response$: toObservable(response),
    activeRoute: inject(ActivatedRoute),
  })),
  withComputed(store => {
    return {
      searchFilterParameters: computed(() => {
        return {
          fullTextQuery: store.fullTextQuery(),
          filters: store.filters(),
          sort: store.sort(),
          aggregationConfig: store.aggregationConfig(),
        } as SearchFilterParameters;
      }),
      searchRequestPageParameters: computed(() => {
        return {
          from: store.from(),
          size: store.size(),
        } as SearchRequestPageParameters;
      }),
      hasMore: computed(() => {
        let total = store.response()?.hits
          .total as elasticsearch.SearchTotalHits;
        return (
          total && total.value && store.from() + store.size() < total.value
        );
      }),
    };
  }),
  withMethods(
    (
      store,
      searchService = inject(SearchService),
      searchRouteService = inject(SearchRouteService)
    ) => ({
      init(
        searchId: string,
        aggregationConfig: Record<
          string,
          elasticsearch.AggregationsAggregationContainer
        >,
        functionScore: elasticsearch.QueryDslFunctionScoreQuery | null,
        size: number,
        sort: elasticsearch.Sort,
        filter: string,
        routing: boolean = false
      ) {
        patchState(store, {
          id: searchId,
          aggregationConfig,
          functionScore,
          size: size,
          pageSize: size,
          filter: filter,
          sort: sort || DEFAULT_SORT,
          routing: routing,
        });

        store.response$.subscribe(() => {
          if ((store.response()?.hits.total as any)?.value) {
            this.setRouting();
          }
        });
        if (store.routing()) {
          this.subscribeToRouteChange();
        }
      },
      setFullTextQuery(value: string) {
        patchState(store, { fullTextQuery: value });
      },
      setRouting() {
        if (!store.routing()) {
          return;
        }
        searchRouteService.setRoute(
          {
            from: store.from() || 0,
            size: store.size(),
            sort: store.sort(),
            fullTextQuery: store.fullTextQuery(),
            filter: store.filter(),
            filters: store.filters(),
          } as SearchRequestParameters,
          store.pageSize()
        );
      },
      subscribeToRouteChange() {
        if (!store.routing()) {
          return;
        }

        store.activeRoute.queryParams.subscribe(params => {
          patchState(
            store,
            searchRouteService.convertRouteParamsToSearch(
              params,
              store.pageSize()
            )
          );
        });
      },
      search: rxMethod<SearchFilterParameters>(
        pipe(
          distinctUntilChanged(),
          debounceTime(300),
          tap(console.log),
          tap(() =>
            patchState(store, {
              isSearching: true,
              response: null,
              error: null,
            })
          ),
          switchMap(searchFilterParameters =>
            from(
              searchService.search({
                ...searchFilterParameters,
                from: store.from() || 0,
                size: store.size(),
                sort: store.sort(),
                functionScore: store.functionScore(),
                filter: store.filter(),
              } as SearchRequestParameters)
            ).pipe(
              tapResponse({
                next: response => {
                  let aggregationConfig = store.aggregationConfig();

                  const aggregationToKeep = Object.fromEntries(
                    Object.entries(store.aggregation()).filter(
                      ([key]) =>
                        aggregationConfig[key]?.meta?.refreshPolicy ===
                        SearchAggRefreshPolicy.NO_REFRESH
                    )
                  );
                  const aggregation = {
                    ...response.aggregations,
                    ...aggregationToKeep,
                  };
                  patchState(store, { response, aggregation });
                },
                error: () => {
                  const error: Error = {
                    name: 'Search error',
                    message: `An error occurred while searching in context ${store.id()}. Check the console for more details and the search configuration for this context.`,
                  };
                  console.error(error);
                  patchState(store, { error: error });
                },
                finalize: () => patchState(store, { isSearching: false }),
              })
            )
          )
        )
      ),
      reset() {
        patchState(store, {
          isReset: true,
          filters: {},
          aggregation: {},
          fullTextQuery: '',
          from: 0,
        });
      },
      setFilter(value: string) {
        patchState(store, { filter: value });
      },
      isFilterActive(field: string, value: string) {
        const filter = store.filters()[field];
        return filter?.values[value];
      },
      addFilter(newFilter: SearchFilter, replaceFilter: boolean = false) {
        const filters = JSON.parse(JSON.stringify(store.filters()));
        const existingFilter = filters[newFilter.field];
        if (existingFilter && !replaceFilter) {
          existingFilter.values = {
            ...existingFilter.values,
            ...newFilter.values,
          };
        } else {
          filters[newFilter.field] = newFilter;
        }
        patchState(store, state => ({
          from: 0,
          size: store.pageSize(),
          filters: { ...filters },
        }));
      },
      removeFilter(field: string) {
        const filters = JSON.parse(JSON.stringify(store.filters()));
        delete filters[field];
        patchState(store, state => ({
          from: 0,
          size: store.pageSize(),
          filters: { ...filters },
        }));
      },
      removeFilterValue(field: string, value: string) {
        const filters = JSON.parse(JSON.stringify(store.filters()));
        const existingFilter = filters[field];
        if (existingFilter) {
          delete existingFilter.values[value];
        } else {
          console.warn(`No filter found for field ${field}.`);
        }
        patchState(store, state => ({
          from: 0,
          size: store.pageSize(),
          filters: { ...filters },
        }));
      },
      setPageSize(pageSize: number) {
        patchState(store, { pageSize, size: pageSize });
      },
      setSort(sort: elasticsearch.Sort) {
        patchState(store, {
          sort,
          from: 0,
          size: store.pageSize(),
        });
      },
      more(size: number) {
        patchState(store, { from: store.from() + store.size() });
      },
      setPage(from: number, size: number) {
        let response = JSON.parse(JSON.stringify(store.response()));
        if (response) {
          response.hits.hits = [];
        }
        patchState(store, { from, size, response });
      },
      next() {
        patchState(store, { from: store.from() + store.size() });
      },
      previous() {
        patchState(store, { from: store.from() - store.size() });
      },
      paging: rxMethod<SearchRequestPageParameters>(
        pipe(
          // distinctUntilChanged(),
          filter(() => !!store.response()),
          tap(() => patchState(store, { isSearching: true, error: null })),
          switchMap(searchRequestPageParameters =>
            from(
              searchService.page({
                ...store.searchFilterParameters(),
                ...searchRequestPageParameters,
                sort: store.sort(),
                functionScore: store.functionScore(),
                filter: store.filter(),
              })
            ).pipe(
              tap(console.log),
              tapResponse({
                next: newHits => {
                  let response = JSON.parse(JSON.stringify(store.response()));
                  if (response) {
                    response.hits.hits = response.hits.hits.concat(
                      newHits.hits.hits
                    );
                    patchState(store, {
                      response,
                    });
                  }
                },
                error: console.log,
                finalize: () => patchState(store, { isSearching: false }),
              })
            )
          )
        )
      ),
      buildDefaultAggregationConfig(key: string) {
        return {
          terms: {
            field: key,
            size: 50,
          },
          meta: {
            refreshPolicy: SearchAggRefreshPolicy.NO_REFRESH,
          },
        };
      },
      getAggregationConfig(
        key: string
      ): elasticsearch.AggregationsAggregationContainer {
        let aggregationConfiguration = JSON.parse(
          JSON.stringify(store.aggregationConfig())
        );
        let configuration = store.aggregationConfig()[key];
        if (configuration) {
          return configuration;
        } else {
          let newConfiguration = this.buildDefaultAggregationConfig(key);
          aggregationConfiguration[key] = newConfiguration;
          patchState(store, {
            aggregationConfig: aggregationConfiguration,
          });
          return newConfiguration;
        }
      },
      getAggregationSearchField(key: string): string {
        const aggregationConfig = store.aggregationConfig()[key];
        if (aggregationConfig as elasticsearch.AggregationsTermsAggregation) {
          return aggregationConfig.terms?.field || key;
        } else {
          return key;
        }
      },
    })
  ),
  withHooks({
    onInit({
      search,
      searchFilterParameters,
      paging,
      searchRequestPageParameters,
      appStore,
      reset
    }) {
      toObservable(appStore.authenticated).subscribe(() => {
        reset();
      } );
      search(searchFilterParameters);
      paging(searchRequestPageParameters);
    },
  })
);

export type SearchStoreType = InstanceType<typeof SearchStore>;
