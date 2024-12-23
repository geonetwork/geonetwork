import { Component } from '@angular/core';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { TimelineModule } from 'primeng/timeline';
import { CardModule } from 'primeng/card';
import { ImageModule } from 'primeng/image';
import { BadgeModule } from 'primeng/badge';
import {
  RecordFieldResourceTypeComponent,
  ResourceTypeLayout,
} from '../../record/record-field-resource-type/record-field-resource-type.component';
import { RecordModule } from '../../record/record.module';
import { RecordViewCardComponent } from '../../record/record-view-card/record-view-card.component';

@Component({
  selector: 'g-search-results-timeline',
  templateUrl: './search-results-timeline.component.html',
  standalone: true,
  styles: `
    p-timeline ::ng-deep .p-timeline-event-opposite {
      border: 1px solid #e0e0e0;
      display: none;
    }
  `,
  imports: [
    TimelineModule,
    CardModule,
    ImageModule,
    BadgeModule,
    RecordFieldResourceTypeComponent,
    RecordModule,
    RecordViewCardComponent,
  ],
})
export class SearchResultsTimelineComponent extends SearchBaseComponent {
  protected readonly ResourceTypeLayout = ResourceTypeLayout;
}
