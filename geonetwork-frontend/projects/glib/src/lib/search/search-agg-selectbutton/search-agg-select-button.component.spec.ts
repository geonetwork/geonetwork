import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ComponentRef } from '@angular/core';
import { SearchAggComponent } from '../search-agg/search-agg.component';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SearchService } from '../search.service';
import { fakeSearchService } from '../search.service.test-helper';
import { SearchStore } from '../search.state';
import { SearchAggSelectButtonComponent } from './search-agg-select-button.component';

describe('SearchAggSelectButtonComponent', () => {
  let component: SearchAggSelectButtonComponent;
  let fixture: ComponentFixture<SearchAggSelectButtonComponent>;
  let componentRef: ComponentRef<SearchAggSelectButtonComponent>;

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

    fixture = TestBed.createComponent(SearchAggSelectButtonComponent);
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
