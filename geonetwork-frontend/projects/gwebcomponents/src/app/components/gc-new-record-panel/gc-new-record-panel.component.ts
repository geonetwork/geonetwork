import { Component, signal, ViewEncapsulation } from '@angular/core';
import { API_CONFIGURATION, NewRecordPanelComponent } from 'glib';
import { DefaultConfig } from 'gapi';
import { GcBaseComponent } from '../gc-base-component';

@Component({
  selector: 'gc-new-record-panel',
  templateUrl: './gc-new-record-panel.component.html',
  providers: [{ provide: API_CONFIGURATION, useValue: signal(DefaultConfig) }],
  imports: [NewRecordPanelComponent],
  styleUrl: './gc-new-record-panel.component.css',
  // encapsulation: ViewEncapsulation.ShadowDom,
})
export class GcNewRecordPanelComponent extends GcBaseComponent {}
