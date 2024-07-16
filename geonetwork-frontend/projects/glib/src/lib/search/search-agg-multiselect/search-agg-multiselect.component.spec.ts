import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchAggMultiselectComponent } from './search-agg-multiselect.component';
import { InjectionToken } from '@angular/core';

describe('SearchAggMultiselectComponent', () => {
  let component: SearchAggMultiselectComponent;
  let fixture: ComponentFixture<SearchAggMultiselectComponent>;

  beforeEach(async () => {
    const SCOPE = new InjectionToken<string>('scope');
    await TestBed.configureTestingModule({
      imports: [SearchAggMultiselectComponent],
      providers: [{ provide: SCOPE, useValue: 'main' }],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchAggMultiselectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
