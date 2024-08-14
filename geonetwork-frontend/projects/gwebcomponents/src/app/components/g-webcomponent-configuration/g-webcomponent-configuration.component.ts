import { Component, input, model, output, signal } from '@angular/core';

@Component({
  selector: 'g-webcomponent-configuration',
  templateUrl: './g-webcomponent-configuration.component.html',
  styleUrl: './g-webcomponent-configuration.component.css',
})
export class GWebcomponentConfigurationComponent {
  apiUrlList = input<string[]>();
  apiUrl = model<string>();
  searchFilter = model<string>();
  pageSize = model<number>();
}
