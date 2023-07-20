package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.repositories.CountryRepository;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Transactional
public class CountryServiceImplTest extends BaseTest {

    @Autowired
    private CountryService service;

    @Test
    @DisplayName("Teste salvar país")
    void testSalvar() {
        var pais = new Country(null,"Alemanha");
        service.salvar(pais);
        pais = service.findById(1);
        assertEquals(1, pais.getId());
        assertEquals("Alemanha", pais.getName());
    }

    @Test
    @DisplayName("Teste alterar pais")
    @Sql({"classpath:/sqls/pais.sql"})
    void testUpdate() {
        var pais = service.findById(3);
        String nomeAntesAlterar = pais.getName();

        var paisAlterado =  service.update(new Country(3,"Itália"));
        assertEquals("Itália", paisAlterado.getName());
        assertNotEquals(nomeAntesAlterar, paisAlterado.getName());
    }

    @Test
    @DisplayName("Teste remover pais")
    @Sql({"classpath:/sqls/pais.sql"})
    void testDelete() {
        service.delete(3);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(3));
        assertEquals("País não existe", exception.getMessage());

    }

    @Test
    @DisplayName("Teste remover pais inexistente")
    void testDeleteNonExists() {

        CountryRepository repositoryMock = mock(CountryRepository.class);
        when(repositoryMock.findById(1)).thenReturn(null);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.delete(1));
        assertEquals("País não existe", exception.getMessage());
        verify(repositoryMock, times(0)).delete(null);
    }

    @Test
    @DisplayName("Testes listar todos os paises cadastrados")
    @Sql({"classpath:/sqls/pais.sql"})
    void testListAll() {
        List<Country> countries = service.listAll();
        assertTrue(!countries.isEmpty() && countries.size() == 2);
    }

    @Test
    @DisplayName("Teste listar todos sem possuir país cadastrado")
    void testListAllIsEmpty() {

        var paises = service.listAll();
        assertTrue(paises.isEmpty() );
    }

    @Test
    @DisplayName("Teste buscar país por ID")
    @Sql({"classpath:/sqls/pais.sql"})
    void testFindById() {
        var pais = service.findById(3);
        assertNotNull(pais);
        assertEquals(3, pais.getId());
        assertEquals("Brasil", pais.getName());
    }

    @Test
    @DisplayName("Teste buscar país por ID inexistente")
    @Sql({"classpath:/sqls/pais.sql"})
    void testFindByIdNonExists() {

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(10));
        assertEquals("País não existe", exception.getMessage());
    }

    @Test
    @DisplayName("Testes buscar país pela descrição ignorando case sensitive")
    @Sql({"classpath:/sqls/pais.sql"})
    void testFindByNomeEqualsIgnoreCase() {

        var paises = service.findByNomeEqualsIgnoreCase("Brasil");
        assertTrue(paises.size() == 1);

        paises = service.findByNomeEqualsIgnoreCase("BRASIL");
        assertTrue(paises.size() == 1);

        paises = service.findByNomeEqualsIgnoreCase("BrAsIl");
        assertTrue(paises.size() == 1);

        paises = service.findByNomeEqualsIgnoreCase("Chile");
        assertTrue(paises.isEmpty());

    }







}
