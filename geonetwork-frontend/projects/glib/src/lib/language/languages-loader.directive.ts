import { Directive, inject, Optional } from '@angular/core';
import { Dropdown } from 'primeng/dropdown';
import { APPLICATION_CONFIGURATION } from '../config/config.loader';

@Directive({
  selector: '[gLanguagesLoader]',
  standalone: true,
})
export class LanguagesLoaderDirective {
  uiConfiguration = inject(APPLICATION_CONFIGURATION).ui;

  languageList = Object.keys(
    this.uiConfiguration?.mods.header.languages || { eng: 'en' }
  ).map((iso3letter: string) => {
    return {
      name: iso3letter,
      code: this.uiConfiguration?.mods.header.languages[iso3letter],
    };
  });

  constructor(@Optional() private dropdown: Dropdown) {}

  public ngOnInit(): void {
    if (this.dropdown) {
      this.dropdown.options = this.languageList;
      this.dropdown.optionLabel = 'name';
      this.dropdown.modelValue.set(
        // TODO: Set default language
        this.languageList.filter(lang => lang.code === 'en')[0]
      );
    }
  }
}
