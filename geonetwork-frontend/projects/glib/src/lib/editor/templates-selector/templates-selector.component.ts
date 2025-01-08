import { Component, model } from '@angular/core';
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
import {
  SearchAggComponent,
  SearchAggLayout,
} from '../../search/search-agg/search-agg.component';

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
    SearchAggComponent,
  ],
  templateUrl: './templates-selector.component.html',
})
export class TemplatesSelectorComponent {
  template = model<string>();
  templateType = model<string>();

  protected readonly ResourceTypeLayout = ResourceTypeLayout;
  protected readonly SearchAggLayout = SearchAggLayout;
}
