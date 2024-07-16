import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchAggComponent } from './search-agg.component';
import { InjectionToken } from '@angular/core';

describe('SearchAggComponent', () => {
  let component: SearchAggComponent;
  let fixture: ComponentFixture<SearchAggComponent>;

  beforeEach(async () => {
    const SCOPE = new InjectionToken<string>('scope');
    await TestBed.configureTestingModule({
      imports: [SearchAggComponent],
      providers: [{ provide: SCOPE, useValue: 'main' }],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchAggComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
