import { Component, computed, inject, input, Input } from '@angular/core';
import { LinkActionTypes } from 'gapi';
import { Button } from 'primeng/button';
import { Gn4MapService } from '../../shared/gn4-map.service';
import { KeyValuePipe, NgTemplateOutlet } from '@angular/common';
import { Toast } from 'primeng/toast';
import { Menu } from 'primeng/menu';
import { MenuItem, MessageService, PrimeTemplate } from 'primeng/api';
import { RecordLinkService } from '../record-link.service';
import { RecordLinkActionIconPipe } from '../record-link-action-icon.pipe';
import { Link } from 'g5api';

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
  links = input<Link[] | null>();

  gn4MapService = inject(Gn4MapService);

  distributionService = inject(RecordLinkService);

  linksBySections = computed(() => {
    return this.distributionService.linksByType(this.links());
  });

  buildMenuItems(links: Link[], type: string): MenuItem[] | undefined {
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

  getLinkAction(link: Link, type: LinkActionTypes) {
    return this.distributionService.getLinkAction(link, type);
  }
}
