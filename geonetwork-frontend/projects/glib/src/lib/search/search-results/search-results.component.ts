import { Component, HostAttributeToken, inject, OnInit } from '@angular/core';
import { SearchContextDirective } from '../search-context.directive';
import { SearchService } from '../search.service';
import { ButtonDirective } from 'primeng/button';
import { AsyncPipe, JsonPipe, NgForOf, NgIf } from '@angular/common';
import { SkeletonModule } from 'primeng/skeleton';
import { RouterLink } from '@angular/router';
import { BadgeModule } from 'primeng/badge';
import { ChipModule } from 'primeng/chip';
import { ImageModule } from 'primeng/image';
import { DataViewModule } from 'primeng/dataview';
import { PrimeTemplate } from 'primeng/api';
import { SearchStoreType } from '../search.state';

@Component({
  selector: 'g-search-results',
  standalone: true,
  imports: [
    SearchContextDirective,
    ButtonDirective,
    JsonPipe,
    SkeletonModule,
    RouterLink,
    BadgeModule,
    ChipModule,
    ImageModule,
    NgForOf,
    NgIf,
    AsyncPipe,
    DataViewModule,
    PrimeTemplate,
  ],
  templateUrl: './search-results.component.html',
  styleUrl: './search-results.component.css',
})
export class SearchResultsComponent implements OnInit {
  scope = inject(new HostAttributeToken('scope'));
  searchService = inject(SearchService);
  search: SearchStoreType;

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope);
  }
}
