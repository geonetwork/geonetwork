import { Component, Input, signal, ViewEncapsulation } from '@angular/core';
import { DefaultConfig } from 'gapi';
import {
  API_CONFIGURATION,
  ResultsLayout,
  SearchAggLayout,
  SearchService,
} from 'glib';
import { GcBaseSearchComponent } from '../gc-base-search-component';

@Component({
  selector: 'gc-search',
  templateUrl: './gc-search.component.html',
  styleUrl: './gc-search.component.css',
  encapsulation: ViewEncapsulation.ShadowDom,
  providers: [
    { provide: API_CONFIGURATION, useValue: signal(DefaultConfig) },
    SearchService,
  ],
})
export class GcSearchComponent extends GcBaseSearchComponent {
  @Input() layout = ResultsLayout.CARD;
  @Input() layoutClass = 'grid grid-cols-3 gap-4 grid-flow-row';
  @Input() landingPageLinkPath: string;

  protected readonly SearchAggLayout = SearchAggLayout;
}
