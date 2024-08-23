import { Component, input } from '@angular/core';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { TimelineModule } from 'primeng/timeline';
import { CardModule } from 'primeng/card';
import { Button } from 'primeng/button';
import { SearchResultsErrorComponent } from '../search-results-error/search-results-error.component';
import { ImageModule } from 'primeng/image';
import { DatePipe, NgIf } from '@angular/common';
import { BadgeModule } from 'primeng/badge';
import {
  RecordFieldResourceTypeComponent,
  ResourceTypeLayout,
} from '../../record/record-field-resource-type/record-field-resource-type.component';
import { RecordFieldOverviewComponent } from '../../record/record-field-overview/record-field-overview.component';
import { RouterLink } from '@angular/router';
import { RecordModule } from '../../record/record.module';
import { RecordViewCardComponent } from '../../record/record-view-card/record-view-card.component';
import { CarouselModule } from 'primeng/carousel';

@Component({
  selector: 'g-search-results-carousel',
  templateUrl: './search-results-carousel.component.html',
  standalone: true,
  imports: [
    CardModule,
    Button,
    SearchResultsErrorComponent,
    ImageModule,
    NgIf,
    BadgeModule,
    RecordFieldResourceTypeComponent,
    RecordFieldOverviewComponent,
    DatePipe,
    RouterLink,
    RecordModule,
    RecordViewCardComponent,
    CarouselModule,
  ],
})
export class SearchResultsCarouselComponent extends SearchBaseComponent {
  numVisible = input(3);
  numScroll = input(3);
  protected readonly ResourceTypeLayout = ResourceTypeLayout;
}
