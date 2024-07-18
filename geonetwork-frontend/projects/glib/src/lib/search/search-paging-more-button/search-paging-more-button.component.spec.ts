import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchPagingMoreButtonComponent } from './search-paging-more-button.component';
import { ComponentRef, InjectionToken } from '@angular/core';
import { SearchAggItemComponent } from '../search-agg-item/search-agg-item.component';
import { SearchAggComponent } from '../search-agg/search-agg.component';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SearchService } from '../search.service';
import { fakeSearchService } from '../search.service.test-helper';
import { SearchStore } from '../search.state';

describe('SearchPagingMoreButtonComponent', () => {
  let component: SearchPagingMoreButtonComponent;
  let fixture: ComponentFixture<SearchPagingMoreButtonComponent>;
  let componentRef: ComponentRef<SearchPagingMoreButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SearchAggComponent,
        SearchBaseComponent,
        FormsModule,
        InputTextModule,
      ],
      providers: [{ provide: SearchService, useValue: fakeSearchService }],
    }).compileComponents();

    TestBed.runInInjectionContext(() => {
      fakeSearchService.register('main', new SearchStore());
    });

    fixture = TestBed.createComponent(SearchPagingMoreButtonComponent);
    component = fixture.componentInstance;
    componentRef = fixture.componentRef;
    componentRef.setInput('scope', 'main');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
