import { Component, input } from '@angular/core';

@Component({
  selector: 'g-search-results-error',
  templateUrl: './search-results-error.component.html',
  standalone: true,
})
export class SearchResultsErrorComponent {
  error = input.required<Error>();
}
