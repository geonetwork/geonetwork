import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchPagingComponent } from './search-paging.component';
import { InjectionToken } from '@angular/core';

describe('SearchPagingComponent', () => {
  let component: SearchPagingComponent;
  let fixture: ComponentFixture<SearchPagingComponent>;

  beforeEach(async () => {
    const SCOPE = new InjectionToken<string>('scope');
    await TestBed.configureTestingModule({
      imports: [SearchPagingComponent],
      providers: [{ provide: SCOPE, useValue: 'main' }],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchPagingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
