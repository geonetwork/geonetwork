import { Component, inject } from '@angular/core';
import { SignInFormComponent } from 'glib';
import { Router } from '@angular/router';

@Component({
  selector: 'gn-signin-page',
  imports: [SignInFormComponent],
  templateUrl: './signin-page.component.html',
  standalone: true,
})
export class SigninPageComponent {
  router = inject(Router);

  routeToPreviousPage() {
    this.router.navigateByUrl('/');
    // window.history.back();
  }
}
