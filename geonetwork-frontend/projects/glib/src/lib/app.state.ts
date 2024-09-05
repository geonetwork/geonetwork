import {
  patchState,
  signalStore,
  withComputed,
  withHooks,
  withMethods,
  withState,
} from '@ngrx/signals';
import { MeApi } from 'gapi';
import { computed, inject } from '@angular/core';
import { API_CONFIGURATION, SearchService } from 'glib';
import { AuthService } from '../../../glib/src/lib/auth/auth.service';

export interface GappState {
  language: string;
  user: any;
  authenticated: boolean;
  authenticationFailure: boolean;
}

const initialAppState: GappState = {
  language: 'en',
  user: {},
  authenticated: false,
  authenticationFailure: false,
};

export const AppStore = signalStore(
  { providedIn: 'root' },
  withState(initialAppState),
  withComputed((store, searchService = inject(SearchService)) => {
    return {
      authenticated: computed(() => {
        return !!store.user()?.id;
      }),
    };
  }),
  withMethods(
    (
      store,
      authService = inject(AuthService),
      apiConfiguration = inject(API_CONFIGURATION)
    ) => ({
      setLanguage(iso2letterCode: string) {
        patchState(store, { language: iso2letterCode });
      },
      signIn(username: string, password: string) {
        patchState(store, { authenticationFailure: false });
        return authService.signIn(username, password).subscribe({
          next: response => {
            this.loadUserInfo();
          },
          error: error => {
            console.log(error);
            patchState(store, {
              authenticationFailure: error.url.indexOf('failure=true') !== -1,
            });
            if (!store.authenticationFailure()) {
              this.loadUserInfo();
            }
          },
        });
      },
      signOut() {
        return authService.signOut().subscribe({
          next: response => {
            this.loadUserInfo();
          },
          error: error => {
            this.loadUserInfo();
          },
        });
      },
      loadUserInfo() {
        new MeApi(apiConfiguration()).getMe().then(user => {
          patchState(store, { user });
        });
      },
    })
  ),
  withHooks({
    onInit(store) {
      store.loadUserInfo();
    },
  })
);
