import {
  AfterViewInit,
  Component,
  computed,
  CUSTOM_ELEMENTS_SCHEMA,
  ElementRef,
  inject,
  OnInit,
  signal,
} from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import {
  API_CONFIGURATION,
  APPLICATION_CONFIGURATION,
  buildGn4BaseUrl,
  SearchAggLayout,
  SearchContextDirective,
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
import { filter, of } from 'rxjs';
import { environment } from '../environments/environment';
import { Drawer } from 'primeng/drawer';
import { Menu } from 'primeng/menu';
import { AppMenuComponent } from './shared/app-menu/app-menu.component';
import { Menubar } from 'primeng/menubar';
import { NgOptimizedImage } from '@angular/common';
import { Button } from 'primeng/button';
import { Avatar } from 'primeng/avatar';
import { CatalogAvatarComponent } from './shared/catalog-avatar/catalog-avatar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  providers: [SearchService],
  imports: [
    RouterOutlet,
    InputGroupModule,
    InputGroupAddonModule,
    InputTextModule,
    SearchContextDirective,
    FormsModule,
    ScrollTopModule,
    SidebarModule,
    HeaderComponent,
    NavigationComponent,
    Drawer,
    Menu,
    AppMenuComponent,
    Menubar,
    NgOptimizedImage,
    Button,
    Avatar,
    CatalogAvatarComponent,
  ],
  templateUrl: './app.component.html',
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
        this.isMapRoute.set(event.url.includes('/map'));
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
