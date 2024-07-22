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
import { API_CONFIGURATION, SearchService, SearchAggLayout } from 'glib';

@Component({
  selector: 'gc-search',
  templateUrl: './gc-search.component.html',
  styleUrl: './gc-search.component.css',
  encapsulation: ViewEncapsulation.ShadowDom,
  providers: [
    { provide: API_CONFIGURATION, useValue: signal(DefaultConfig) },
    SearchService,
  ],
})
export class GcSearchComponent implements OnInit, OnChanges {
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

  protected readonly SearchAggLayout = SearchAggLayout;
}
