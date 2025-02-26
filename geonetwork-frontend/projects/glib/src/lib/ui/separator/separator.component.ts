import { Component, input } from '@angular/core';

@Component({
  selector: 'g-separator',
  imports: [],
  templateUrl: './separator.component.html',
})
export class SeparatorComponent {
  label = input('');
}
