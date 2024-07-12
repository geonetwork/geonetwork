import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchAggItemComponent } from './search-agg-item.component';

describe('SearchAggItemComponent', () => {
  let component: SearchAggItemComponent;
  let fixture: ComponentFixture<SearchAggItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchAggItemComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchAggItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
