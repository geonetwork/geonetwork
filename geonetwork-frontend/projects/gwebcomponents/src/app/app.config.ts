import {
  ApplicationConfig,
  provideExperimentalZonelessChangeDetection,
  signal,
} from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { providePrimeNG } from 'primeng/config';
import {
  API5_CONFIGURATION,
  API_CONFIGURATION,
  APPLICATION_CONFIGURATION,
  GeoNetworkTheme,
  getAppConfig,
  loadAppConfig,
} from 'glib';
import { DefaultConfig } from 'gapi';
import { provideHttpClient } from '@angular/common/http';
import { Configuration as Gn5Configuration } from 'g5api';

export const appConfig: ApplicationConfig = {
  providers: [
    provideExperimentalZonelessChangeDetection(),
    provideRouter(routes, withComponentInputBinding()),
    provideAnimations(),
    provideHttpClient(),
    { provide: APPLICATION_CONFIGURATION, useValue: getAppConfig() },
    { provide: API_CONFIGURATION, useValue: signal(DefaultConfig) },
    { provide: API5_CONFIGURATION, useValue: signal(Gn5Configuration) },
    providePrimeNG({
      theme: {
        preset: GeoNetworkTheme,
      },
    }),
  ],
};
