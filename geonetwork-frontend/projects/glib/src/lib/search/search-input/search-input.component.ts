import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SearchBaseComponent } from '../search-base/search-base.component';

@Component({
  selector: 'g-search-input',
  standalone: true,
  imports: [FormsModule, InputTextModule, FormsModule],
  templateUrl: './search-input.component.html',
  styleUrl: './search-input.component.css',
})
export class SearchInputComponent extends SearchBaseComponent {
}
