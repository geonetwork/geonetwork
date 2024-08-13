import { Component, inject, OnInit } from '@angular/core';
import { PanelMenuModule } from 'primeng/panelmenu';
import { Router, RouterOutlet } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AppModule } from './app.module';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [PanelMenuModule, AppModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  title = 'Webcomponents demo';

  router = inject(Router);
  items: MenuItem[];

  ngOnInit() {
    this.items = [
      {
        label: 'Webcomponents',
        icon: 'fa fa-code',
        expanded: true,
        items: [
          {
            label: 'Search table',
            icon: 'fa fa-search',
            command: () => {
              this.router.navigate([
                'webcomponents',
                'gc-search-results-table',
              ]);
            },
          },
          {
            label: 'Search results',
            icon: 'fa fa-list',
            command: () => {
              this.router.navigate(['webcomponents', 'gc-search-results']);
            },
          },
          {
            label: 'Data table',
            icon: 'fa fa-table',
            command: () => {
              this.router.navigate(['webcomponents', 'gc-data-results-table']);
            },
          },
        ],
      },
      {
        label: 'GeoNetwork AngularJS test',
        icon: 'fa fa-home',
        command: () => {
          this.router.navigate(['gn']);
        },
      },
    ];
  }
}
