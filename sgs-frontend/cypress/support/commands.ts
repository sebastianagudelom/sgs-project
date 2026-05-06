declare namespace Cypress {
  interface Chainable {
    login(email: string, password: string): Chainable<void>;
    loginAsAdmin(): Chainable<void>;
    loginAsTestUser(): Chainable<void>;
  }
}

Cypress.Commands.add('login', (email: string, password: string) => {
  cy.visit('/login');
  cy.get('[data-cy=login-email]').type(email);
  cy.get('[data-cy=login-password]').type(password);
  cy.get('[data-cy=login-submit]').click();
  cy.url().should('not.include', '/login');
});

Cypress.Commands.add('loginAsAdmin', () => {
  const email = Cypress.env('adminEmail');
  const pass = Cypress.env('adminPass');
  cy.login(email, pass);
});

Cypress.Commands.add('loginAsTestUser', () => {
  const email = Cypress.env('testUserEmail');
  const pass = Cypress.env('testUserPass');
  cy.login(email, pass);
});
