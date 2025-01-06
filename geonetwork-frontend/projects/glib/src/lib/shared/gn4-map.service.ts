import { inject, Injectable } from '@angular/core';
import { GnLink } from 'gapi';
import { Router } from '@angular/router';

interface Gn4MapCommand {
  url: string;
  name?: string;
}

@Injectable({
  providedIn: 'root',
})
export class Gn4MapService {
  router = inject(Router);

  addWmsLayers = (links: GnLink[]) => {
    const command = links
      .filter(link => link.urlObject)
      .map(link => {
        const cmd: Gn4MapCommand = {
          url: encodeURIComponent(link.urlObject!.default),
        };
        if (link.nameObject) {
          cmd.name = link.nameObject.default;
        }
        return cmd;
      });

    if (command.length > 0) {
      // window.location.replace('map#/map?add=' + JSON.stringify(command));
      const webcomponent = document.getElementsByTagName('gn-app-frame')[0];
      const url = webcomponent.getAttribute('url') || '';
      const config = webcomponent.getAttribute('config') || '';
      const language = webcomponent.getAttribute('language') || 'eng';
      webcomponent.shadowRoot?.children[0].setAttribute(
        'src',
        url +
          '/srv/eng/catalog.search?uiconfig=' +
          encodeURIComponent(config.trim()) +
          '#/map?add=' +
          JSON.stringify(command)
      );
      this.router.navigate(['/map']);
    } else {
      links.forEach(link => {
        if (!link.urlObject) {
          console.warn('Link is missing url or name object', link);
        }
      });
    }
  };

  // TODO: Add Map
  // window.location.href = 'map#/map?map=' + encodeURI(mapUrl);
}
