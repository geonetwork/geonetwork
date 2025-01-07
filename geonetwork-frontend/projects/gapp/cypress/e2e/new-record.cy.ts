describe('New record page test', () => {
  beforeEach(() => {});

  describe('Create new record', () => {
    it('From a dataset template', () => {
      cy.signIn('admin', 'admin');
      cy.visit('/new-record');

      cy.get('[data-cy="gn-new-record-button-step-data"] button').should(
        'be.disabled'
      );
      cy.get('[data-cy="gn-new-record-button-edit"] button').should(
        'be.disabled'
      );
      cy.get('#gn-record-ee94ea60-2645-496c-921a-4e5649214cdf')
        .click()
        .then(() => {
          cy.get('[data-cy="gn-new-record-button-step-data"] button').should(
            'not.be.disabled'
          );
          cy.get('[data-cy="gn-new-record-button-edit"] button').should(
            'not.be.disabled'
          );
        });
    });
  });
});
