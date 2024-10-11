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
  APPLICATION_CONFIGURATION, GeoNetworkTheme,
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
import { PrimeNGConfig } from 'primeng/api';
import { Aura } from 'primeng/themes/aura';
import { Nora } from 'primeng/themes/nora';
import { Lara } from 'primeng/themes/lara';
import { definePreset } from 'primeng/themes';


@Component({
  selector: 'app-root',
  standalone: true,
  providers: [SearchService],
  imports: [
    RouterOutlet,
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

  constructor(private config: PrimeNGConfig) {
    this.config.theme.set({
      preset: GeoNetworkTheme,
      options: {
        cssLayer: {
          name: 'primeng',
          order: 'tailwind-base, primeng, tailwind-utilities',
        },
      },
    });
  }

  protected readonly SearchAggLayout = SearchAggLayout;
}
