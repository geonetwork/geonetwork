import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { DataTableComponent } from 'glib';
import { AppModule } from './app.module';
import { SelectButtonModule } from 'primeng/selectbutton';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { TabViewModule } from 'primeng/tabview';
import { TabMenuModule } from 'primeng/tabmenu';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    DataTableComponent,
    AppModule,
    SelectButtonModule,
    DropdownModule,
    FormsModule,
    RouterLink,
    TabViewModule,
    TabMenuModule,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'Webcomponents demo';

  router = inject(Router);

  tabs = [
    {
      label: 'Webcomponents',
      icon: 'fa fa-code',
      command: () => {
        this.router.navigate(['webcomponents']);
      },
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
