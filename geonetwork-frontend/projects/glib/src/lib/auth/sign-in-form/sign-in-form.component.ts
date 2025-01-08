import { Component, effect, inject, output } from '@angular/core';
import { Button } from 'primeng/button';
import { ChipModule } from 'primeng/chip';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { AppStore } from '../../app.state';
import { Message } from 'primeng/message';

@Component({
  selector: 'g-sign-in-form',
  standalone: true,
  imports: [
    ChipModule,
    InputGroupModule,
    InputGroupAddonModule,
    InputTextModule,
    FormsModule,
    Button,
    Message,
  ],
  templateUrl: './sign-in-form.component.html',
})
export class SignInFormComponent {
  onSignIn = output<void>();

  readonly app = inject(AppStore);

  username: string = '';
  password: string = '';

  constructor() {
    effect(() => {
      if (this.app.authenticationFailure() === false) {
        this.#cleanup();
        this.onSignIn && this.onSignIn.emit();
      }
    });
  }

  signIn() {
    this.app.signIn(this.username, this.password);
  }

  #cleanup() {
    this.username = '';
    this.password = '';
  }
}
