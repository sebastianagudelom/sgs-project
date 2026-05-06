describe('Autenticación', () => {
  it('C1 Login exitoso con credenciales válidas', () => {
    cy.loginAsTestUser();
    cy.url().should('include', '/productos');
    cy.get('[data-cy=navbar-cart]').should('be.visible');
  });

  it('C2 Login fallido muestra mensaje de error', () => {
    cy.visit('/login');
    cy.get('[data-cy=login-email]').type('noexiste@sgs.com');
    cy.get('[data-cy=login-password]').type('passwordIncorrecta123');
    cy.get('[data-cy=login-submit]').click();
    cy.get('[data-cy=login-error]').should('be.visible');
    cy.url().should('include', '/login');
  });

  it('C3 Cerrar sesión redirige a login', () => {
    cy.loginAsTestUser();
    cy.url().should('include', '/productos');
    cy.get('[data-cy=navbar-logout]').click({ force: true });
    cy.url().should('include', '/login');
    cy.get('[data-cy=navbar-cart]').should('not.exist');
  });
});
