import { Component, computed, OnInit, signal } from '@angular/core';

interface field {
  label: string;
  path: string;
  order: number;
}

@Component({
  selector: 'g-webcomponents-doc',
  templateUrl: './g-webcomponents-doc.component.html',
  styleUrl: './g-webcomponents-doc.component.css',
})
export class GWebcomponentsDocComponent implements OnInit {
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

  selectionModeList = [
    { label: 'none', type: undefined },
    { label: 'single', type: 'single' },
    { label: 'multiple', type: 'multiple' },
  ];
  selectionMode = this.selectionModeList[0].type;

  listOfFields: field[] = [
    { label: 'Aperçu', path: 'overview[*]', order: 1 },
    { label: 'Titre', path: 'resourceTitleObject.default', order: 2 },
    { label: 'Type', path: 'resourceType[0]', order: 3 },
    { label: 'Mot clé', path: 'th_gemet[*]', order: 4 },
    { label: 'Status', path: 'cl_status[0].default', order: 5 },
    { label: 'Légende', path: "link[?(@.function == 'legend')]", order: 6 },
    { label: 'Visualiser', path: "link[?(@.protocol == 'OGC:WMS')]", order: 7 },
    {
      label: 'Gestionnaire',
      path: 'custodianOrgForResourceObject[*].default',
      order: 8,
    },
    { label: 'Crédit', path: 'resourceCréditObject.default', order: 9 },
  ];
  selectedFields = signal(this.listOfFields);
  otherFieldsAsText = signal('');

  listOfFieldsAsText = computed(
    () =>
      this.selectedFields()
        .sort((a, b) => a.order - b.order)
        .map(field => field.path)
        .join(',') + this.otherFieldsAsText()
  );
  listOfLabelsAsText = computed(
    () =>
      this.selectedFields()
        .sort((a, b) => a.order - b.order)
        .map(field => field.label)
        .join(',') + this.otherFieldsAsText()
  );

  ngOnInit() {}
}
