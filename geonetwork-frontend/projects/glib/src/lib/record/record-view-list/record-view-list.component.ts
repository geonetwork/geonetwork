import { Component, input } from '@angular/core';
import { ChipModule } from 'primeng/chip';
import { elasticsearch } from 'gapi';
import { GnIndexRecord } from 'gapi';
import { RecordFieldOverviewComponent } from '../record-field-overview/record-field-overview.component';
import {
  RecordFieldResourceTypeComponent,
  ResourceTypeLayout,
} from '../record-field-resource-type/record-field-resource-type.component';
import { RecordFieldRange } from '../record-field-range.pipe';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'g-record-view-list',
  standalone: true,
  imports: [
    ChipModule,
    RecordFieldOverviewComponent,
    RouterLink,
    RecordFieldResourceTypeComponent,
    RecordFieldRange,
  ],
  templateUrl: './record-view-list.component.html',
  styleUrl: './record-view-list.component.css',
})
export class RecordViewListComponent {
  protected readonly ResourceTypeLayout = ResourceTypeLayout;
  hit = input<elasticsearch.SearchHit<GnIndexRecord>>();
  landingPage = input<string>('');
  landingPageLinkOn = input<string>();
  landingPageLabel = input<string>('Read more');
}
