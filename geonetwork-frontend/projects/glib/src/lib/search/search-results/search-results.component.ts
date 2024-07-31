import { Component, input } from '@angular/core';
import { SearchContextDirective } from '../search-context.directive';
import { ButtonDirective } from 'primeng/button';
import { AsyncPipe, DatePipe, JsonPipe, NgForOf, NgIf } from '@angular/common';
import { SkeletonModule } from 'primeng/skeleton';
import { RouterLink } from '@angular/router';
import { BadgeModule } from 'primeng/badge';
import { ChipModule } from 'primeng/chip';
import { ImageModule } from 'primeng/image';
import { DataViewModule } from 'primeng/dataview';
import { PrimeTemplate } from 'primeng/api';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { SearchModule } from '../search.module';
import { SearchResultsErrorComponent } from '../search-results-error/search-results-error.component';
import {
  RecordFieldResourceTypeComponent,
  ResourceTypeLayout,
} from '../../record/record-field-resource-type/record-field-resource-type.component';
import { RecordFieldOverviewComponent } from '../../record/record-field-overview/record-field-overview.component';
import { RecordFieldRange } from '../../record/record-field-range.pipe';
import { FirstSentencePipe } from '../../shared/first-sentence.pipe';

@Component({
  selector: 'g-search-results',
  standalone: true,
  imports: [
    SearchContextDirective,
    ButtonDirective,
    JsonPipe,
    SkeletonModule,
    RouterLink,
    BadgeModule,
    ChipModule,
    ImageModule,
    NgForOf,
    NgIf,
    AsyncPipe,
    DataViewModule,
    PrimeTemplate,
    SearchModule,
    SearchResultsErrorComponent,
    RecordFieldResourceTypeComponent,
    RecordFieldOverviewComponent,
    RecordFieldRange,
    FirstSentencePipe,
  ],
  providers: [DatePipe],
  templateUrl: './search-results.component.html',
  styleUrl: './search-results.component.css',
})
export class SearchResultsComponent extends SearchBaseComponent {
  protected readonly ResourceTypeLayout = ResourceTypeLayout;
}
