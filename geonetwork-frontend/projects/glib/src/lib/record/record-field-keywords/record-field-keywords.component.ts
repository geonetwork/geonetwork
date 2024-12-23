import { Component, input } from '@angular/core';
import { ChipModule } from 'primeng/chip';

@Component({
  selector: 'g-record-field-keywords',
  templateUrl: './record-field-keywords.component.html',
  standalone: true,
  imports: [ChipModule],
})
export class RecordFieldKeywordsComponent {
  field = input<any[] | null>();
  limit = input<number | undefined>();
}
