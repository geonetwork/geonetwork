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
import { HeaderComponent } from './shared/header/header.component';
import { NavigationComponent } from './shared/navigation/navigation.component';

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
    HeaderComponent,
    NavigationComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'GeoNetwork';

  uiConfiguration = inject(APPLICATION_CONFIGURATION).ui;

  scoreConfig = this.uiConfiguration?.mods.search.scoreConfig;
  pageSize = this.uiConfiguration?.mods.search.paginationInfo.hitsPerPage;
  aggregationConfig = this.uiConfiguration?.mods.search.facetConfig;

  constructor() {}

  protected readonly SearchAggLayout = SearchAggLayout;
}
