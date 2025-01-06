import { Component, inject } from '@angular/core';
import { Button, ButtonDirective, ButtonIcon } from 'primeng/button';
import { Popover } from 'primeng/popover';
import { UserBadgeComponent } from '../user-badge/user-badge.component';
import { SignInFormComponent } from '../sign-in-form/sign-in-form.component';
import { AppStore } from '../../app.state';

@Component({
  selector: 'g-sign-in-status-menu',
  imports: [
    Button,
    ButtonDirective,
    Popover,
    UserBadgeComponent,
    SignInFormComponent,
    ButtonIcon,
  ],
  templateUrl: './sign-in-status-menu.component.html',
})
export class SignInStatusMenuComponent {
  readonly app = inject(AppStore);
}
