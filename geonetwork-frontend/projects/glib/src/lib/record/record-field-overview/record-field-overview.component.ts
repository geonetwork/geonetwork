import { Component, computed, input } from '@angular/core';
import { ImageModule } from 'primeng/image';
import { GalleriaModule } from 'primeng/galleria';
import { GnOverview } from 'gapi';

@Component({
  selector: 'g-record-field-overview',
  templateUrl: './record-field-overview.component.html',
  standalone: true,
  imports: [ImageModule, GalleriaModule],
})
export class RecordFieldOverviewComponent {
  field = input<GnOverview | GnOverview[] | null>();
  overviewList = computed<GnOverview[]>(() => {
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
