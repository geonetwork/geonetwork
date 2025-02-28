import { Component, effect, input, model, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ProgressBarModule } from 'primeng/progressbar';
import { SelectButtonModule } from 'primeng/selectbutton';
import {
  RecordFieldResourceTypeComponent,
  ResourceTypeLayout,
} from '../../record/record-field-resource-type/record-field-resource-type.component';
import { SearchContextDirective } from '../../search/search-context.directive';
import { SearchResultsComponent } from '../../search/search-results/search-results.component';
import { SearchAggLayout } from '../../search/search-agg/search-agg.component';
import { GnIndexRecord } from 'gapi';
import { estypes } from '@elastic/elasticsearch';

@Component({
  selector: 'g-templates-selector',
  standalone: true,
  imports: [
    FormsModule,
    InputTextModule,
    ProgressBarModule,
    RecordFieldResourceTypeComponent,
    SearchContextDirective,
    SearchResultsComponent,
    SelectButtonModule,
  ],
  templateUrl: './templates-selector.component.html',
})
export class TemplatesSelectorComponent {
  template = model<string>();
  templateType = model<string>();

  filter = input('');
  response = signal<estypes.SearchResponse<GnIndexRecord> | null>(null);

  numberOfTemplates = model<number>();

  protected readonly ResourceTypeLayout = ResourceTypeLayout;
  protected readonly SearchAggLayout = SearchAggLayout;

  constructor() {
    effect(() => {
      let total = this.response()?.hits.total as estypes.SearchTotalHits;
      if (total) {
        this.numberOfTemplates.set(total.value);
      }
    });
  }
}
