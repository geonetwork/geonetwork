import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchAggMultiselectComponent } from './search-agg-multiselect.component';
import { ComponentRef, InjectionToken } from '@angular/core';
import { SearchPagingComponent } from '../search-paging/search-paging.component';
import { SearchAggComponent, SearchAggLayout } from '../search-agg/search-agg.component';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SearchService } from '../search.service';
import { fakeSearchService } from '../search.service.test-helper';
import { SearchStore } from '../search.state';

describe('SearchAggMultiselectComponent', () => {
  let component: SearchAggMultiselectComponent;
  let fixture: ComponentFixture<SearchAggMultiselectComponent>;
  let componentRef: ComponentRef<SearchAggMultiselectComponent>;

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

    fixture = TestBed.createComponent(SearchAggMultiselectComponent);
    component = fixture.componentInstance;
    componentRef = fixture.componentRef;
    componentRef.setInput('scope', 'main');
    componentRef.setInput('buckets', {});
    componentRef.setInput('field', 'resourceType');
    componentRef.setInput('aggregationConfig', {});
    fixture.detectChanges();
  });
  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
