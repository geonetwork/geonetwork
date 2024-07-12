import { Component, HostAttributeToken, inject, OnInit } from '@angular/core';
import { SearchService } from '../search.service';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SearchStoreType } from '../search.state';

@Component({
  selector: 'g-search-input',
  standalone: true,
  imports: [FormsModule, InputTextModule, FormsModule],
  templateUrl: './search-input.component.html',
  styleUrl: './search-input.component.css',
})
export class SearchInputComponent implements OnInit {
  scope = inject(new HostAttributeToken('scope'));
  searchService = inject(SearchService);
  search: SearchStoreType;

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope);
  }
}
