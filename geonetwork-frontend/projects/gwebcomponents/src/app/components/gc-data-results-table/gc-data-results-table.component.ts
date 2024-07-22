import { Component, input, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'gc-data-results-table',
  templateUrl: './gc-data-results-table.component.html',
  styleUrl: './gc-data-results-table.component.css',
  encapsulation: ViewEncapsulation.ShadowDom,
})
export class GcDataResultsTableComponent {
  source = input<string>();
}
