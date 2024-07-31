import { Component, input } from '@angular/core';

@Component({
  selector: 'g-record-field-links',
  templateUrl: './record-field-links.component.html',
  styleUrl: './record-field-links.component.css',
  standalone: true,
})
export class RecordFieldLinksComponent {
  field = input<any[] | null>();
}
