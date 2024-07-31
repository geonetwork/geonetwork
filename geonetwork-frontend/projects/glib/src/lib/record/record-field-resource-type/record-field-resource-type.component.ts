import { Component, input } from '@angular/core';
import { BadgeModule } from 'primeng/badge';
import { JsonPipe } from '@angular/common';

export enum ResourceTypeLayout {
  BADGE = 'badge',
  ICON_WITH_BADGE = 'icon_with_badge',
  ICON = 'icon',
}

export const ResourceTypeIcons = {
  dataset: {
    icon: 'fa-globe',
    // stack: [
    //   {
    //     icon: 'fa-database',
    //     transform: 'shrink-8 up-6', Require SVG ?
    //   },
    //   { icon: 'fa-globe' },
    // ],
  },
  nonGeographicDataset: {
    icon: 'fa-chart-column',
  },
  series: {
    icon: 'fa-copy',
  },
  service: {
    icon: 'fa-cloud',
  },
  map: {
    icon: 'fa-map',
  },
  featureCatalog: {
    icon: 'fa-table',
  },
  publication: {
    icon: 'fa-book',
  },
  document: {
    icon: 'fa-file',
  },
};

type ResourceTypeLayoutValues = keyof typeof ResourceTypeLayout;

@Component({
  selector: 'g-record-field-resource-type',
  templateUrl: './record-field-resource-type.component.html',
  styleUrl: './record-field-resource-type.component.css',
  standalone: true,
  imports: [BadgeModule, JsonPipe],
})
export class RecordFieldResourceTypeComponent {
  field = input<string[] | null>();
  styleClass = input<string>('');
  layout = input<ResourceTypeLayout>(ResourceTypeLayout.BADGE);
  protected readonly resourceTypeLayout = ResourceTypeLayout;
  protected readonly resourceTypeIcons = ResourceTypeIcons;
}
