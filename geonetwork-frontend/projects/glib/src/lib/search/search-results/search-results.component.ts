import { Component, ContentChild, input, TemplateRef } from '@angular/core';
import { DatePipe, NgTemplateOutlet } from '@angular/common';
import { SkeletonModule } from 'primeng/skeleton';
import { BadgeModule } from 'primeng/badge';
import { ChipModule } from 'primeng/chip';
import { ImageModule } from 'primeng/image';
import { DataViewModule } from 'primeng/dataview';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { SearchModule } from '../search.module';
import { SearchResultsErrorComponent } from '../search-results-error/search-results-error.component';
import { ResourceTypeLayout } from '../../record/record-field-resource-type/record-field-resource-type.component';
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
    SkeletonModule,
    BadgeModule,
    ChipModule,
    ImageModule,
    DataViewModule,
    SearchModule,
    SearchResultsErrorComponent,
    RecordViewListComponent,
    RecordViewCardComponent,
    NgTemplateOutlet,
  ],
  providers: [DatePipe],
  templateUrl: './search-results.component.html',
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
