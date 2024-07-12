import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Button, ButtonDirective } from 'primeng/button';
import {
  GlibComponent,
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
  SearchService,
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
    SearchAggComponent,
    SearchResultsComponent,
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

  aggregationConfig: Record<string, AggregationsAggregationContainer> = {
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
    topic: {
      terms: {
        field: 'cl_topic.key',
        size: 20,
      },
      meta: {
        refreshPolicy: SearchAggRefreshPolicy.NO_REFRESH,
        layout: SearchAggLayout.CARD,
        decorator: {
          type: 'img',
          map: {
            biota:
              'https://live.staticflickr.com/8383/8616798436_4d6c64ef1b_b.jpg',
            farming:
              'https://live.staticflickr.com/8208/8278649915_9288846c34.jpg',
            boundaries:
              'https://live.staticflickr.com/4246/34760368741_e58a034d2c_b.jpg',
            inlandWaters:
              'https://live.staticflickr.com/4870/40440037563_e8839c12f2_b.jpg',
            environment:
              'https://live.staticflickr.com/3905/14744038903_387abf1902_b.jpg',
            geoscientificInformation:
              'https://live.staticflickr.com/5709/23453476815_d861652686_b.jpg',
            imageryBaseMapsEarthCover:
              'https://live.staticflickr.com/1561/24270793842_f32d495613_b.jpg',
          },
        },
        orderByTranslation: true,
      },
    },
    resolutionScaleDenominator: {
      histogram: {
        field: 'resolutionScaleDenominator',
        interval: 10000,
        keyed: true,
        min_doc_count: 1,
      },
      meta: {
        collapsed: true,
      },
    },
  };
  protected readonly SearchAggLayout = SearchAggLayout;
}
