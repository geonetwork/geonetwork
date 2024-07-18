import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchPagingComponent } from './search-paging.component';
import { ComponentRef, InjectionToken } from '@angular/core';
import { SearchPagingMoreButtonComponent } from '../search-paging-more-button/search-paging-more-button.component';
import { SearchAggComponent } from '../search-agg/search-agg.component';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SearchService } from '../search.service';
import { fakeSearchService } from '../search.service.test-helper';
import { SearchStore } from '../search.state';

describe('SearchPagingComponent', () => {
  let component: SearchPagingComponent;
  let fixture: ComponentFixture<SearchPagingComponent>;
  let componentRef: ComponentRef<SearchPagingComponent>;

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

    fixture = TestBed.createComponent(SearchPagingComponent);
    component = fixture.componentInstance;
    componentRef = fixture.componentRef;
    componentRef.setInput('scope', 'main');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
