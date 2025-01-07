import { Component, ViewEncapsulation } from '@angular/core';
import { NewRecordPanelComponent } from 'glib';

@Component({
  selector: 'gc-new-record-panel',
  templateUrl: './gc-new-record-panel.component.html',
  imports: [NewRecordPanelComponent],
  styleUrl: './gc-new-record-panel.component.css',
  // encapsulation: ViewEncapsulation.ShadowDom,
})
export class GcNewRecordPanelComponent {}
