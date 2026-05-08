describe('Carrito y pedido', () => {
  beforeEach(() => {
    cy.loginAsTestUser();
    cy.visit('/productos');
  });

  it('C52 Agregar producto al carrito incrementa el contador', () => {
    cy.get('[data-cy=btn-agregar-carrito]').first().click();
    cy.get('[data-cy=cart-badge]').should('be.visible');
    cy.get('[data-cy=cart-badge]').invoke('text').then((text) => {
      expect(parseInt(text.trim())).to.be.greaterThan(0);
    });
  });

  it('C53 Ver carrito muestra producto y precio', () => {
    cy.get('[data-cy=btn-agregar-carrito]').first().click();
    cy.visit('/carrito');
    cy.get('[data-cy=carrito-items]').should('be.visible');
    cy.get('[data-cy=carrito-item]').should('have.length.greaterThan', 0);
    cy.get('[data-cy=carrito-item]').first().within(() => {
      cy.get('h3').should('not.be.empty');
      cy.get('.item-precio-unit').should('not.be.empty');
    });
  });

  it('C54 Botón de pago está disponible con producto y dirección', () => {
    cy.get('[data-cy=btn-agregar-carrito]').first().click();
    cy.visit('/carrito');
    cy.get('[data-cy=carrito-item]').should('have.length.greaterThan', 0);
    cy.get('[data-cy=btn-checkout]').should('exist');
    cy.get('[data-cy=btn-checkout]').should('be.disabled');
    cy.get('textarea#direccion').type('Cra 15 #10-20, Armenia, Quindío');
    cy.get('[data-cy=btn-checkout]').should('not.be.disabled');
  });
});
