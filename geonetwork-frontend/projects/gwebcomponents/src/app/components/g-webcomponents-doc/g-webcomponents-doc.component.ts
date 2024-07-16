import { Component, signal } from '@angular/core';

@Component({
  selector: 'g-webcomponents-doc',
  templateUrl: './g-webcomponents-doc.component.html',
  styleUrl: './g-webcomponents-doc.component.css',
})
export class GWebcomponentsDocComponent {
  csvSourceFiles = [
    'https://static.data.gouv.fr/resources/journees-europeennes-du-patrimoine/20160914-151814/Journees_europeennes_du_patrimoine_20160914.csv',
    'https://www.data.gouv.fr/fr/datasets/r/ca621361-ec32-458a-8c40-e27bdcf58b16',
  ];

  selectedSourceFile = signal(this.csvSourceFiles[0]);
}
