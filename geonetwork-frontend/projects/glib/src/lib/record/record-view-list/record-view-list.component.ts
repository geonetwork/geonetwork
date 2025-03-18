import { Component, inject, input } from '@angular/core';
import { ChipModule } from 'primeng/chip';
import { elasticsearch } from 'gapi';
import { RecordFieldOverviewComponent } from '../record-field-overview/record-field-overview.component';
import {
  RecordFieldResourceTypeComponent,
  ResourceTypeLayout,
} from '../record-field-resource-type/record-field-resource-type.component';
import { RecordFieldRange } from '../record-field-range.pipe';
import { RouterLink } from '@angular/router';
import { FirstSentencePipe } from '../../shared/first-sentence.pipe';
import { RecordFieldKeywordsComponent } from '../record-field-keywords/record-field-keywords.component';
import { RecordFieldLinksButtonComponent } from '../record-field-links-button/record-field-links-button.component';
import { IndexRecord } from 'g5api';

@Component({
  selector: 'g-record-view-list',
  standalone: true,
  imports: [
    ChipModule,
    RecordFieldOverviewComponent,
    RouterLink,
    RecordFieldResourceTypeComponent,
    RecordFieldRange,
    FirstSentencePipe,
    RecordFieldKeywordsComponent,
    RecordFieldLinksButtonComponent,
  ],
  templateUrl: './record-view-list.component.html',
})
export class RecordViewListComponent {
  protected readonly ResourceTypeLayout = ResourceTypeLayout;
  hit = input<elasticsearch.SearchHit<IndexRecord>>();
  landingPage = input<string>('');
  landingPageLinkOn = input<string>();
  landingPageLabel = input<string>('Read more');
}
