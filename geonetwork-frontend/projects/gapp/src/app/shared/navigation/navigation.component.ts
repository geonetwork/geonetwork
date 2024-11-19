import { Component, inject, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { DropdownChangeEvent, DropdownModule } from 'primeng/dropdown';
import { LanguagesLoaderDirective, SignInFormComponent } from 'glib';
import { SpeedDialModule } from 'primeng/speeddial';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { ChipModule } from 'primeng/chip';
import { AppStore } from 'glib';

@Component({
  selector: 'gn-navigation',
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.css',
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
    SignInFormComponent,
  ],
})
export class NavigationComponent implements OnInit {
  items: MenuItem[];
  readonly app = inject(AppStore);

  router = inject(Router);

  ngOnInit() {
    this.items = [
      {
        label: 'Home',
        icon: 'fa fa-home',
        command: () => {
          this.router.navigate(['home']);
        },
      },
      {
        label: 'Search',
        icon: 'fa fa-search',
        command: () => {
          this.router.navigate(['search']);
        },
      },
      {
        label: 'Map',
        icon: 'fa fa-map',
        command: () => {
          this.router.navigate(['map'], { fragment: '/map' });
        },
      },
      {
        label: 'Data upload',
        icon: 'fa fa-upload',
        command: () => {
          this.router.navigate(['dataupload']);
        },
      },
      // GN5 signin
      // {
      //   label: 'Sign in',
      //   icon: 'fa fa-sign-in',
      //   url: '/signin',
      // },
    ];
  }

  setLanguage(event: DropdownChangeEvent) {
    this.app.setLanguage(event.value.code);
  }
}
