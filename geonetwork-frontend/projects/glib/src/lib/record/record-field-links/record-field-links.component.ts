import { Component, computed, inject, input } from '@angular/core';
import { ConfigService } from '../../shared/config.service';
import { Button } from 'primeng/button';
import { Router } from '@angular/router';
import { APPLICATION_CONFIGURATION } from '../../config/config.loader';
import { BadgeModule } from 'primeng/badge';
import { Gn4MapService } from '../../shared/gn4-map.service';
import { RecordLinkService } from '../record-link.service';
import { KeyValuePipe } from '@angular/common';
import { Link } from 'g5api';

@Component({
  selector: 'g-record-field-links',
  templateUrl: './record-field-links.component.html',
  standalone: true,
  imports: [KeyValuePipe, Button, BadgeModule],
})
export class RecordFieldLinksComponent {
  links = input<Link[] | null>();

  distributionConfig = inject(APPLICATION_CONFIGURATION).ui?.mods.recordview
    .distributionConfig;

  configService = inject(ConfigService);

  router = inject(Router);
  gn4MapService = inject(Gn4MapService);
  distributionService = inject(RecordLinkService);

  linksBySections = computed(() => {
    return this.distributionService.linksBySections(this.links());
  });

  addWmsLayers = this.gn4MapService.addWmsLayers;
}
