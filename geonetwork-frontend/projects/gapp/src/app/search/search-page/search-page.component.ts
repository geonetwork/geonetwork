import { Component, signal } from '@angular/core';
import { InputGroupModule } from 'primeng/inputgroup';
import {
  SearchAggComponent,
  SearchAggLayout,
  SearchAggRefreshPolicy,
  SearchAggsContainerComponent,
  SearchPagingComponent,
  SearchPagingMoreButtonComponent,
  SearchQueryResetDirective,
  SearchQuerySetterDirective,
  SearchResultsComponent,
} from 'glib';
import { SidebarModule } from 'primeng/sidebar';
import { Button, ButtonDirective } from 'primeng/button';
import { elasticsearch } from 'gapi';
import { PanelModule } from 'primeng/panel';
import { AvatarModule } from 'primeng/avatar';
import { InputText } from 'primeng/inputtext';
import { AutoFocus } from 'primeng/autofocus';

@Component({
  selector: 'gn-search-page',
  templateUrl: './search-page.component.html',
  standalone: true,
  imports: [
    InputGroupModule,
    SidebarModule,
    Button,
    SearchQuerySetterDirective,
    ButtonDirective,
    SearchAggComponent,
    SearchQueryResetDirective,
    SearchAggsContainerComponent,
    SearchPagingComponent,
    SearchResultsComponent,
    SearchPagingMoreButtonComponent,
    PanelModule,
    AvatarModule,
    InputText,
    AutoFocus,
  ],
})
export class SearchPageComponent {
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
