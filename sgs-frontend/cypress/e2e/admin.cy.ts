describe('Panel de administración', () => {
  beforeEach(() => {
    cy.loginAsAdmin();
  });

  it('C10 Login como admin da acceso a rutas de administración', () => {
    cy.visit('/admin/clientes');
    cy.url().should('include', '/admin/clientes');
    cy.get('h2').should('contain.text', 'Clientes');
  });

  it('C11 Gestión de clientes muestra tabla con datos', () => {
    cy.visit('/admin/clientes');
    cy.get('[data-cy=clientes-table-container]').should('be.visible');
    cy.get('[data-cy=cliente-row]').should('have.length.greaterThan', 0);
    cy.get('[data-cy=cliente-row]').first().within(() => {
      cy.get('td').should('have.length.greaterThan', 0);
    });
  });

  it('C12 Alertas de inventario muestra estado del stock', () => {
    cy.visit('/admin/inventario-alertas');
    cy.url().should('include', '/admin/inventario-alertas');
    cy.get('h2').should('contain.text', 'Inventario');
    cy.get('body').then(($body) => {
      if ($body.find('[data-cy=alerts-list]').length > 0) {
        cy.get('[data-cy=alerts-list]').should('be.visible');
        cy.get('[data-cy=alert-card]').should('have.length.greaterThan', 0);
      } else {
        cy.get('.empty-state').should('be.visible');
      }
    });
  });
});
