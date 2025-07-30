import { inject, Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  GuardResult,
  MaybeAsync,
  Router,
} from '@angular/router';
import { AppStore } from '../app.state';
import { toObservable } from '@angular/core/rxjs-interop';
import { catchError, map, of, filter } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AccessGuard implements CanActivate {
  app = inject(AppStore);
  router = inject(Router);
  isAuthenticated = toObservable(this.app.authenticated);

  canActivate(route: ActivatedRouteSnapshot): MaybeAsync<GuardResult> {
    const requiresLogin = route.data.requiresLogin !== false;
    const redirectTo = route.data.redirectTo || '/signin';

    return this.isAuthenticated.pipe(
      filter(authenticated => authenticated !== undefined),
      map(authenticated => {
        if (requiresLogin && !authenticated) {
          this.router.navigate([redirectTo]);
          return false;
        }
        return true;
      }),
      catchError(() => {
        this.router.navigate([redirectTo]);
        return of(false);
      })
    );
  }
}
