import {
  AfterViewInit,
  Component,
  ElementRef,
  inject,
  Input,
  OnChanges,
  OnInit,
  signal,
  SimpleChanges,
  ViewEncapsulation,
} from '@angular/core';
import { Configuration, DefaultConfig } from 'gapi';
import { API5_CONFIGURATION, API_CONFIGURATION, SearchService } from 'glib';
import { Configuration as Gn5Configuration } from 'g5api';
import { GcShadowdomstyleComponentComponent } from './gc-shadowdomstyle-component';

@Component({
  selector: 'gc-base-component',
  template: '<div></div>',
  providers: [
    { provide: API_CONFIGURATION, useValue: signal(DefaultConfig) },
    { provide: API5_CONFIGURATION, useValue: signal(Gn5Configuration) },
    SearchService,
  ],
  // encapsulation: ViewEncapsulation.ShadowDom,
})
// extends GcShadowdomstyleComponentComponent
export class GcBaseComponent implements OnInit, OnChanges {
  @Input({ alias: 'api-url' }) apiUrl: string;

  apiConfiguration = inject(API_CONFIGURATION);
  api5Configuration = inject(API5_CONFIGURATION);

  setApiUrl(url: string) {
    this.apiConfiguration.set(new Configuration({ basePath: url }));
    // TODO: Update to use GN5 context path
    this.api5Configuration.set(
      new Gn5Configuration({
        basePath: url && url.replace('/srv/api', ''),
      })
    );
    console.log(
      'Setting APIs URL',
      url,
      this.api5Configuration(),
      this.apiConfiguration()
    );
  }

  ngOnInit() {
    // super.ngOnInit();
    this.setApiUrl(this.apiUrl);
  }

  ngOnChanges(changes: SimpleChanges): void {
    Object.keys(changes).forEach(prop => {
      if (prop == 'apiUrl') {
        this.setApiUrl(changes[prop].currentValue);
      }
    });
  }
}
