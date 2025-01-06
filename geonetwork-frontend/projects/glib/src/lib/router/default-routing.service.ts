import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { SearchFilter } from '../search/search.state.model';
import { SearchRouteService } from '../search/search-route.service';

@Injectable({
  providedIn: 'root',
})
export class DefaultRoutingService {
  router = inject(Router);
  searchRouteService = inject(SearchRouteService);

  constructor() {}

  search(filter: SearchFilter) {
    this.router.navigate(['/search'], {
      queryParams: this.searchRouteService.buildFilterQueryParams(filter),
    });
  }
}
