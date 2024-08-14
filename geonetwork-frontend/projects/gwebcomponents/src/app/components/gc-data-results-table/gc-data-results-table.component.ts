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

@Component({
  selector: 'gc-data-results-table',
  templateUrl: './gc-data-results-table.component.html',
  styleUrl: './gc-data-results-table.component.css',
  encapsulation: ViewEncapsulation.ShadowDom,
})
export class GcDataResultsTableComponent implements OnInit, OnChanges {
  @Input() source = '';

  currentSource = signal<string>('');

  ngOnInit() {
    this.currentSource.set(this.source);
  }

  ngOnChanges(changes: SimpleChanges): void {
    Object.keys(changes).forEach(prop => {
      if (prop == 'source') {
        this.currentSource.set(changes[prop].currentValue);
      }
    });
  }
}
