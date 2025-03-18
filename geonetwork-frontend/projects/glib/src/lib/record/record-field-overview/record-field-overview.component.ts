import { Component, computed, input, signal } from '@angular/core';
import { ImageModule } from 'primeng/image';
import { GalleriaModule } from 'primeng/galleria';
import { Overview } from 'g5api';

@Component({
  selector: 'g-record-field-overview',
  templateUrl: './record-field-overview.component.html',
  standalone: true,
  imports: [ImageModule, GalleriaModule],
})
export class RecordFieldOverviewComponent {
  field = input<Overview | Overview[] | null>();
  overviewList = computed<Overview[]>(() => {
    const field = this.field();
    if (!field) {
      return [];
    } else if (Array.isArray(field)) {
      return field;
    } else {
      return [field];
    }
  });
  styleClass = input<string>('');
  preview = input<boolean>(true);
}
