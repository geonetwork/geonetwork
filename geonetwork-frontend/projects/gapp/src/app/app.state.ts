import {
  patchState,
  signalStore,
  withHooks,
  withMethods,
  withState,
} from '@ngrx/signals';
import { MeApi } from 'gapi';

export interface GappState {
  language: string;
  user: any;
}

const initialAppState: GappState = {
  language: 'en',
  user: {},
};

export const AppStore = signalStore(
  { providedIn: 'root' },
  withState(initialAppState),
  withMethods(store => ({
    setLanguage(iso2letterCode: string) {
      console.log('Setting language to:', iso2letterCode);
      patchState(store, { language: iso2letterCode });
    },
    loadUserInfo() {
      console.log('Loading user info');
      new MeApi().getMe().then(user => {
        console.log('User info loaded:', user);
        patchState(store, { user });
      });
    },
  })),
  withHooks({
    onInit(store) {
      store.loadUserInfo();
    },
  })
);
