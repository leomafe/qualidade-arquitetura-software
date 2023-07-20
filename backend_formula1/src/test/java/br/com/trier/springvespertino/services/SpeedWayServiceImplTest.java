package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Speedway;
import br.com.trier.springvespertino.repositories.SpeedwayRepository;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Transactional
public class SpeedWayServiceImplTest extends BaseTest {

    @Autowired
    private SpeedwayService service;

    @Test
    @DisplayName("Teste buscar pista por ID")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql"})
    void testFindById() {
        var pista = service.findById(3);
        assertNotNull(pista);
        assertEquals(3, pista.getId());
        assertEquals("Pista Curta", pista.getName());
    }

    @Test
    @DisplayName("Teste buscar pista por ID inexistente")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql"})
    void testFindByIdNonExists() {

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(10));
        assertEquals("Pista 10 não existe", exception.getMessage());
    }

    @Test
    @Sql({"classpath:/sqls/pais.sql",})
    @DisplayName("Teste salvar pista")
    void testInsert() {

        var pista = new Speedway(null, "Pista média", 15, new Country(3, "Brasil"));
        service.insert(pista);
        pista = service.findById(1);
        assertEquals(1, pista.getId());
        assertEquals("Pista média", pista.getName());
    }
    @Test
    @Sql({"classpath:/sqls/pais.sql",})
    @DisplayName("Teste salvar pista com tamanho inválido")
    void testInsertException() {
        var pista = new Speedway(null,"Pista média", 0, new Country(3, "Brasil"));
        var exception = assertThrows(
                IntegrityViolation.class, () -> service.insert(pista));
        assertEquals("Tamanho da pista inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Testes listar todos as pistas cadastradas")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql"})
    void testListAll() {
        List<Speedway> speedways = service.listAll();
        assertTrue(!speedways.isEmpty() && speedways.size() == 2);
    }

    @Test
    @DisplayName("Teste listar todos sem possuir pista cadastrada")
    void testListAllIsEmpty() {

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.listAll());
        assertEquals("Nenhuma pista cadastrada", exception.getMessage());

    }

    @Test
    @DisplayName("Teste alterar pista")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql"})
    void testUpdate() {
        var pista = service.findById(3);
        String nomeAntesAlterar = pista.getName();

        var pistaAlterada = service.update(new Speedway(3, "Pista Nova", 15, new Country(4, "Alemanha")));
        assertEquals("Pista Nova", pistaAlterada.getName());
        assertNotEquals(nomeAntesAlterar, pistaAlterada.getName());
    }

    @Test
    @DisplayName("Teste remover pista")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql"})
    void testDelete() {
        service.delete(3);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(3));
        assertEquals("Pista 3 não existe", exception.getMessage());

    }

    @Test
    @DisplayName("Teste remover pista inexistente")
    void testDeleteNonExists() {

        SpeedwayRepository repositoryMock = mock(SpeedwayRepository.class);
        when(repositoryMock.findById(1)).thenReturn(null);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.delete(1));
        assertEquals("Pista 1 não existe", exception.getMessage());
        verify(repositoryMock, times(0)).delete(null);
    }

    @Test
    @DisplayName("Testes buscar pista pela descrição ignorando case sensitive")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql"})
    void testFindByNameStartsWithIgnoreCase() {

        var pistas = service.findByNameStartsWithIgnoreCase("Pis");
        assertTrue(pistas.size() == 2);

        pistas = service.findByNameStartsWithIgnoreCase("PIST");
        assertTrue(pistas.size() == 2);

        pistas = service.findByNameStartsWithIgnoreCase("Pista");
        assertTrue(pistas.size() == 2);


        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByNameStartsWithIgnoreCase("22 volt"));
        assertEquals("Nenhuma pista cadastrada com esse nome", exception.getMessage());

    }

    @Test
    @DisplayName("Testes buscar pista pelo intervalo de medidas")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql"})
    void testFindBySizeBetween() {

        var pistas = service.findBySizeBetween(10, 12);
        assertTrue(pistas.size() == 1);

        pistas = service.findBySizeBetween(15, 20);
        assertTrue(pistas.size() == 1);

        pistas = service.findBySizeBetween(5, 22);
        assertTrue(pistas.size() == 2);


        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findBySizeBetween(25, 30));
        assertEquals("Nenhuma pista cadastrada com essas medidas", exception.getMessage());

    }

    @Test
    @DisplayName("Testes buscar pista por país ")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/pista.sql"})
    void testFindByCountryOrderBySizeDesc() {

        var pistas = service.findByCountryOrderBySizeDesc(new Country(3,"Brasil"));
        assertTrue(pistas.size() == 1);

        pistas = service.findByCountryOrderBySizeDesc(new Country(4,"Japão"));
        assertTrue(pistas.size() == 1);


        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByCountryOrderBySizeDesc(new Country(1,"Chile")));
        assertEquals("Nenhuma pista cadastrada no país: Chile", exception.getMessage());
    }



}
