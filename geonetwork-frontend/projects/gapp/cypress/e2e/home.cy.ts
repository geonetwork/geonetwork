describe('Search Page Test', () => {
  beforeEach(() => {
    cy.visit('/');

    cy.get('gn-navigation').first().as('navigationMenu');
    // cy.get('gn-navigation p-select[glanguagesloader]')
    //   .first()
    //   .as('languageDropdown');
    cy.get('gn-navigation g-sign-in-status-menu button')
      .first()
      .as('signInButton');
  });

  describe('Header and navigation', () => {
    it('Visits the home page and check header content', () => {
      cy.visit('/');
      cy.title().should('eq', 'My GeoNetwork');
    });

    it('Check navigation bar content', () => {
      // cy.get('@navigationMenu')
      //   .find('li[role=menuitem]')
      //   .should('have.length', 2);
      cy.get('@signInButton').should('exist');
    });

    // it('Check language selector menu content', () => {
    //   cy.get('@languageDropdown')
    //     .click()
    //     .find('p-dropdownitem')
    //     .should('have.length', 23);
    //   cy.get('@languageDropdown').click();
    // });
  });

  describe('Highlights and numbers', () => {
    it('Visits the home page and check highlights and numbers', () => {
      cy.visit('/');
      cy.get('[gsearchcontext=highlights]').should('exist');
      cy.get(
        '[gsearchcontext=highlights] p-timeline div.p-timeline-event'
      ).should('have.length', 5);
    });
  });
});
