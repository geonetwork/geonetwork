import { AfterViewInit, Component, ElementRef } from '@angular/core';

@Component({
  selector: 'g-gn-angularjs',
  templateUrl: './gn-angularjs.component.html',
  styleUrl: './gn-angularjs.component.css',
})
export class GnAngularjsComponent implements AfterViewInit {
  constructor(private el: ElementRef) {}

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

  ngAfterViewInit() {
    var s = document.createElement('script');
    s.type = 'text/javascript';
    s.src =
      'https://apps.titellus.net/geonetwork/catalog/webcomponents/webcomponents.js';
    this.el.nativeElement.appendChild(s);
  }
}
