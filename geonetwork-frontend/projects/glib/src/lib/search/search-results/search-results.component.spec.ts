import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchStore } from '../search.state';
import { SearchService } from '../search.service';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ComponentRef } from '@angular/core';

import { fakeSearchService } from '../search.service.test-helper';
import { SearchResultsComponent } from './search-results.component';

describe('SearchResultsComponent', () => {
  let component: SearchResultsComponent;
  let fixture: ComponentFixture<SearchResultsComponent>;
  let componentRef: ComponentRef<SearchResultsComponent>;
  let htmlElement: HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SearchResultsComponent,
        SearchBaseComponent,
        FormsModule,
        InputTextModule,
      ],
      providers: [{ provide: SearchService, useValue: fakeSearchService }],
    }).compileComponents();

    TestBed.runInInjectionContext(() => {
      fakeSearchService.register('main', new SearchStore());
    });

    fixture = TestBed.createComponent(SearchResultsComponent);
    component = fixture.componentInstance;
    componentRef = fixture.componentRef;
    componentRef.setInput('scope', 'main');
    fixture.detectChanges();
    htmlElement = fixture.nativeElement;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display search results', () => {
    component.search.search({
      fullTextQuery: 'africa',
      filters: {},
      aggregationConfig: {},
    });
    console.log(htmlElement);
    expect(component).toBeTruthy();
    // let inputElement = htmlElement.getElementsByTagName('input')[0];
    // fakeSearchService.getSearch('main').setFullTextQuery('');
    // expect(inputElement.value).toBe('');
    //
    // fakeSearchService.getSearch('main').setFullTextQuery('africa');
    // fixture.detectChanges();
    // fixture.whenStable().then(() => {
    //   expect(inputElement.value).toBe('africa');
    // });
  });
  //
  // it('should update search state when input field change', () => {
  //   let inputElement = htmlElement.getElementsByTagName('input')[0];
  //   inputElement.focus();
  //   inputElement.value = 'asia';
  //   inputElement.dispatchEvent(new Event('input'));
  //   fixture.detectChanges();
  //   expect(inputElement.value).toBe(component.search.fullTextQuery());
  //   // });
  // });
});
