import { DEFAULT_APP_CONFIG, MISSING_CONFIG_ERROR } from './constants';
import { InjectionToken } from '@angular/core';
import { AppConfig, getUiConfiguration, UiSetting } from 'gapi';

export const APP_CONFIG_TOKEN = new InjectionToken<AppConfig>('APP_CONFIG');

let appConfig: UiSetting | null = null;
let appConfigLoading = true;

export function getAppConfig(): UiSetting {
  if (appConfig === null) throw new Error(MISSING_CONFIG_ERROR);
  return appConfig;
}

export function loadAppConfig() {
  return getUiConfiguration({ uiIdentifier: 'srv' }).then(
    (response: any) => {
      appConfigLoading = false;
      console.log(response);
      return response.configuration
        ? (JSON.parse(response.configuration) as AppConfig)
        : {};
    },
    (error: any) => {
      console.warn(
        'Invalid or empty configuration returned by the API. Using the default configuration.'
      );
      return DEFAULT_APP_CONFIG;
    }
  );
}
