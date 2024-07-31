import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import {
  ApplicationConfig,
  enableProdMode,
  provideExperimentalZonelessChangeDetection,
  signal,
} from '@angular/core';
import {
  API_CONFIGURATION,
  APPLICATION_CONFIGURATION,
  loadAppConfig,
} from 'glib';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { routes } from './app/app.routes';
import { environment } from './environments/environment';
import { DATE_PIPE_DEFAULT_OPTIONS } from '@angular/common';

if (environment.production) {
  enableProdMode();
}

loadAppConfig(environment).then((config: any) => {
  let applicationConfig: ApplicationConfig = {
    providers: [
      { provide: APPLICATION_CONFIGURATION, useValue: config },
      { provide: API_CONFIGURATION, useValue: signal(config.apiConfig) },
      {
        provide: DATE_PIPE_DEFAULT_OPTIONS,
        useValue: {
          dateFormat: config.ui.mods.global.dateFormat || 'dd/MM/yyyy',
        },
      },
      provideExperimentalZonelessChangeDetection(),
      provideRouter(routes),
      provideAnimations(),
    ],
  };

  bootstrapApplication(AppComponent, applicationConfig).catch(err =>
    console.error(err)
  );
});
