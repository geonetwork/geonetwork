describe('Search Page Test', () => {
  beforeEach(() => {
    cy.visit('/');

    cy.get('input[gsearchquerysetter = "main"]').first().as('searchInput');
    cy.get('g-search-results[scope = "main"]').first().as('searchResults');
    cy.get('g-search-results[scope = "main"] > p')
      .first()
      .as('searchResultsCount');
    cy.get('button[gsearchqueryreset = "main"').first().as('searchResetButton');
    cy.get('g-search-agg[aggregation="resourceType"][scope="main"]')
      .first()
      .as('aggResourceType');
    cy.get(
      'g-search-agg[aggregation="cl_maintenanceAndUpdateFrequency.key"][scope="main"]'
    )
      .first()
      .as('aggMaintenanceFrequency');
    cy.get('g-search-agg[aggregation="tag"][scope="main"]')
      .first()
      .as('aggTag');
  });

  describe('Header Test', () => {
    it('Visits the initial project page', () => {
      cy.visit('/');
      cy.contains('My GeoNetwork');
    });
  });

  describe('Search Page', () => {
    it('should have default search results on page load', () => {
      cy.get('@searchResults').find('a').should('have.length', 10);
      cy.get('@searchResultsCount').should('contain.text', 77);

      cy.get('@searchInput')
        .focus()
        .invoke('val', 'africa')
        .trigger('keyup', { keypress: 17 })
        .then(() => {
          cy.get('@searchResults').find('a').should('have.length', 1);
        });

      cy.get('@aggResourceType')
        .find('g-search-agg-item')
        .should('have.length', 5);
      cy.get('@aggMaintenanceFrequency').should('exist');
      cy.get('@aggTag').should('exist');
    });

    it('should filter on search input', () => {
      cy.get('@searchInput')
        .focus()
        .invoke('val', 'africa')
        .trigger('keyup', { keypress: 17 })
        .then(() => {
          cy.get('@searchResults').find('a').should('have.length', 1);
        });
    });

    it('should find no result for dummy search', () => {
      cy.get('@searchInput')
        .focus()
        .invoke('val', 'zzyyxx')
        .trigger('keyup', { keypress: 17 })
        .then(() => {
          cy.get('@searchResults').find('a').should('have.length', 0);
        });
    });

    it('should filter on aggregation click', () => {
      let agg = cy.get('@aggResourceType').find('g-search-agg-item').first();
      let aggCount = '';
      agg.then($value => {
        aggCount = $value
          .text()
          .replace(/.* \(([0-9]+)\)/, '$1')
          .trim();
      });

      agg.find('p-chip').click();
      cy.get('@searchResultsCount').should('contain.text', aggCount);
    });
  });
});
