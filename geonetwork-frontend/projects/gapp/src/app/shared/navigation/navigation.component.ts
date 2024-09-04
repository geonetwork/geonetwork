import { Component, inject, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { DropdownChangeEvent, DropdownModule } from 'primeng/dropdown';
import { LanguagesLoaderDirective, SearchStore } from 'glib';
import { SpeedDialModule } from 'primeng/speeddial';
import { AppStore } from '../../app.state';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { HttpClient, provideHttpClient } from '@angular/common/http';
import { OverlayPanelModule } from 'primeng/overlaypanel';

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
  ],
})
export class NavigationComponent implements OnInit {
  items: MenuItem[];
  readonly appStore = inject(AppStore);

  router = inject(Router);
  private http = inject(HttpClient);

  username: string = '';
  password: string = '';

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
        label: 'Sign in',
        icon: 'fa fa-sign-in',
        url: '/signin',
      },
    ];
  }

  setLanguage(event: DropdownChangeEvent) {
    this.appStore.setLanguage(event.value.code);
  }

  signin() {
    this.http
      .post('http://localhost:8080/geonetwork/signin', {
        username: this.username,
        password: this.password,
      })
      .subscribe(response => {
        console.log(response);
      });
  }
}
