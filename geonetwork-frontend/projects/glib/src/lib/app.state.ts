import {
  patchState,
  signalStore,
  withComputed,
  withHooks,
  withMethods,
  withState,
} from '@ngrx/signals';
import { MeResponse } from 'gapi';
import { computed, inject } from '@angular/core';
import { AuthService } from './auth/auth.service';

export interface GappState {
  language: string;
  user: MeResponse | undefined | null;
  authenticationFailure: boolean | undefined;
}

const initialAppState: GappState = {
  language: 'en',
  user: undefined,
  authenticationFailure: undefined,
};

export const AppStore = signalStore(
  { providedIn: 'root' },
  withState(initialAppState),
  withComputed(store => {
    return {
      authenticated: computed<boolean | undefined>(() => {
        return store.user() === undefined ? undefined : !!store.user()?.id;
      }),
    };
  }),
  withMethods((store, authService = inject(AuthService)) => ({
    setLanguage(iso2letterCode: string) {
      patchState(store, { language: iso2letterCode });
    },
    signIn(username: string, password: string) {
      patchState(store, { authenticationFailure: undefined });
      return authService.signIn(username, password).subscribe({
        next: response => {
          patchState(store, { authenticationFailure: false });
          return this.loadUserInfo();
        },
        error: error => {
          console.log(error);
          patchState(store, {
            authenticationFailure: error.url.indexOf('signin?error') !== -1,
          });
          if (!store.authenticationFailure()) {
            return this.loadUserInfo();
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
      authService.getUser().then(user => {
        patchState(store, { user });
        return user;
      });
    },
  })),
  withHooks({
    onInit(store) {
      store.loadUserInfo();
    },
  })
);

export type AppStoreType = InstanceType<typeof AppStore>;
