import {
  Component,
  computed,
  inject,
  input,
  OnInit,
  signal,
  TemplateRef,
} from '@angular/core';
import { JsonPipe, NgTemplateOutlet } from '@angular/common';
import { API_CONFIGURATION } from '../../config/config.loader';
import { GnIndexRecord, SearchApi } from 'gapi';
import { TabViewModule } from 'primeng/tabview';
import { RecordFieldOverviewComponent } from '../record-field-overview/record-field-overview.component';
import { RecordFieldKeywordsComponent } from '../record-field-keywords/record-field-keywords.component';
import { AccordionModule } from 'primeng/accordion';
import { RecordFieldLinksComponent } from '../record-field-links/record-field-links.component';
import { Fieldset } from 'primeng/fieldset';
import { RecordFieldResourceTypeComponent } from '../record-field-resource-type/record-field-resource-type.component';

@Component({
  selector: 'g-record-view',
  templateUrl: './record-view.component.html',
  standalone: true,
  imports: [
    NgTemplateOutlet,
    TabViewModule,
    RecordFieldOverviewComponent,
    RecordFieldKeywordsComponent,
    AccordionModule,
    RecordFieldLinksComponent,
    Fieldset,
    RecordFieldResourceTypeComponent,
    JsonPipe,
  ],
})
export class RecordViewComponent implements OnInit {
  uuid = input<string>();

  backButtonTplRef = input<TemplateRef<unknown>>();

  apiConfiguration = inject(API_CONFIGURATION);

  searchApi = computed(() => {
    return new SearchApi(this.apiConfiguration());
  });

  record = signal<GnIndexRecord | undefined>(undefined);

  recordStatus = signal<string | undefined>(undefined);

  ngOnInit() {
    if (!this.uuid()) return;

    this.searchApi()
      .search({
        body: {
          query: {
            term: {
              uuid: this.uuid() as string,
            },
          },
        },
      })
      .then(
        response => {
          if (response.hits.total === 0) {
            this.recordStatus.set('not-found-or-not-shared-with-you');
          } else {
            this.record.set(response.hits.hits[0]._source);
          }
        },
        error => {
          console.warn(error);
          this.recordStatus.set('not-found-or-not-shared-with-you');
        }
      );
  }

  protected readonly statusbar = statusbar;
}
