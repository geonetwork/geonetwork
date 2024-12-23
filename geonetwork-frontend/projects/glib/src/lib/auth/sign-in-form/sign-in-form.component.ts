import { Component, inject, output } from '@angular/core';
import { Button } from 'primeng/button';
import { ChipModule } from 'primeng/chip';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { AppStore } from '../../app.state';

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
  ],
  templateUrl: './sign-in-form.component.html',
})
export class SignInFormComponent {
  onSignIn = output<void>();

  readonly app = inject(AppStore);

  username: string = '';
  password: string = '';

  signIn() {
    this.app.signIn(this.username, this.password);
    // TODO: Handle failure
    this.#cleanup();
    this.onSignIn && this.onSignIn.emit();
  }

  #cleanup() {
    this.username = '';
    this.password = '';
  }
}
