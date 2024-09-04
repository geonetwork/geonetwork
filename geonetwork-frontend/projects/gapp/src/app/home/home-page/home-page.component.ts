import { Component, inject } from '@angular/core';
import {
  APPLICATION_CONFIGURATION,
  DEFAULT_PAGE_SIZE,
  SearchAggComponent,
  SearchAggLayout,
  SearchContextDirective,
  SearchResultsCarouselComponent,
  SearchResultsFirstOverviewAsBackgroundDirective,
  SearchResultsLayoutDirective,
  SearchResultsTimelineComponent,
} from 'glib';
import { Params } from '../../../../../gapi/src/lib/ui-settings';
import { Sort } from '@elastic/elasticsearch/lib/api/types';
import { CarouselModule } from 'primeng/carousel';

@Component({
  selector: 'gn-home-page',
  templateUrl: './home-page.component.html',
  standalone: true,
  styleUrl: './home-page.component.css',
  imports: [
    SearchContextDirective,
    SearchAggComponent,
    SearchResultsTimelineComponent,
    SearchResultsCarouselComponent,
    SearchResultsLayoutDirective,
    SearchResultsFirstOverviewAsBackgroundDirective,
    CarouselModule,
  ],
})
export class HomePageComponent {
  protected readonly SearchAggLayout = SearchAggLayout;

  uiConfiguration = inject(APPLICATION_CONFIGURATION).ui;

  getSort(params: Params): Sort {
    return params.sortBy ? [{ [params.sortBy]: params.sortOrder }] : ['_score'];
  }
  getSize(params: Params): number {
    return params.to ? params.to : DEFAULT_PAGE_SIZE;
  }
}
