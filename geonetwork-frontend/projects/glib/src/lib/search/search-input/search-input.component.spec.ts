import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchStore } from '../search.state';
import { SearchService } from '../search.service';
import { SearchInputComponent } from './search-input.component';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ComponentRef } from '@angular/core';

import { fakeSearchService } from '../search.service.test-helper';

describe('SearchInputComponent', () => {
  let component: SearchInputComponent;
  let fixture: ComponentFixture<SearchInputComponent>;
  let componentRef: ComponentRef<SearchInputComponent>;
  let htmlElement: HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SearchInputComponent,
        SearchBaseComponent,
        FormsModule,
        InputTextModule,
      ],
      providers: [{ provide: SearchService, useValue: fakeSearchService }],
    }).compileComponents();

    TestBed.runInInjectionContext(() => {
      fakeSearchService.register('main', new SearchStore());
    });

    fixture = TestBed.createComponent(SearchInputComponent);
    component = fixture.componentInstance;
    componentRef = fixture.componentRef;
    componentRef.setInput('scope', 'main');
    fixture.detectChanges();
    htmlElement = fixture.nativeElement;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have one input field', () => {
    expect(htmlElement.getElementsByTagName('input').length).toBe(1);
  });

  it('should update input when search state full text query change', () => {
    let inputElement = htmlElement.getElementsByTagName('input')[0];
    fakeSearchService.getSearch('main').setFullTextQuery('');
    expect(inputElement.value).toBe('');

    fakeSearchService.getSearch('main').setFullTextQuery('africa');
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(inputElement.value).toBe('africa');
    });
  });

  it('should update search state when input field change', () => {
    let inputElement = htmlElement.getElementsByTagName('input')[0];
    inputElement.focus();
    inputElement.value = 'asia';
    inputElement.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    expect(inputElement.value).toBe(component.search.fullTextQuery());
  });
});
