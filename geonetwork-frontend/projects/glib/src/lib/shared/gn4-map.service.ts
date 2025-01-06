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

  addMap = (link: GnLink) => {
    if (link.urlObject) {
      this.applyCommand(`map=${encodeURIComponent(link.urlObject.default)}`);
    } else {
      console.warn('Map link is missing url pointing to a map context.', link);
    }
  };

  addWmsLayers = (links: GnLink | GnLink[]) => {
    if (!Array.isArray(links)) {
      links = [links];
    }
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
      const commandParameter = 'add=' + JSON.stringify(command);
      this.applyCommand(commandParameter);
    } else {
      links.forEach(link => {
        if (!link.urlObject) {
          console.warn('Add layer link is missing url or name object', link);
        }
      });
    }
  };

  applyCommand = (command: string) => {
    const webcomponent = document.getElementsByTagName('gn-app-frame')[0];
    const url = webcomponent.getAttribute('url') || '';
    const config = webcomponent.getAttribute('config') || '';
    const language = webcomponent.getAttribute('language') || 'eng';
    webcomponent.shadowRoot?.children[0].setAttribute(
      'src',
      `${url}/srv/${language}/catalog.search?uiconfig=${encodeURIComponent(config.trim())}#/map?${command}`
    );
    this.router.navigate(['/map']);
  };
}
