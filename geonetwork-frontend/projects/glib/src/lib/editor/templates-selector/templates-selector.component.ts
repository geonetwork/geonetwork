import { Component, Input, WritableSignal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PrimeTemplate } from 'primeng/api';
import { ProgressBarModule } from 'primeng/progressbar';
import { SelectButtonModule } from 'primeng/selectbutton';
import {
  RecordFieldResourceTypeComponent,
  ResourceTypeLayout,
} from '../../record/record-field-resource-type/record-field-resource-type.component';
import { SearchContextDirective } from '../../search/search-context.directive';
import { SearchResultsComponent } from '../../search/search-results/search-results.component';

@Component({
  selector: 'g-templates-selector',
  standalone: true,
  imports: [
    FormsModule,
    InputTextModule,
    PrimeTemplate,
    ProgressBarModule,
    RecordFieldResourceTypeComponent,
    SearchContextDirective,
    SearchResultsComponent,
    SelectButtonModule,
  ],
  templateUrl: './templates-selector.component.html',
  styleUrl: './templates-selector.component.css',
})
export class TemplatesSelectorComponent {
  @Input() template!: WritableSignal<string>;

  protected readonly ResourceTypeLayout = ResourceTypeLayout;
}
