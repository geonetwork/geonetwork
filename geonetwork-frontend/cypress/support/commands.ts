declare namespace Cypress {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  interface Chainable<Subject> {
    signIn(username?: string, password?: string): void
    signInWithApi(username?: string, password?: string): void
  }
}

Cypress.Commands.add("signInWithApi", (username, password) => {
  cy.session(
    [username, password],
    () => {
      cy.request({
        method: 'POST',
        url: '/api/user/signin',
        body: `username=${username}&password=${password}`,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        followRedirect: false,
      }).then((response) => {
          expect(response.status).to.eq(302);
          expect(response.redirectedToUrl).not.contains('error');
        });
    },
    {
      validate() {
        cy.request({
          method: 'GET',
          url: '/geonetwork/srv/api/me',
          headers: {
            Accept: 'application/json',
          },
        }).then((response) => {
          expect(response.status).to.eq(200);
          expect(response.body.username).to.eq(username);
        });
      },
    }
  )
})


Cypress.Commands.add("signIn", (username, password) => {
  // cy.session(
  //   [username, password],
  //   () => {
      cy.visit('/');
      cy.get('gn-navigation g-sign-in-status-menu button').first().as('signInButton');
      cy.get('@signInButton').click();
      ['username', 'password'].forEach(field => {
        cy.get(`input[name=${field}]`).type('admin');
      });

      cy.get('form[name=gSignInForm] button').should(
        'not.be.disabled'
      );
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
        });
    },
    {
      validate() {

      },
    // }
  // )
})

