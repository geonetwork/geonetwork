import { inject, Pipe, PipeTransform } from '@angular/core';
import { RecordLinkService } from './record-link.service';

@Pipe({
  name: 'gRecordLinkActionIcon',
  standalone: true,
})
export class RecordLinkActionIconPipe implements PipeTransform {
  distributionService = inject(RecordLinkService);

  transform(actionType: string): string {
    if (!actionType) {
      return '';
    }
    return this.distributionService.getLinkActionIcon(actionType);
  }
}
