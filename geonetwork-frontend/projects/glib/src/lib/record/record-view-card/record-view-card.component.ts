import { Component, input } from '@angular/core';
import { elasticsearch } from 'gapi';
import { GnIndexRecord } from 'gapi';
import { DatePipe } from '@angular/common';
import { Button } from 'primeng/button';
import { RouterLink } from '@angular/router';
import { CardModule } from 'primeng/card';
import { RecordFieldOverviewComponent } from '../record-field-overview/record-field-overview.component';
import { GJsonpathPipe } from '../../shared/g-jsonpath.pipe';
import { UrlPlaceholderPipe } from '../../shared/url-placeholder.pipe';

@Component({
  selector: 'g-record-view-card',
  templateUrl: './record-view-card.component.html',
  styleUrl: './record-view-card.component.css',
  standalone: true,
  imports: [
    DatePipe,
    Button,
    RouterLink,
    CardModule,
    RecordFieldOverviewComponent,
    GJsonpathPipe,
    UrlPlaceholderPipe,
  ],
})
export class RecordViewCardComponent {
  hit = input<elasticsearch.SearchHit<GnIndexRecord>>();
  landingPage = input<string>();
  landingPageLinkPath = input<string>();
  landingPageLabel = input<string>('Read more');
  styleClass = input<string>('');
}
