import { Component, signal, ViewEncapsulation } from '@angular/core';
import { GcBaseComponent } from '../gc-base-component';
import { NewRecordPanelComponent } from 'glib';

@Component({
  selector: 'gc-new-record-panel',
  templateUrl: './gc-new-record-panel.component.html',
  imports: [NewRecordPanelComponent],
  styleUrl: './gc-new-record-panel.component.css',
  // encapsulation: ViewEncapsulation.ShadowDom,
})
export class GcNewRecordPanelComponent extends GcBaseComponent {}
