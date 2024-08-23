import { Component, inject, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';

@Component({
  selector: 'gn-navigation',
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.css',
  standalone: true,
  imports: [MenubarModule],
})
export class NavigationComponent implements OnInit {
  items: MenuItem[];

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
        label: 'Sign in',
        icon: 'fa fa-sign-in',
        url: '/signin',
      },
    ];
  }
}
