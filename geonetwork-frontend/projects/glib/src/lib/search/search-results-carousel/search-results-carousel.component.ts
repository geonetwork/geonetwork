import { Component, input } from '@angular/core';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { CardModule } from 'primeng/card';
import { SearchResultsErrorComponent } from '../search-results-error/search-results-error.component';
import { ImageModule } from 'primeng/image';
import { BadgeModule } from 'primeng/badge';
import { ResourceTypeLayout } from '../../record/record-field-resource-type/record-field-resource-type.component';
import { RecordModule } from '../../record/record.module';
import { RecordViewCardComponent } from '../../record/record-view-card/record-view-card.component';
import { CarouselModule } from 'primeng/carousel';

@Component({
  selector: 'g-search-results-carousel',
  templateUrl: './search-results-carousel.component.html',
  standalone: true,
  imports: [
    CardModule,
    SearchResultsErrorComponent,
    ImageModule,
    BadgeModule,
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
