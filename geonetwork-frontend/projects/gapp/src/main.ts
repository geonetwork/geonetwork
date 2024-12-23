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
  GeoNetworkTheme,
  loadAppConfig,
} from 'glib';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { routes } from './app/app.routes';
import { environment } from './environments/environment';
import { DATE_PIPE_DEFAULT_OPTIONS } from '@angular/common';
import { provideHttpClient } from '@angular/common/http';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';

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
      provideHttpClient(),
      providePrimeNG({
        theme: {
          preset: GeoNetworkTheme,
          options: {
            darkModeSelector: '.gn-app-dark',
          },
        },
      }),
    ],
  };

  bootstrapApplication(AppComponent, applicationConfig).catch(err =>
    console.error(err)
  );
});
