import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchAggComponent } from './search-agg.component';
import { ComponentRef } from '@angular/core';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SearchService } from '../search.service';
import { fakeSearchService } from '../search.service.test-helper';
import { SearchStore } from '../search.state';

describe('SearchAggComponent', () => {
  let component: SearchAggComponent;
  let fixture: ComponentFixture<SearchAggComponent>;
  let componentRef: ComponentRef<SearchAggComponent>;

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

    fixture = TestBed.createComponent(SearchAggComponent);
    component = fixture.componentInstance;
    componentRef = fixture.componentRef;
    componentRef.setInput('scope', 'main');
    componentRef.setInput('aggregation', {});
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
