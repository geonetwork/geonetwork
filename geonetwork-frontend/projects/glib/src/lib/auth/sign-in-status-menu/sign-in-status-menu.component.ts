import { Component, inject } from '@angular/core';
import { Button } from 'primeng/button';
import { Popover } from 'primeng/popover';
import { UserBadgeComponent } from '../user-badge/user-badge.component';
import { SignInFormComponent } from '../sign-in-form/sign-in-form.component';
import { AppStore } from '../../app.state';
import { Router } from '@angular/router';

@Component({
  selector: 'g-sign-in-status-menu',
  imports: [Button, Popover, UserBadgeComponent, SignInFormComponent],
  templateUrl: './sign-in-status-menu.component.html',
})
export class SignInStatusMenuComponent {
  readonly app = inject(AppStore);
  router = inject(Router);
}
