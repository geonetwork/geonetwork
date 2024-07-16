import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchAggItemComponent } from './search-agg-item.component';
import { InjectionToken } from '@angular/core';

describe('SearchAggItemComponent', () => {
  let component: SearchAggItemComponent;
  let fixture: ComponentFixture<SearchAggItemComponent>;

  beforeEach(async () => {
    const SCOPE = new InjectionToken<string>('scope');
    await TestBed.configureTestingModule({
      imports: [SearchAggItemComponent],
      providers: [{ provide: SCOPE, useValue: 'main' }],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchAggItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
