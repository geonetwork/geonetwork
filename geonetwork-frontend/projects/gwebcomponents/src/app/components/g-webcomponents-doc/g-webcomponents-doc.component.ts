import { Component, computed, signal } from '@angular/core';

@Component({
  selector: 'g-webcomponents-doc',
  templateUrl: './g-webcomponents-doc.component.html',
  styleUrl: './g-webcomponents-doc.component.css',
})
export class GWebcomponentsDocComponent {
  apiUrlList = [
    { url: 'http://localhost:8080/geonetwork/srv/api' },
    { url: 'https://apps.titellus.net/geonetwork/srv/api' },
    { url: 'https://www.nationaalgeoregister.nl/geonetwork/srv/api' },
    { url: 'https://sextant.ifremer.fr/geonetwork/srv/api' },
    { url: 'https://metawal.wallonie.be/geonetwork/srv/api' },
  ];

  apiUrl = this.apiUrlList[0].url;

  csvSourceFiles = [
    'https://static.data.gouv.fr/resources/journees-europeennes-du-patrimoine/20160914-151814/Journees_europeennes_du_patrimoine_20160914.csv',
    'https://www.data.gouv.fr/fr/datasets/r/ca621361-ec32-458a-8c40-e27bdcf58b16',
  ];

  selectedSourceFile = signal(this.csvSourceFiles[0]);
}
