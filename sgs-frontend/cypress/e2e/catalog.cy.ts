describe('Catálogo de productos', () => {
  beforeEach(() => {
    cy.loginAsTestUser();
    cy.visit('/productos');
  });

  it('C4 Lista de productos muestra cards', () => {
    cy.get('[data-cy=product-grid]').should('be.visible');
    cy.get('[data-cy=product-card]').should('have.length.greaterThan', 0);
  });

  it('C5 Ver detalle de producto muestra nombre y precio', () => {
    cy.get('[data-cy=product-card]').first().within(() => {
      cy.get('.product-name').should('not.be.empty');
      cy.get('.product-price').should('not.be.empty');
    });
    cy.get('[data-cy=product-card]').first().find('.product-name').click();
    cy.url().should('match', /\/productos\/\d+/);
    cy.get('.producto-nombre, h1, h2').first().should('not.be.empty');
  });

  it('C6 Filtrar por categoría muestra solo productos de esa categoría', () => {
    cy.get('[data-cy=category-chip]').first().then(($chip) => {
      const categoriaNombre = $chip.text().trim();
      cy.wrap($chip).click();
      cy.get('[data-cy=product-card]').each(($card) => {
        cy.wrap($card).find('.product-category').invoke('text').then((text) => {
          expect(text.trim()).to.equal(categoriaNombre);
        });
      });
    });
  });
});
