import { Component, input, output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { Button } from 'primeng/button';
import { InputGroup } from 'primeng/inputgroup';
import { AutoFocus } from 'primeng/autofocus';
import { InputGroupAddon } from 'primeng/inputgroupaddon';

@Component({
  selector: 'g-search-input',
  standalone: true,
  imports: [
    FormsModule,
    InputTextModule,
    InputGroup,
    AutoFocus,
    Button,
    InputGroupAddon,
  ],
  templateUrl: './search-input.component.html',
})
export class SearchInputComponent extends SearchBaseComponent {
  styleClass = input<string>('');
  placeholder = input<string>('Search ...');
  autofocus = input<boolean>(false);
  onSearch = output<string>();

  onModelChange($event: string) {
    this.search.setFullTextQuery($event);
    this.onSearch.emit($event);
  }
}
