import { Component, inject } from '@angular/core';
import { Button, ButtonDirective } from 'primeng/button';
import { ChipModule } from 'primeng/chip';
import { OverlayPanel, OverlayPanelModule } from 'primeng/overlaypanel';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { AppStore } from '../../app.state';

@Component({
  selector: 'g-sign-in-form',
  standalone: true,
  imports: [
    Button,
    ChipModule,
    OverlayPanelModule,
    InputGroupModule,
    InputGroupAddonModule,
    InputTextModule,
    FormsModule,
    ButtonDirective,
  ],
  templateUrl: './sign-in-form.component.html',
  styleUrl: './sign-in-form.component.css',
})
export class SignInFormComponent {
  readonly app = inject(AppStore);

  username: string = '';
  password: string = '';

  signIn(op: OverlayPanel) {
    this.app.signIn(this.username, this.password);
    // TODO: Handle failure
    this.#cleanup();
  }

  #cleanup() {
    this.username = '';
    this.password = '';
  }
}
