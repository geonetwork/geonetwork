import {Component,} from '@angular/core';
import {Button} from 'primeng/button';

@Component({
  selector: 'gn-map-page',
  standalone: true,
  imports: [Button],
  templateUrl: './map-page.component.html',
  styleUrl: './map-page.component.css',
})
export class MapPageComponent {
  addLayerFromService = (serviceUrl: string) => {
    let command = [{ url: serviceUrl }];
    window.location.hash = '#/map?add=' + JSON.stringify(command);
  };

  addLayer = (serviceUrl: string, layer: string) => {
    let command = [{ url: serviceUrl, name: layer }];
    window.location.hash = '#/map?add=' + JSON.stringify(command);
  };

  addMap = (mapUrl: string) => {
    window.location.hash = '#/map?map=' + mapUrl;
  };

  defaultMap = (mapUrl: string) => {
    window.location.hash = '#/map';
  };

  setTool = (tool: string) => {
    window.location.hash = '#/map?tool=' + tool;
  };
}
