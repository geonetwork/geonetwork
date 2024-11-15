import {
  AfterViewInit,
  Component,
  computed,
  ElementRef,
  input,
  Input,
  OnInit,
} from '@angular/core';
import { buildGn4BaseUrl } from 'glib';

@Component({
  selector: 'g-gn-angularjs',
  templateUrl: './gn-angularjs.component.html',
  styleUrl: './gn-angularjs.component.css',
})
export class GnAngularjsComponent implements OnInit, AfterViewInit {
  gn4baseApiUrl = input<string>();

  gn4baseUrl: string;

  constructor(private el: ElementRef) {}

  ngOnInit(): void {
    this.gn4baseUrl = buildGn4BaseUrl(
      this.gn4baseApiUrl() || window.location.origin + '/geonetwork/srv/api'
    );
  }

  ngAfterViewInit() {
    var s = document.createElement('script');
    s.type = 'text/javascript';
    s.src = this.gn4baseUrl + '/catalog/webcomponents/webcomponents.js';
    this.el.nativeElement.appendChild(s);
  }

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
