import { Directive, inject, Optional } from '@angular/core';
import { Dropdown } from 'primeng/dropdown';
import { APPLICATION_CONFIGURATION } from '../config/config.loader';
import { Select } from 'primeng/select';

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

  constructor(@Optional() private select: Select) {}

  public ngOnInit(): void {
    if (this.select) {
      this.select.options = this.languageList;
      this.select.optionLabel = 'name';
      this.select.modelValue.set(
        // TODO: Set default language
        this.languageList.filter(lang => lang.code === 'en')[0]
      );
    }
  }
}
