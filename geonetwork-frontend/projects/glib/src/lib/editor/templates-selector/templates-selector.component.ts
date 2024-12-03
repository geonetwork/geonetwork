import { Component, Input, WritableSignal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PrimeTemplate } from 'primeng/api';
import { ProgressBarModule } from 'primeng/progressbar';
import {
  RecordFieldResourceTypeComponent,
  ResourceTypeLayout,
  SearchContextDirective,
  SearchResultsComponent,
} from 'glib';
import { SelectButtonModule } from 'primeng/selectbutton';

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
