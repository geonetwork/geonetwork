import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Button, ButtonDirective } from 'primeng/button';
import {
  GlibComponent,
  SearchAggsContainerComponent,
  SearchAggComponent,
  SearchAggLayout,
  SearchAggRefreshPolicy,
  SearchContextDirective,
  SearchInputComponent,
  SearchPagingComponent,
  SearchPagingMoreButtonComponent,
  SearchQueryResetDirective,
  SearchQuerySetterDirective,
  SearchResultsComponent,
  SearchResultsTimelineComponent,
  SearchService,
  APPLICATION_CONFIGURATION,
} from 'glib';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { AggregationsAggregationContainer } from '@elastic/elasticsearch/lib/api/types';
import { FormsModule } from '@angular/forms';
import { ScrollTopModule } from 'primeng/scrolltop';
import { SidebarModule } from 'primeng/sidebar';

@Component({
  selector: 'app-root',
  standalone: true,
  providers: [SearchService],
  imports: [
    RouterOutlet,
    Button,
    GlibComponent,
    SearchAggsContainerComponent,
    SearchAggComponent,
    SearchResultsComponent,
    SearchResultsTimelineComponent,
    SearchQuerySetterDirective,
    SearchQueryResetDirective,
    SearchPagingComponent,
    SearchPagingMoreButtonComponent,
    SearchInputComponent,
    InputGroupModule,
    ButtonDirective,
    InputGroupAddonModule,
    InputTextModule,
    SearchContextDirective,
    FormsModule,
    ScrollTopModule,
    SidebarModule,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'GeoNetwork';

  themeSidebarSelector = signal(false);

  uiConfiguration = inject(APPLICATION_CONFIGURATION).ui;

  scoreConfig = this.uiConfiguration?.mods.search.scoreConfig;
  pageSize = this.uiConfiguration?.mods.search.paginationInfo.hitsPerPage;
  aggregationConfig = this.uiConfiguration?.mods.search.facetConfig;
  localAggregationConfig: Record<string, AggregationsAggregationContainer> = {
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

  constructor() {}

  protected readonly SearchAggLayout = SearchAggLayout;
}
