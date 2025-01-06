import { computed, inject, Injectable } from '@angular/core';
import { Gn4MapService } from '../shared/gn4-map.service';
import { GnLink, LinkActionTypes, LinkTypes } from 'gapi';
import { APPLICATION_CONFIGURATION } from 'glib';
import { ConfigService } from '../shared/config.service';

interface LinkType {
  icon: string;
  action?: Function;
}

@Injectable({
  providedIn: 'root',
})
export class RecordLinkService {
  gn4MapService = inject(Gn4MapService);
  configService = inject(ConfigService);

  distributionConfig = inject(APPLICATION_CONFIGURATION).ui?.mods.recordview
    .distributionConfig;

  linkTypesActions: { [key in LinkActionTypes]: LinkType | undefined } = {
    layers: {
      icon: 'fa-layer-group',
      action: this.gn4MapService.addWmsLayers,
    },
    maps: {
      icon: 'fa-map',
      action: this.gn4MapService.addMap,
    },
    links: {
      icon: 'fa-link',
    },
    downloads: {
      icon: 'fa-download',
    },
  };

  linkTypesProtocols: LinkTypes =
    inject(APPLICATION_CONFIGURATION).ui?.mods.search.linkTypes || {};

  types = Object.keys(this.linkTypesProtocols);

  linksByType = (links: GnLink[] | null | undefined) => {
    const linksByType: { [key in LinkActionTypes]?: GnLink[] } = {};
    if (!links) {
      return linksByType;
    }

    this.types.map(type => {
      let protocols = this.linkTypesProtocols[type as LinkActionTypes] || [];
      // TODO: in GN4 there is some logic about value starting by #
      // Maybe we should combine this with distribution panel logic
      // let linksForType = links!.filter(link => protocols.includes(link.protocol || ''));
      let linksForType = links!.filter(link =>
        (link.protocol || '').match(protocols.join('|'))
      );
      if (linksForType.length > 0) {
        linksByType[type as LinkActionTypes] = linksForType;
      }
    });

    return linksByType;
  };

  // sections: [
  //   {
  //     filter:
  //       'protocol:OGC:WMS|OGC:WMTS|ESRI:.*|atom.*|REST|OGC API Maps|OGC API Records',
  //     title: 'API',
  //   },
  linksBySections = (links: GnLink[] | null | undefined) => {
    const linksBySections: { [key: string]: GnLink[] } = {};
    if (!links) {
      return linksBySections;
    }

    this.distributionConfig?.sections.map(section => {
      let sectionFilter = this.configService.parseFilterExpression(
        section.filter
      );

      for (const link of links!) {
        if (this.configService.testExpressionFilters(sectionFilter, link)) {
          if (!linksBySections[section.title]) {
            linksBySections[section.title] = [];
          }
          linksBySections[section.title]!.push(link);
        }
      }
    });
    return linksBySections;
  };

  getLinkAction(link: GnLink, type: string) {
    const linkType = this.linkTypesActions[type as LinkActionTypes];
    return linkType?.action || undefined;
  }

  getLinkActionIcon(type: string): string {
    return this.linkTypesActions[type as LinkActionTypes]?.icon || 'fa-link';
  }
}
