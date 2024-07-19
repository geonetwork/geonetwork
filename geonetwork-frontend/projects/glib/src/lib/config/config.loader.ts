import { Configuration, DefaultConfig, UiApi, UiConfiguration } from 'gapi';
import { DEFAULT_UI_CONFIGURATION, MISSING_CONFIG_ERROR } from './constants';
import { InjectionToken, signal, WritableSignal } from '@angular/core';

export interface ApplicationConfiguration {
  ui: UiConfiguration | undefined;
  apiConfig: Configuration | undefined;
  space: string;
}

export const DEFAULT_SPACE = 'srv';

export const API_CONFIGURATION = new InjectionToken<
  WritableSignal<Configuration>
>('api.baseUrl');

export const APPLICATION_CONFIGURATION =
  new InjectionToken<ApplicationConfiguration>('app.config');

let appConfig: ApplicationConfiguration = {
  ui: undefined,
  apiConfig: undefined,
  space: DEFAULT_SPACE,
};

let appConfigLoading = true;

export function getAppConfig(): ApplicationConfiguration {
  if (appConfig === null) throw new Error(MISSING_CONFIG_ERROR);
  return appConfig;
}

export function loadAppConfig(environment: any) {
  if (environment.baseUrl) {
    appConfig.apiConfig = new Configuration({ basePath: environment.baseUrl });
  } else {
    appConfig.apiConfig = DefaultConfig;
  }

  return new UiApi(appConfig.apiConfig)
    .getUiConfiguration({ uiIdentifier: appConfig.space })
    .then(
      (response: any) => {
        appConfigLoading = false;
        console.log(response);
        appConfig.ui = response.configuration
          ? (JSON.parse(response.configuration) as UiConfiguration)
          : DEFAULT_UI_CONFIGURATION;
        return appConfig;
      },
      (error: any) => {
        console.warn(
          'Invalid or empty configuration returned by the API. Using the default configuration.'
        );
        appConfig.ui = DEFAULT_UI_CONFIGURATION;
        return appConfig;
      }
    );
}
