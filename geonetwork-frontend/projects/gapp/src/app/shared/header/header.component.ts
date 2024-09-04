import { Component } from '@angular/core';
import { DropdownModule } from 'primeng/dropdown';
import { LanguagesLoaderDirective } from 'glib';

@Component({
  selector: 'gn-header',
  templateUrl: './header.component.html',
  standalone: true,
  styleUrl: './header.component.css',
  imports: [DropdownModule, LanguagesLoaderDirective],
})
export class HeaderComponent {}
