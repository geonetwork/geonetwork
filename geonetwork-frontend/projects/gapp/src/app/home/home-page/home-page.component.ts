import { Component } from '@angular/core';
import {
  SearchAggComponent,
  SearchAggLayout,
  SearchContextDirective,
  SearchResultsTimelineComponent,
} from 'glib';

@Component({
  selector: 'gn-home-page',
  templateUrl: './home-page.component.html',
  standalone: true,
  styleUrl: './home-page.component.css',
  imports: [
    SearchContextDirective,
    SearchAggComponent,
    SearchResultsTimelineComponent,
  ],
})
export class HomePageComponent {
  protected readonly SearchAggLayout = SearchAggLayout;
}
