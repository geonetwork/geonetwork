import { Component, input, Input, WritableSignal } from '@angular/core';
import { DropdownModule } from 'primeng/dropdown';
import { PrimeTemplate } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { IndexRecord } from 'g5api';
import { Select } from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { InplaceFieldComponent } from '../inplace-field/inplace-field.component';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'g-data-analysis-panel',
  standalone: true,
  imports: [
    DropdownModule,
    PrimeTemplate,
    TableModule,
    Select,
    FormsModule,
    InplaceFieldComponent,
    JsonPipe,
  ],
  templateUrl: './data-analysis-panel.component.html',
})
export class DataAnalysisPanelComponent {
  @Input() analysisResult!: WritableSignal<IndexRecord | undefined>;

  uuid = input.required<string>();

  resourceIdentifier = '';
}
