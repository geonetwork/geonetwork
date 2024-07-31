import { Component, input } from '@angular/core';
import { ImageModule } from 'primeng/image';

@Component({
  selector: 'g-record-field-overview',
  templateUrl: './record-field-overview.component.html',
  styleUrl: './record-field-overview.component.css',
  standalone: true,
  imports: [ImageModule],
})
export class RecordFieldOverviewComponent extends ImageModule {
  field = input<any[] | null>();
  styleClass = input<string>('');
  preview = input<boolean>(false);
}
