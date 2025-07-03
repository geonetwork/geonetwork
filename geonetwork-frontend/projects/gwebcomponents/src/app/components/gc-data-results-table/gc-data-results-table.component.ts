import {
  Component,
  Input,
  input,
  OnChanges,
  OnInit,
  signal,
  SimpleChanges,
  ViewEncapsulation,
} from '@angular/core';
import { Configuration } from 'gapi';
import { DataTableComponent } from 'glib';
import { GcShadowdomstyleComponentComponent } from '../gc-shadowdomstyle-component';

@Component({
  selector: 'gc-data-results-table',
  templateUrl: './gc-data-results-table.component.html',
  styleUrl: './gc-data-results-table.component.css',
  imports: [DataTableComponent],
  encapsulation: ViewEncapsulation.ShadowDom,
})
export class GcDataResultsTableComponent
  extends GcShadowdomstyleComponentComponent
  implements OnInit, OnChanges
{
  @Input() source = '';

  @Input() export = false;

  currentSource = signal<string>(this.source);
  isAllowingExport = signal<boolean>(this.export);

  override ngOnInit() {
    super.ngOnInit();
    this.currentSource.set(this.source);
    this.isAllowingExport.set(this.export);
  }

  ngOnChanges(changes: SimpleChanges): void {
    Object.keys(changes).forEach(prop => {
      if (prop == 'source') {
        this.currentSource.set(changes[prop].currentValue);
      } else if (prop == 'export') {
        this.isAllowingExport.set(changes[prop].currentValue);
      }
    });
  }
}
