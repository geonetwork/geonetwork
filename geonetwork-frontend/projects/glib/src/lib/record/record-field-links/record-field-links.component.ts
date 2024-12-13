import { Component, computed, inject, input } from '@angular/core';
import { GnLink } from 'gapi';
import { ConfigService } from '../../shared/config.service';
import { JsonPipe, KeyValuePipe } from '@angular/common';
import { Button } from 'primeng/button';
import { Router } from '@angular/router';
import { APPLICATION_CONFIGURATION } from '../../config/config.loader';
import { BadgeModule } from 'primeng/badge';
import { Gn4MapService } from '../../shared/gn4-map.service';

@Component({
  selector: 'g-record-field-links',
  templateUrl: './record-field-links.component.html',
  styleUrl: './record-field-links.component.css',
  standalone: true,
  imports: [JsonPipe, KeyValuePipe, Button, BadgeModule],
})
export class RecordFieldLinksComponent {
  links = input<GnLink[] | null>();

  distributionConfig = inject(APPLICATION_CONFIGURATION).ui?.mods.recordview
    .distributionConfig;

  configService = inject(ConfigService);

  router = inject(Router);
  gn4MapService = inject(Gn4MapService);

  // sections: [
  //   {
  //     filter:
  //       'protocol:OGC:WMS|OGC:WMTS|ESRI:.*|atom.*|REST|OGC API Maps|OGC API Records',
  //     title: 'API',
  //   },
  linksBySections = computed(() => {
    const linksBySections: { [key: string]: GnLink[] } = {};
    if (!this.links()) {
      return linksBySections;
    }

    this.distributionConfig?.sections.map(section => {
      let sectionFilter = this.configService.parseFilterExpression(
        section.filter
      );

      for (const link of this.links()!) {
        if (this.configService.testExpressionFilters(sectionFilter, link)) {
          if (!linksBySections[section.title]) {
            linksBySections[section.title] = [];
          }
          linksBySections[section.title]!.push(link);
        }
      }
    });
    return linksBySections;
  });

  addWmsLayers = this.gn4MapService.addWmsLayers;
}
