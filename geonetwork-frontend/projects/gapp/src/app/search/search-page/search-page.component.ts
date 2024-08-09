import { Component, signal } from '@angular/core';
import { InputGroupModule } from 'primeng/inputgroup';
import {
  SearchAggComponent,
  SearchAggLayout,
  SearchAggRefreshPolicy,
  SearchAggsContainerComponent,
  SearchContextDirective,
  SearchInputComponent,
  SearchPagingComponent,
  SearchPagingMoreButtonComponent,
  SearchQueryResetDirective,
  SearchQuerySetterDirective,
  SearchResultsComponent,
} from 'glib';
import { SidebarModule } from 'primeng/sidebar';
import { Button, ButtonDirective } from 'primeng/button';
import { ChipsModule } from 'primeng/chips';
import { AggregationsAggregationContainer } from '@elastic/elasticsearch/lib/api/types';
import { PanelModule } from 'primeng/panel';
import { AvatarModule } from 'primeng/avatar';

@Component({
  selector: 'gn-search-page',
  templateUrl: './search-page.component.html',
  standalone: true,
  styleUrl: './search-page.component.css',
  imports: [
    InputGroupModule,
    SidebarModule,
    Button,
    SearchQuerySetterDirective,
    ChipsModule,
    ButtonDirective,
    SearchAggComponent,
    SearchQueryResetDirective,
    SearchAggsContainerComponent,
    SearchPagingComponent,
    SearchResultsComponent,
    SearchPagingMoreButtonComponent,
    SearchContextDirective,
    SearchInputComponent,
    PanelModule,
    AvatarModule,
  ],
})
export class SearchPageComponent {
  protected readonly SearchAggLayout = SearchAggLayout;
  themeSidebarSelector = signal(false);

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
}
