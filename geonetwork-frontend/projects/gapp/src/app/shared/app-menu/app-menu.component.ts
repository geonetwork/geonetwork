import { Component, ElementRef, inject, input, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { Menu } from 'primeng/menu';
import { APPLICATION_CONFIGURATION } from 'glib';
import { Ripple } from 'primeng/ripple';

@Component({
  selector: 'gn-app-menu',
  imports: [Menu, Ripple],
  templateUrl: './app-menu.component.html',
  styles: [
    `
      p-menu ::ng-deep,
      p-menu ::ng-deep .p-menu {
        min-width: unset;
      }
    `,
  ],
})
export class AppMenuComponent implements OnInit {
  iconMode = input(true);

  uiConfiguration = inject(APPLICATION_CONFIGURATION).ui;

  items: MenuItem[];

  listOfMenu: { [key: string]: MenuItem } = {
    // home: {
    //   label: 'Home',
    //   icon: 'fa fa-home',
    //   command: () => {
    //     this.router.navigate(['home']);
    //   },
    // },
    search: {
      label: 'Search',
      icon: 'fa fa-search',
      command: () => {
        this.router.navigate(['search']);
      },
    },
    map: {
      label: 'New map',
      icon: 'fa fa-map',
      command: () => {
        this.router.navigate(['map']);
      },
    },
    editor: {
      label: 'New record',
      icon: 'fa fa-plus',
      command: () => {
        this.router.navigate(['new-record']);
      },
    },
    // admin: {
    //   label: 'Admin console',
    //   icon: 'fa fa-cogs',
    //   command: () => {
    //     location.replace('/geonetwork/srv/eng/admin.console');
    //   },
    // }
  };

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.items = [];

    Object.keys(this.listOfMenu).forEach(key => {
      if (
        this.uiConfiguration?.mods &&
        (this.uiConfiguration?.mods as any)[key].enabled
      ) {
        this.items.push(this.listOfMenu[key]);
      }
    });
  }
}
