import { Component, inject, OnInit, signal } from '@angular/core';
import { InputGroupModule } from 'primeng/inputgroup';
import {
  SearchAggComponent,
  SearchAggLayout,
  SearchAggRefreshPolicy,
  SearchAggsContainerComponent,
  SearchFilterOperator,
  SearchFilterValueState,
  SearchPagingComponent,
  SearchPagingMoreButtonComponent,
  SearchQueryResetDirective,
  SearchResultsComponent,
  SearchService,
  SearchStoreType,
} from 'glib';
import { SidebarModule } from 'primeng/sidebar';
import { Button } from 'primeng/button';
import { elasticsearch } from 'gapi';
import { PanelModule } from 'primeng/panel';
import { AvatarModule } from 'primeng/avatar';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'gn-search-page',
  templateUrl: './search-page.component.html',
  standalone: true,
  imports: [
    InputGroupModule,
    SidebarModule,
    Button,
    SearchAggComponent,
    SearchQueryResetDirective,
    SearchAggsContainerComponent,
    SearchPagingComponent,
    SearchResultsComponent,
    SearchPagingMoreButtonComponent,
    PanelModule,
    AvatarModule,
  ],
})
export class SearchPageComponent implements OnInit {
  activeRoute = inject(ActivatedRoute);
  searchService = inject(SearchService);
  search: SearchStoreType;

  ngOnInit() {
    this.search = this.searchService.getSearch('main');
  }

  protected readonly SearchAggLayout = SearchAggLayout;
  themeSidebarSelector = signal(false);

  localAggregationConfig: Record<
    string,
    elasticsearch.AggregationsAggregationContainer
  > = {
    resourceType: {
      terms: {
        field: 'resourceType',
        include: ['dataset', 'service', 'series', 'map', 'featureCatalog'],
        missing: 'Others',
      },
      meta: {
        refreshPolicy: SearchAggRefreshPolicy.NO_REFRESH,
        decorator: {
          type: 'icon',
          prefix: 'fa fa-fw ',
          map: {
            dataset: 'fa-database',
            service: 'fa-cloud',
            map: 'fa-map',
          },
        },
      },
    },
    'cl_maintenanceAndUpdateFrequency.key': {
      terms: {
        field: 'cl_maintenanceAndUpdateFrequency.key',
        size: 10,
      },
      meta: {
        collapsed: true,
      },
    },
    tag: { terms: { field: 'tag.default', size: 20 } },
    organisationForResource: {
      terms: {
        field: 'OrgForResourceObject.default',
        include: '.*',
        size: 50,
        order: { _key: 'asc' },
      },
      meta: {
        layout: SearchAggLayout.MULTISELECT,
        refreshPolicy: SearchAggRefreshPolicy.NO_REFRESH,
      },
    },
  };
}
