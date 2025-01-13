import { Component, input, signal } from '@angular/core';
import { RadioButton } from 'primeng/radiobutton';
import { FormsModule } from '@angular/forms';
import { FileUpload } from 'primeng/fileupload';

@Component({
  selector: 'g-overview-selector',
  imports: [RadioButton, FormsModule, FileUpload],
  templateUrl: './overview-selector.component.html',
  standalone: true,
})
export class OverviewSelectorComponent {
  uuid = input.required<string>();
  suggestion = input<string | undefined>();

  listOfOverviewTypes = [
    'overview_none',
    'overview_upload',
    'overview_suggestion',
  ];

  overviewType = signal(this.listOfOverviewTypes[0]);

  uploadOverview(event: any) {}
}
