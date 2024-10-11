import { Component, inject } from '@angular/core';
import { ChipModule } from 'primeng/chip';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { AppStore } from '../../app.state';
import { PopoverModule } from 'primeng/popover';
import { ButtonModule } from 'primeng/button';
import { InputGroupModule } from 'primeng/inputgroup';

@Component({
  selector: 'g-sign-in-form',
  standalone: true,
  imports: [
    ChipModule,
    InputGroupAddonModule,
    InputTextModule,
    FormsModule,
    PopoverModule,
    ButtonModule,
    InputGroupModule,
  ],
  templateUrl: './sign-in-form.component.html',
  styleUrl: './sign-in-form.component.css',
})
export class SignInFormComponent {
  readonly app = inject(AppStore);

  username: string = '';
  password: string = '';

  signIn() {
    this.app.signIn(this.username, this.password);
    // TODO: Handle failure
    this.#cleanup();
  }

  #cleanup() {
    this.username = '';
    this.password = '';
  }
}
