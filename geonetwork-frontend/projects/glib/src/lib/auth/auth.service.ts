import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  http = inject(HttpClient);

  constructor() {}

  signIn(username: string, password: string) {
    return this.http.post(
      '/geonetwork/signin',
      `username=${username}&password=${password}&_csrf=437b0875-db73-4785-8ad3-1b7919c7f0da`,
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      }
    );
  }

  signOut() {
    return this.http.get('/geonetwork/signout');
  }
}
