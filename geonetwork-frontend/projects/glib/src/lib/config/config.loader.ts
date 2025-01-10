import { Configuration, DefaultConfig, UiApi, UiConfiguration } from 'gapi';
import { Configuration as Gn5Configuration } from 'g5api';
import { DEFAULT_UI_CONFIGURATION, MISSING_CONFIG_ERROR } from './constants';
import { InjectionToken, WritableSignal } from '@angular/core';

export interface ApplicationConfiguration {
  ui: UiConfiguration | undefined;
  apiConfig: Configuration | undefined;
  api5Config: Configuration | undefined;
  space: string;
}

export const DEFAULT_SPACE = 'srv';

export const API_CONFIGURATION = new InjectionToken<
  WritableSignal<Configuration>
>('GeoNetwork 4 API configuration');

export const API5_CONFIGURATION = new InjectionToken<
  WritableSignal<Gn5Configuration>
>('GeoNetwork 5 API configuration');

export const APPLICATION_CONFIGURATION =
  new InjectionToken<ApplicationConfiguration>('app.config');

let appConfig: ApplicationConfiguration = {
  ui: undefined,
  apiConfig: undefined,
  api5Config: undefined,
  space: DEFAULT_SPACE,
};

let appConfigLoading = true;

export function getAppConfig(): ApplicationConfiguration {
  if (appConfig === null) throw new Error(MISSING_CONFIG_ERROR);
  return appConfig;
}

export function buildGn4BaseUrl(baseUrl: string): string {
  const gn4BaseUrl = baseUrl.split('/').slice(0, -2).join('/');
  return gn4BaseUrl.startsWith('http')
    ? gn4BaseUrl
    : window.location.origin + gn4BaseUrl;
}

export function loadAppConfig(environment: any) {
  if (environment.baseUrl) {
    appConfig.apiConfig = new Configuration({ basePath: environment.baseUrl });
    appConfig.api5Config = new Configuration({
      basePath: environment.baseUrlGn5Api,
    });
  } else {
    appConfig.apiConfig = DefaultConfig;
    appConfig.api5Config = DefaultConfig;
  }

  return new UiApi(appConfig.apiConfig)
    .getUiConfiguration({ uiIdentifier: appConfig.space })
    .then(
      (response: any) => {
        appConfigLoading = false;
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
