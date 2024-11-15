import {
  AfterViewInit,
  Component,
  CUSTOM_ELEMENTS_SCHEMA,
  ElementRef,
  inject,
  OnInit,
  signal,
} from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { Button, ButtonDirective } from 'primeng/button';
import {
  APPLICATION_CONFIGURATION,
  buildGn4BaseUrl,
  GlibComponent,
  SearchAggComponent,
  SearchAggLayout,
  SearchAggsContainerComponent,
  SearchContextDirective,
  SearchInputComponent,
  SearchPagingComponent,
  SearchPagingMoreButtonComponent,
  SearchQueryResetDirective,
  SearchQuerySetterDirective,
  SearchResultsComponent,
  SearchResultsTimelineComponent,
  SearchService,
} from 'glib';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { ScrollTopModule } from 'primeng/scrolltop';
import { SidebarModule } from 'primeng/sidebar';
import { HeaderComponent } from './shared/header/header.component';
import { NavigationComponent } from './shared/navigation/navigation.component';
import { filter } from 'rxjs';
import { environment } from '../environments/environment';

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
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppComponent implements OnInit, AfterViewInit {
  title = 'GeoNetwork';

  isMapRoute = signal(false);

  uiConfiguration = inject(APPLICATION_CONFIGURATION).ui;

  scoreConfig = this.uiConfiguration?.mods.search.scoreConfig;
  pageSize = this.uiConfiguration?.mods.search.paginationInfo.hitsPerPage;
  aggregationConfig = this.uiConfiguration?.mods.search.facetConfig;

  gn4baseUrl = buildGn4BaseUrl(environment.baseUrl);

  constructor(
    private router: Router,
    private el: ElementRef
  ) {}

  ngOnInit(): void {
    this.router.events
      .pipe(
        filter(
          (event): event is NavigationEnd => event instanceof NavigationEnd
        )
      )
      .subscribe((event: NavigationEnd) => {
        this.isMapRoute.set(event.url.includes('map'));
      });
  }

  ngAfterViewInit() {
    var s = document.createElement('script');
    s.type = 'text/javascript';
    s.src = this.gn4baseUrl + '/catalog/webcomponents/webcomponents.js';
    this.el.nativeElement.appendChild(s);
  }

  protected readonly SearchAggLayout = SearchAggLayout;
}
