import { Routes } from '@angular/router';
import { GnAngularjsComponent } from './components/gn-angularjs/gn-angularjs.component';
import { GWebcomponentsDocComponent } from './components/g-webcomponents-doc/g-webcomponents-doc.component';
import { GDataResultsTableComponent } from './components/g-data-results-table/g-data-results-table.component';

export const routes: Routes = [
  { path: 'webcomponents', component: GWebcomponentsDocComponent },
  { path: 'gn', component: GnAngularjsComponent },
  { path: '', redirectTo: '/webcomponents', pathMatch: 'full' },
];
