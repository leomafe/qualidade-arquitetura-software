describe('trabalho final qualidade arquitetura de software ', () => {
  
  
  it('Teste acessar a pagina', () => {
        cy.visit('https://www.mercadolivre.com.br')
        cy.get('body').should('contain', 'Mercado Livre')
       
    })

    
  it('Deve encontrar resultados para a busca por camiseta', () => {
        // Acessar o site do Mercado Livre
        cy.visit('https://www.mercadolivre.com');
    
        // Encontrar o campo de busca e digitar "camiseta"
        cy.get('.nav-search-input').type('camiseta');
    
        // Clicar no botão de busca
        cy.get('.nav-icon-search').click();
    
        // Verificar se existem resultados
        cy.get('.ui-search-search-result__quantity-results').should('exist');
         
      });
    
})