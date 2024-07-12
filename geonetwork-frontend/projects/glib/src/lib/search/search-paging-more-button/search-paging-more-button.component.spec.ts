import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchPagingMoreButtonComponent } from './search-paging-more-button.component';

describe('SearchPagingMoreButtonComponent', () => {
  let component: SearchPagingMoreButtonComponent;
  let fixture: ComponentFixture<SearchPagingMoreButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchPagingMoreButtonComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchPagingMoreButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
