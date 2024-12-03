import { Component } from '@angular/core';
import { NewRecordPanelComponent } from 'glib';

@Component({
  selector: 'gn-new-record-page',
  templateUrl: './new-record-page.component.html',
  standalone: true,
  imports: [NewRecordPanelComponent],
  styleUrl: './new-record-page.component.css',
})
export class NewRecordPageComponent {}
