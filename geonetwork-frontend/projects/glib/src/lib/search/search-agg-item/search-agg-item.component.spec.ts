import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchAggItemComponent } from './search-agg-item.component';
import { SearchAggComponent, SearchAggLayout } from '../search-agg/search-agg.component';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SearchService } from '../search.service';
import { fakeSearchService } from '../search.service.test-helper';
import { SearchStore } from '../search.state';
import { ComponentRef } from '@angular/core';

describe('SearchAggItemComponent', () => {
  let component: SearchAggItemComponent;
  let fixture: ComponentFixture<SearchAggItemComponent>;
  let componentRef: ComponentRef<SearchAggItemComponent>;

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

    fixture = TestBed.createComponent(SearchAggItemComponent);
    component = fixture.componentInstance;
    componentRef = fixture.componentRef;
    componentRef.setInput('scope', 'main');
    componentRef.setInput('bucket', {});
    componentRef.setInput('field', 'resourceType');
    componentRef.setInput('layout', SearchAggLayout.CHECKBOX);
    componentRef.setInput('aggregationConfig', {});
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
