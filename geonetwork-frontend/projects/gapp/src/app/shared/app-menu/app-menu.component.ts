import { Component, computed, inject, input, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { Menu } from 'primeng/menu';
import { APPLICATION_CONFIGURATION, AppStore } from 'glib';

@Component({
  selector: 'gn-app-menu',
  imports: [Menu],
  templateUrl: './app-menu.component.html',
  styles: [
    `
      p-menu.gn-icon-menu ::ng-deep,
      p-menu.gn-icon-menu ::ng-deep .p-menu {
        min-width: unset;
        width: 130px;
        height: 100%;
        border-bottom: none;
        border-top: none;
        border-radius: 0;
      }

      p-menu.gn-icon-menu ::ng-deep .p-menu-item-link {
        flex-direction: column;
        align-items: center;
        text-align: center;
      }

      p-menu.gn-icon-menu ::ng-deep .p-menu-list {
        align-items: center;
      }

      p-menu.gn-icon-menu ::ng-deep .p-menu-separator {
        width: 100%;
      }
    `,
  ],
})
export class AppMenuComponent implements OnInit {
  iconMode = input(false);

  readonly app = inject(AppStore);

  uiConfiguration = inject(APPLICATION_CONFIGURATION).ui;

  items = computed(() => {
    const menuItems: { [key: string]: MenuItem } = {
      home: {
        label: 'Discover',
        icon: 'fa-compass',
        iconClass: this.iconClass(),
        routerLink: 'home',
      },
      search: {
        label: 'Search',
        icon: 'fa-search',
        iconClass: this.iconClass(),
        routerLink: 'search',
      },
      map: {
        label: 'New map',
        icon: 'fa-map',
        iconClass: this.iconClass(),
        visible: this.uiConfiguration?.mods?.map?.enabled || false,
        routerLink: 'map',
      },
      editorSeparator: {
        separator: true,
        visible: this.app.authenticated(),
      },
      editor: {
        label: 'New record',
        icon: 'fa-plus',
        iconClass: this.iconClass(),
        visible: this.app.authenticated(),
        routerLink: 'new-record',
      },
      adminSeparator: {
        separator: true,
        visible: this.app.authenticated(),
      },
      admin: {
        label: 'Admin console',
        icon: 'fa-cogs',
        iconClass: this.iconClass(),
        visible: this.app.authenticated(),
        url: '/geonetwork/srv/eng/admin.console',
      },
    };
    const newItems: MenuItem[] = [];

    Object.keys(menuItems).forEach(key => {
      if (
        menuItems[key].separator ||
        (this.uiConfiguration?.mods &&
          (this.uiConfiguration?.mods as any)[key]?.enabled)
      ) {
        newItems.push(menuItems[key]);
      }
    });

    return newItems;
  });

  constructor(private router: Router) {}

  ngOnInit(): void {}

  private iconClass() {
    return this.iconMode() ? 'fa fa-2x' : 'fa';
  }
}
