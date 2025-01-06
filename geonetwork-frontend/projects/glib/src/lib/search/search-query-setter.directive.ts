import {
  Directive,
  HostListener,
  inject,
  input,
  OnInit,
  Optional,
} from '@angular/core';
import { SearchService } from './search.service';
import { SearchStoreType } from './search.state';
import { Select } from 'primeng/select';
import { InputText } from 'primeng/inputtext';
import { NgModel } from '@angular/forms';

@Directive({
  selector: '[gSearchQuerySetter]',
  standalone: true,
})
export class SearchQuerySetterDirective implements OnInit {
  scope = input<string>('', { alias: 'gSearchQuerySetter' });
  searchService = inject(SearchService);
  search: SearchStoreType;

  constructor(@Optional() private inputText: InputText) {}

  ngOnInit() {
    this.search = this.searchService.getSearch(this.scope());
    if (this.inputText) {
      this.inputText.ngModel.model = this.search.fullTextQuery;
      // this.inputText.ngModel = new NgModel(this.search.fullTextQuery);
    }
  }

  @HostListener('keyup', ['$event'])
  public onKeyup(event: KeyboardEvent): void {
    const value = (event.target as HTMLInputElement).value;
    this.search.setFullTextQuery(value);
  }
}
