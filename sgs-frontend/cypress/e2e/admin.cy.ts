describe('Panel de administración', () => {
  beforeEach(() => {
    cy.loginAsAdmin();
  });

  it('C55 Login como admin da acceso a rutas de administración', () => {
    cy.visit('/admin/clientes');
    cy.url().should('include', '/admin/clientes');
    cy.get('h2').should('contain.text', 'Clientes');
  });

  it('C56 Gestión de clientes muestra tabla con datos', () => {
    cy.visit('/admin/clientes');
    cy.get('[data-cy=clientes-table-container]').should('be.visible');
    cy.get('[data-cy=cliente-row]').should('have.length.greaterThan', 0);
    cy.get('[data-cy=cliente-row]').first().within(() => {
      cy.get('td').should('have.length.greaterThan', 0);
    });
  });

  it('C57 Alertas de inventario muestra estado del stock', () => {
    cy.visit('/admin/inventario-alertas');
    cy.url().should('include', '/admin/inventario-alertas');
    cy.get('h2').should('contain.text', 'Inventario');
    // Espera a que termine el loading: una de las dos vistas debe estar visible
    cy.get('[data-cy=alerts-list], .empty-state', { timeout: 15000 }).should('be.visible');
  });
});
