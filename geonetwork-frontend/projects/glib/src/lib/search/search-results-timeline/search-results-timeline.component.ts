import { Component } from '@angular/core';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { TimelineModule } from 'primeng/timeline';
import { CardModule } from 'primeng/card';
import { Button } from 'primeng/button';
import { SearchResultsErrorComponent } from '../search-results-error/search-results-error.component';
import { ImageModule } from 'primeng/image';
import { NgIf } from '@angular/common';
import { BadgeModule } from 'primeng/badge';
import {
  RecordFieldResourceTypeComponent,
  ResourceTypeLayout,
} from '../../record/g-record-field-resource-type/record-field-resource-type.component';

@Component({
  selector: 'g-search-results-timeline',
  templateUrl: './search-results-timeline.component.html',
  standalone: true,
  styles: `
    p-timeline /deep/ .p-timeline-event-opposite {
      border: 1px solid #e0e0e0;
      display: none;
    }
  `,
  imports: [
    TimelineModule,
    CardModule,
    Button,
    SearchResultsErrorComponent,
    ImageModule,
    NgIf,
    BadgeModule,
    RecordFieldResourceTypeComponent,
  ],
})
export class SearchResultsTimelineComponent extends SearchBaseComponent {
  protected readonly ResourceTypeLayout = ResourceTypeLayout;
}
