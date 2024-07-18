import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import {
  ApplicationConfig,
  enableProdMode,
  provideExperimentalZonelessChangeDetection,
} from '@angular/core';
import { APP_CONFIG, loadAppConfig } from 'glib';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { routes } from './app/app.routes';
import { environment } from './environments/environment';
import { Configuration } from '../../gapi/src/lib';

if (environment.production) {
  enableProdMode();
}

if (environment.baseUrl) {
  new Configuration({ basePath: environment.baseUrl });
}

loadAppConfig().then((config: any) => {
  let applicationConfig: ApplicationConfig = {
    providers: [
      { provide: APP_CONFIG, useValue: config },
      // provideZoneChangeDetection({ eventCoalescing: true }),
      provideExperimentalZonelessChangeDetection(),
      provideRouter(routes),
      provideAnimations(),
    ],
  };

  bootstrapApplication(AppComponent, applicationConfig).catch(err =>
    console.error(err)
  );
});
