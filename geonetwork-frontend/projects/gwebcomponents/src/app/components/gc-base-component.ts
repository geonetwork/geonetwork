import {
  Component,
  inject,
  Input,
  OnChanges,
  OnInit,
  signal,
  SimpleChanges,
} from '@angular/core';
import { Configuration, DefaultConfig } from 'gapi';
import {API_CONFIGURATION, GeoNetworkTheme, SearchService} from 'glib';
import {PrimeNGConfig} from "primeng/api";
import { Aura } from 'primeng/themes/aura';

@Component({
  selector: 'gc-base-component',
  template: '<div></div>',
  providers: [
    { provide: API_CONFIGURATION, useValue: signal(DefaultConfig) },
    SearchService,
  ],
})
export class GcBaseComponent implements OnInit, OnChanges {
  @Input({ alias: 'api-url' }) apiUrl: string;

  apiConfiguration = inject(API_CONFIGURATION);

  constructor(private config: PrimeNGConfig) {
    this.config.theme.set({
      // preset: Aura,
      preset: GeoNetworkTheme,
      options: {
        cssLayer: {
          name: 'primeng',
          order: 'tailwind-base, primeng, tailwind-utilities',
        },
      },
    });
  }

  ngOnInit() {
    this.apiConfiguration.set(new Configuration({ basePath: this.apiUrl }));
  }

  ngOnChanges(changes: SimpleChanges): void {
    Object.keys(changes).forEach(prop => {
      if (prop == 'apiUrl') {
        this.apiConfiguration.set(
          new Configuration({ basePath: changes['apiUrl'].currentValue })
        );
      }
    });
  }
}
