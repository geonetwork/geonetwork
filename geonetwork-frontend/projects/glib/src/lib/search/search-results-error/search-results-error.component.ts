import { Component, input } from '@angular/core';

@Component({
  selector: 'g-search-results-error',
  templateUrl: './search-results-error.component.html',
  styleUrl: './search-results-error.component.css',
  standalone: true,
})
export class SearchResultsErrorComponent {
  error = input.required<Error>();
}
