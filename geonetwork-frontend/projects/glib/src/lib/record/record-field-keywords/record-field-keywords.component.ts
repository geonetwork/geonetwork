import { Component, input } from '@angular/core';
import { ChipModule } from 'primeng/chip';

@Component({
  selector: 'g-record-field-keywords',
  templateUrl: './record-field-keywords.component.html',
  styleUrl: './record-field-keywords.component.css',
  standalone: true,
  imports: [ChipModule],
})
export class RecordFieldKeywordsComponent {
  field = input<any[] | null>();
}
