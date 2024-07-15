import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchAggMultiselectComponent } from './search-agg-multiselect.component';

describe('SearchAggMultiselectComponent', () => {
  let component: SearchAggMultiselectComponent;
  let fixture: ComponentFixture<SearchAggMultiselectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchAggMultiselectComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchAggMultiselectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
