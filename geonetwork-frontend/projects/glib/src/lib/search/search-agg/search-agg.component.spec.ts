import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchAggComponent } from './search-agg.component';

describe('SearchAggComponent', () => {
  let component: SearchAggComponent;
  let fixture: ComponentFixture<SearchAggComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchAggComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchAggComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
