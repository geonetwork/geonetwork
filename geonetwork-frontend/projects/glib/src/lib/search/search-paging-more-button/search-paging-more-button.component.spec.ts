import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchPagingMoreButtonComponent } from './search-paging-more-button.component';
import { InjectionToken } from '@angular/core';

describe('SearchPagingMoreButtonComponent', () => {
  let component: SearchPagingMoreButtonComponent;
  let fixture: ComponentFixture<SearchPagingMoreButtonComponent>;

  beforeEach(async () => {
    const SCOPE = new InjectionToken<string>('scope');
    await TestBed.configureTestingModule({
      imports: [SearchPagingMoreButtonComponent],
      providers: [{ provide: SCOPE, useValue: 'main' }],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchPagingMoreButtonComponent);
    component = fixture.componentInstance;
    component.scope = 'main';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
