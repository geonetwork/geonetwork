import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AppStore } from '../app.state';

export const AuthGuard = () => {
  const app = inject(AppStore);

  return app.authenticated() ? true : inject(Router).navigate(['/signin']);
};
