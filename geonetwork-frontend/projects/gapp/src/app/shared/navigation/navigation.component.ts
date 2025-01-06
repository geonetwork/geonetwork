import { Component, inject, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { DropdownChangeEvent, DropdownModule } from 'primeng/dropdown';
import {
  AppStore,
  LanguagesLoaderDirective,
  SearchInputComponent,
  SignInStatusMenuComponent,
} from 'glib';
import { SpeedDialModule } from 'primeng/speeddial';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { ChipModule } from 'primeng/chip';
import { Select } from 'primeng/select';
import { Button } from 'primeng/button';
import { Popover } from 'primeng/popover';
import { Drawer } from 'primeng/drawer';
import { AppMenuComponent } from '../app-menu/app-menu.component';
import { CatalogAvatarComponent } from '../catalog-avatar/catalog-avatar.component';

@Component({
  selector: 'gn-navigation',
  templateUrl: './navigation.component.html',
  standalone: true,
  imports: [
    MenubarModule,
    DropdownModule,
    SpeedDialModule,
    LanguagesLoaderDirective,
    InputGroupModule,
    InputGroupAddonModule,
    InputTextModule,
    FormsModule,
    OverlayPanelModule,
    ChipModule,
    Select,
    SignInStatusMenuComponent,
    Button,
    Popover,
    Drawer,
    AppMenuComponent,
    SearchInputComponent,
    CatalogAvatarComponent,
  ],
})
export class NavigationComponent implements OnInit {
  readonly app = inject(AppStore);

  router = inject(Router);

  ngOnInit() {}

  setLanguage(event: DropdownChangeEvent) {
    this.app.setLanguage(event.value.code);
  }

  darkMode = signal(false);

  primaryColor = signal('#007bff');

  toggleTheme() {
    const element = document.querySelector('html');
    element!.classList.toggle('gn-app-dark');
    this.darkMode.set(element!.classList.contains('gn-app-dark'));
  }

  setRouteToSearch(searchText: string) {
    if (this.router.url === '/search') {
      return;
    }
    this.router.navigate(['/search']);
  }
}
