import {
  Component,
  inject,
  Input,
  OnChanges,
  OnInit,
  signal,
  SimpleChanges,
  ViewEncapsulation,
} from '@angular/core';
import { Configuration, DefaultConfig } from 'gapi';
import { API_CONFIGURATION, SearchService } from 'glib';

@Component({
  selector: 'g-search',
  templateUrl: './g-search.component.html',
  styleUrl: './g-search.component.css',
  encapsulation: ViewEncapsulation.ShadowDom,
  providers: [
    { provide: API_CONFIGURATION, useValue: signal(DefaultConfig) },
    SearchService,
  ],
})
export class GSearchComponent implements OnInit, OnChanges {
  @Input() apiUrl: string;
  @Input() searchId = Math.random().toString().slice(2, 5);

  apiConfiguration = inject(API_CONFIGURATION);

  ngOnChanges(changes: SimpleChanges): void {
    this.apiConfiguration.set(
      new Configuration({ basePath: changes['apiUrl'].currentValue })
    );
  }

  ngOnInit() {
    this.apiConfiguration.set(new Configuration({ basePath: this.apiUrl }));
  }
}
