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
    icon: 'fa-cog',
  },
  map: {
    icon: 'fa-map',
  },
  featureCatalogue: {
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
  layout = input<ResourceTypeLayout>(ResourceTypeLayout.BADGE);
  styleClass = input<string>('');
  protected readonly resourceTypeLayout = ResourceTypeLayout;
  protected readonly resourceTypeIcons = ResourceTypeIcons;
}
