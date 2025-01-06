import { Component, computed, inject, input, Input } from '@angular/core';
import { GnLink, LinkActionTypes, LinkTypes } from 'gapi';
import { Button } from 'primeng/button';
import { Gn4MapService } from '../../shared/gn4-map.service';
import { APPLICATION_CONFIGURATION } from 'glib';
import { KeyValuePipe, NgTemplateOutlet } from '@angular/common';
import { Toast } from 'primeng/toast';
import { Menu } from 'primeng/menu';
import { MenuItem, MessageService, PrimeTemplate } from 'primeng/api';
import { RecordLinkService } from '../record-link.service';
import { RecordLinkActionIconPipe } from '../record-link-action-icon.pipe';
import { SplitButton } from 'primeng/splitbutton';

@Component({
  selector: 'g-record-field-links-button',
  standalone: true,

  templateUrl: './record-field-links-button.component.html',
  imports: [
    Button,
    KeyValuePipe,
    NgTemplateOutlet,
    Toast,
    Menu,
    RecordLinkActionIconPipe,
  ],
  providers: [MessageService],
})
export class RecordFieldLinksButtonComponent {
  links = input<GnLink[] | null>();

  gn4MapService = inject(Gn4MapService);

  distributionService = inject(RecordLinkService);

  linksBySections = computed(() => {
    return this.distributionService.linksByType(this.links());
  });

  buildMenuItems(links: GnLink[], type: string): MenuItem[] | undefined {
    return links.map(link => {
      const menuItem: MenuItem = {
        label:
          link.nameObject?.default || link.urlObject?.default || '- missing -',
        icon: 'fa ' + this.distributionService.getLinkActionIcon(type),
      };
      const action = this.distributionService.getLinkAction(link, type);
      if (type && action) {
        menuItem.command = () => {
          action(link, type);
        };
      } else {
        menuItem.url = link.urlObject?.default;
      }
      return menuItem;
    });
  }

  getLinkAction(link: GnLink, type: LinkActionTypes) {
    return this.distributionService.getLinkAction(link, type);
  }
}
