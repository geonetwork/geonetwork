import { computed, inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API5_CONFIGURATION } from '../config/config.loader';
import { LoginEndpointApi } from 'g5api';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  api5Configuration = inject(API5_CONFIGURATION);

  http = inject(HttpClient);

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

  signOut() {
    return this.http.get(
      this.api5Configuration().basePath + '/api/user/signout'
    );
  }
}
