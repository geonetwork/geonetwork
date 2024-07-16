import { SearchInputComponent } from './search-input.component';
import {input, signal} from '@angular/core';
import { SearchService } from '../search.service';
import {SearchStoreType} from "../search.state";


describe('SearchInputComponent', () => {
  let scope = signal('main');
  it('mounts', () => {
    // see: https://on.cypress.io/mounting-angular
    const SearchMock = {
      fullTextQuery: () => {
        return signal('africa');
      },
      setFullTextQuery: () => {},
    }
    // cy.stub(SearchMock, 'fullTextQuery').returns(signal('africa'));
    // cy.spy(SearchMock, 'setFullTextQuery');

    const SearchServiceMock = {
      getSearch: () => {},
    };
    cy.stub(SearchServiceMock, 'getSearch').returns((id: string) => {
      cy.log('mock', id);
      // cy.log('mock', SearchMock);
      return SearchMock
    });


    // cy.spy(SearchServiceMock, 'setFullTextQuery');

    cy.mount(SearchInputComponent, {
      componentProperties: {
        scope: signal('main'),
      },
      providers: [{ provide: SearchService, useValue: SearchServiceMock }],
    });
  });
});
