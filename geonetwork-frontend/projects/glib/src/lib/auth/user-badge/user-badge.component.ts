import { Component, inject } from '@angular/core';
import { Avatar } from 'primeng/avatar';
import { AppStore } from '../../app.state';

@Component({
  selector: 'g-user-badge',
  imports: [Avatar],
  templateUrl: './user-badge.component.html',
})
export class UserBadgeComponent {
  readonly app = inject(AppStore);
}
