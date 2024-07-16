import { Component, input, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'g-data-results-table',
  templateUrl: './g-data-results-table.component.html',
  styleUrl: './g-data-results-table.component.css',
  encapsulation: ViewEncapsulation.ShadowDom,
})
export class GDataResultsTableComponent {
  source = input<string>();
}
