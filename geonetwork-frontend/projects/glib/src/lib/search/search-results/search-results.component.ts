import { Component, ContentChild, input, TemplateRef } from '@angular/core';
import { SearchContextDirective } from '../search-context.directive';
import { ButtonDirective } from 'primeng/button';
import {
  AsyncPipe,
  DatePipe,
  JsonPipe,
  NgForOf,
  NgIf,
  NgTemplateOutlet,
} from '@angular/common';
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
import { RecordViewListComponent } from '../../record/record-view-list/record-view-list.component';
import { RecordViewCardComponent } from '../../record/record-view-card/record-view-card.component';

export enum ResultsLayout {
  LIST = 'list',
  CARD = 'card',
}

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
    RecordViewListComponent,
    RecordViewCardComponent,
    NgTemplateOutlet,
  ],
  providers: [DatePipe],
  templateUrl: './search-results.component.html',
  styleUrl: './search-results.component.css',
})
export class SearchResultsComponent extends SearchBaseComponent {
  protected readonly ResourceTypeLayout = ResourceTypeLayout;

  layout = input<ResultsLayout>(ResultsLayout.LIST);
  layoutClass = input<string>('');
  landingPage = input<string>('');
  landingPageLinkPath = input<string>();

  @ContentChild('resultsTemplate') resultsTemplate:
    | TemplateRef<any>
    | undefined;
  @ContentChild('searchProgressTemplate') searchProgressTemplate:
    | TemplateRef<any>
    | undefined;

  protected readonly ResultsLayout = ResultsLayout;
}
