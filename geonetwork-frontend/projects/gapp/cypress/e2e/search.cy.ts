describe('Search Page Test', () => {
  beforeEach(() => {
    cy.visit('/search');
    cy.get('input[gsearchquerysetter = "main"]').first().as('searchInput');
    cy.get('g-search-results[scope = "main"]').first().as('searchResults');
  });

  describe('Search Page', () => {
    it('should have default search results on page load', () => {
      cy.get('@searchResults')
        .find('g-record-view-list')
        .should('have.length', 7);
      cy.get('g-search-paging[scope = "main"] .p-paginator-current')
        .first()
        .should('contain.text', 'Showing 1 to 7 ');

      cy.get('@searchInput')
        .focus()
        .invoke('val', 'africa')
        .trigger('keyup', { keypress: 17 })
        .then(() => {
          cy.get('@searchResults').find('a').should('have.length', 1);
        });

      cy.get(
        'g-search-agg[data-cy="agg-cl_maintenanceAndUpdateFrequency.key"]'
      ).should('exist');

      cy.get('g-search-agg[data-cy="agg-tag"]').should('exist');
    });

    it('should filter on search input', () => {
      cy.get('@searchInput')
        .type('africa')
        .then(() => {
          cy.get('@searchResults').find('a').should('have.length', 1);
        });
    });

    it('should find no result for dummy search', () => {
      cy.get('@searchInput')
        .type('zzyyxx')
        .then(() => {
          cy.get('@searchResults').find('a').should('have.length', 0);
        });
    });

    it('should filter on aggregation click', () => {
      let agg = cy.get('g-search-agg[data-cy="agg-cl_maintenanceAndUpdateFrequency.key"]').find('g-search-agg-item').first();
      let aggCount = '';
      agg.then($value => {
        aggCount = $value
          .text()
          .replace(/.* \(([0-9]+)\)/, '$1')
          .trim();
      });

      agg.find('input[type=checkbox]').click({force: true}).then(() => {
        cy.get('@searchResults').find('a').should('have.length', aggCount);
      });

      cy.get('g-search-agg[data-cy="agg-cl_maintenanceAndUpdateFrequency.key"]').find('input[type=checkbox]').click({force: true});
    });
  });
});
