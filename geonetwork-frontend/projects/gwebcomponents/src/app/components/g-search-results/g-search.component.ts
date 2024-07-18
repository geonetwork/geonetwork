import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
  ViewEncapsulation,
} from '@angular/core';
import { Configuration } from 'gapi';

@Component({
  selector: 'g-search',
  templateUrl: './g-search.component.html',
  styleUrl: './g-search.component.css',
  encapsulation: ViewEncapsulation.ShadowDom,
})
export class GSearchComponent implements OnInit, OnChanges {
  @Input() apiUrl: string;
  @Input() searchId = Math.random().toString().slice(2, 5);

  constructor() {}

  ngOnChanges(changes: SimpleChanges): void {
    new Configuration({ basePath: changes['apiUrl'].currentValue });
  }

  ngOnInit() {
    new Configuration({ basePath: this.apiUrl });
  }
}
