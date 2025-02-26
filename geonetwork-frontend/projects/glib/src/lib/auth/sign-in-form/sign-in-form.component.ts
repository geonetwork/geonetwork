import { Component, effect, inject, OnInit, output } from '@angular/core';
import { Button, ButtonDirective } from 'primeng/button';
import { ChipModule } from 'primeng/chip';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { AppStore } from '../../app.state';
import { Message } from 'primeng/message';
import { AuthService } from '../auth.service';
import { AuthProvider } from 'g5api';
import { SeparatorComponent } from '../../ui/separator/separator.component';

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
    ButtonDirective,
    SeparatorComponent,
  ],
  templateUrl: './sign-in-form.component.html',
})
export class SignInFormComponent implements OnInit {
  onSignIn = output<void>();

  readonly app = inject(AppStore);

  authService = inject(AuthService);

  username: string = '';
  password: string = '';

  authProviders: AuthProvider[] = [];

  constructor() {
    effect(() => {
      if (this.app.authenticationFailure() === false) {
        this.#cleanup();
        this.onSignIn && this.onSignIn.emit();
      }
    });
  }

  ngOnInit() {
    this.authService.getAuthProviders().subscribe(authProviders => {
      this.authProviders = authProviders;
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
