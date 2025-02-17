import { computed, inject, Injectable, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API5_CONFIGURATION, API_CONFIGURATION } from '../config/config.loader';
import { LoginEndpointApi } from 'g5api';
import { Configuration, MeApi, SearchApi } from 'gapi';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  api5Configuration = inject(API5_CONFIGURATION);
  apiConfiguration = inject(API_CONFIGURATION);

  http = inject(HttpClient);

  meApi = computed(() => {
    return new MeApi(this.apiConfiguration());
  });

  // loginApi = computed(() => {
  //   return new LoginEndpointApi(this.api5Configuration());
  // });

  signIn(username: string, password: string) {
    return this.http.post(
      this.api5Configuration().basePath + '/api/user/signin',
      `username=${username}&password=${password}`,
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      }
    );

    // return this.loginApi().apiUserSigninPost({apiUserSigninPostRequest: {
    //   username: username,
    //   password: password,
    // }} as ApiUserSigninPostOperationRequest);
  }

  getUser() {
    return this.meApi().getMe();
  }

  signOut() {
    return this.http.get(
      this.api5Configuration().basePath + '/api/user/signout'
    );
  }
}
