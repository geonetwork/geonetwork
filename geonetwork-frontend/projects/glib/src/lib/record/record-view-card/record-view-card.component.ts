import { Component, input } from '@angular/core';
import { SearchHit } from '@elastic/elasticsearch/lib/api/types';
import { GnIndexRecord } from 'gapi';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CardModule } from 'primeng/card';
import { RecordFieldOverviewComponent } from '../record-field-overview/record-field-overview.component';
import { GJsonpathPipe } from '../../shared/g-jsonpath.pipe';
import { UrlPlaceholderPipe } from '../../shared/url-placeholder.pipe';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'g-record-view-card',
  templateUrl: './record-view-card.component.html',
  styleUrl: './record-view-card.component.css',
  standalone: true,
  imports: [
    DatePipe,
    RouterLink,
    CardModule,
    RecordFieldOverviewComponent,
    GJsonpathPipe,
    UrlPlaceholderPipe,
    ButtonModule,
  ],
})
export class RecordViewCardComponent {
  hit = input<SearchHit<GnIndexRecord>>();
  landingPage = input<string>();
  landingPageLinkPath = input<string>();
  landingPageLabel = input<string>('Read more');
  styleClass = input<string>('');
}
