import { Component, input, model, output } from '@angular/core';

@Component({
  selector: 'g-webcomponent-configuration',
  templateUrl: './g-webcomponent-configuration.component.html',
  styleUrl: './g-webcomponent-configuration.component.css',
})
export class GWebcomponentConfigurationComponent {
  apiUrlList = input<string[]>();
  apiUrl = model<string>();
}
