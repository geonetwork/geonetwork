describe('Search Page Test', () => {
  beforeEach(() => {
    cy.visit('/');

    cy.get('gn-navigation').first().as('navigationMenu');
    cy.get('gn-navigation g-sign-in-status-menu button')
      .first()
      .as('signInButton');
  });

  describe('Sign in form test', () => {
    it('Check sign in button is disabled if no username or password', () => {
      cy.get('@signInButton').click();
      cy.get('form[name=gSignInForm] button').should('be.disabled');
      cy.get('@signInButton').click();
    });

    it('Check sign in and out with form', () => {
      cy.get('@signInButton').click();
      ['username', 'password'].forEach(field => {
        cy.get(`input[name=${field}]`).type('admin');
      });

      cy.get('form[name=gSignInForm] button').should('not.be.disabled');
      cy.intercept('/geonetwork/srv/api/me').as('meApiCall');
      cy.get('form[name=gSignInForm] button')
        .click()
        .then(() => {
          cy.wait('@meApiCall');
          cy.get('g-user-badge p-avatar')
            .first()
            .then($userInfo => {
              expect($userInfo.text().trim()).to.eq('A');
            });

          cy.get('[data-cy=gSignOutButton]').click();
          cy.wait('@meApiCall');
          cy.get('g-user-badge').should('not.exist');
        });
    });
  });
});
