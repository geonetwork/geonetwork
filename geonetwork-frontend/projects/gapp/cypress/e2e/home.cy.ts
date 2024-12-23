describe('Search Page Test', () => {
  beforeEach(() => {
    cy.visit('/');

    cy.get('gn-navigation').first().as('navigationMenu');
    cy.get('gn-navigation p-select[glanguagesloader]')
      .first()
      .as('languageDropdown');
    cy.get('gn-navigation g-sign-in-form').first().as('signInForm');
  });

  describe('Header and navigation', () => {
    it('Visits the home page and check header content', () => {
      cy.visit('/');
      cy.contains('My GeoNetwork');
    });

    it('Check navigation bar content', () => {
      cy.get('@navigationMenu')
        .find('li[role=menuitem]')
        .should('have.length', 2);
      cy.get('@signInForm').should('exist');
    });

    it('Check language selector menu content', () => {
      cy.get('@languageDropdown')
        .click()
        .find('p-dropdownitem')
        .should('have.length', 23);
      cy.get('@languageDropdown').click();
    });

    it('Check sign in button is disabled if no username or password', () => {
      cy.get('@signInForm').click();
      cy.get('form[name=gSignInForm] button').should('attr', 'disabled');
      cy.get('@signInForm').click();
    });

    it('Check sign in and out with form', () => {
      cy.get('@signInForm').click();
      ['username', 'password'].forEach(field => {
        cy.get(`input[name=${field}]`).type('admin');
      });

      cy.get('form[name=gSignInForm] button').should(
        'not.have.attr',
        'disabled'
      );
      cy.intercept('/geonetwork/srv/api/me').as('meApiCall');
      cy.get('form[name=gSignInForm] button')
        .click()
        .then(() => {
          cy.wait('@meApiCall');
          cy.get('[data-cy=gUserInfo]')
            .first()
            .then($userInfo => {
              expect($userInfo.text().trim()).to.eq('admin');
            });

          cy.get('[data-cy=gSignOutButton]').click();
          cy.wait('@meApiCall');
          cy.get('[data-cy=gUserInfo]').should('not.exist');
        });
    });
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
