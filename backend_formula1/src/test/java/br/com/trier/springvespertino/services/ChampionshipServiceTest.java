package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.repositories.ChampionshipRepository;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
public class ChampionshipServiceTest extends BaseTest {

    @Autowired
    private ChampionshipService service;

    @Test
    @DisplayName("Teste buscar campeonato por ID")
    @Sql({"classpath:/sqls/campeonato.sql"})
    void testFindById() {
        var campeonato = service.findById(3);
        assertNotNull(campeonato);
        assertEquals(3, campeonato.getId());
        assertEquals("Mundial", campeonato.getDescription());
        assertEquals(2022, campeonato.getYear());
    }

    @Test
    @DisplayName("Teste buscar campeonato por ID inexistente")
    @Sql({"classpath:/sqls/campeonato.sql"})
    void testFindByIdNonExists() {

       var campeonato = service.findById(10);
       assertNull(campeonato);
    }

    @Test
    @DisplayName("Teste inserir campeonato")
    void testInsert() {
        var campeonato = new Championship(null,"Mundial interlagos", 2024);
        service.insert(campeonato);
        campeonato = service.findById(1);
        assertEquals(1, campeonato.getId());
        assertEquals("Mundial interlagos", campeonato.getDescription());
        assertEquals(2024, campeonato.getYear());
    }

    @Test
    @DisplayName("Teste inserir campeonato com ano nulo")
    void testInsertYearIsNull() {
        var campeonato = new Championship(null,"Regional",null);
        var exception = assertThrows(
                IntegrityViolation.class, () -> service.insert(campeonato));
        assertEquals("Ano não pode ser nulo", exception.getMessage());

    }

    @Test
    @DisplayName("Teste inserir campeonato com ano inválido")
    void testInsertInvalidYear() {
        var campeonato = new Championship(null,"Regional",1890);
        var exception = assertThrows(
                IntegrityViolation.class, () -> service.insert(campeonato));
        assertEquals("Ano inválido: 1890", exception.getMessage());

    }

    @Test
    @DisplayName("Testes listar todos os campeonatos cadastrados")
    @Sql({"classpath:/sqls/campeonato.sql"})
    void testListAll() {
        List<Championship> championships = service.listAll();
        assertTrue(!championships.isEmpty() && championships.size() == 2);
    }

    @Test
    @DisplayName("Teste listar todos sem possuir campeonato cadastrado")
    void testListAllIsEmpty() {

        var campeonatos = service.listAll();
        assertTrue(campeonatos.isEmpty() );

    }

    @Test
    @DisplayName("Teste alterar campeonato")
    @Sql({"classpath:/sqls/campeonato.sql"})
    void testUpdate() {
        var campeonato = service.findById(3);
        String descricaoAntesAlterar = campeonato.getDescription();
        Integer anoAntesAlterar = campeonato.getYear();

        var campeonatoAlterado =  service.update(new Championship(3,"Mundial interlagos",2023));
        assertEquals("Mundial interlagos", campeonatoAlterado.getDescription());
        assertEquals(2023, campeonatoAlterado.getYear());
        assertNotEquals(descricaoAntesAlterar, campeonatoAlterado.getDescription());
        assertNotEquals(anoAntesAlterar, campeonatoAlterado.getYear());
    }


    @Test
    @DisplayName("Teste remover campeonato")
    @Sql({"classpath:/sqls/campeonato.sql"})
    void testDelete() {
        service.delete(3);
        assertNull(service.findById(3));

    }

    @Test
    @DisplayName("Teste remover campeonato inexistente")
    void testDeleteNonExists() {

        ChampionshipRepository repositoryMock = mock(ChampionshipRepository.class);
        when(repositoryMock.findById(1)).thenReturn(null);
        service.delete(1);
        verify(repositoryMock, times(0)).delete(null);
    }

    @Test
    @DisplayName("Testes buscar campeonato entre anos")
    @Sql({"classpath:/sqls/campeonato.sql"})
    void testFindByYearBetween() {

        var campeonatos = service.findByYearBetween(2022, 2022);
        assertTrue(campeonatos.size() == 1);

        campeonatos = service.findByYearBetween(2022, 2023);
        assertTrue(campeonatos.size() == 2);

        campeonatos = service.findByYearBetween(2024, 2025);
        assertTrue(campeonatos.isEmpty());

    }

    @Test
    @DisplayName("Testes buscar campeonatos por ano")
    @Sql({"classpath:/sqls/campeonato.sql"})
    void testFindByYear() {

        var campeonatos = service.findByYear(2022);
        assertTrue(campeonatos.size() == 1);

        campeonatos = service.findByYear(2023);
        assertTrue(campeonatos.size() == 1);

        campeonatos = service.findByYear(2024);
        assertTrue(campeonatos.isEmpty());

    }

    @Test
    @DisplayName("Testes buscar campeonatos pela descrição ignorando case sensitive")
    @Sql({"classpath:/sqls/campeonato.sql"})
    void testFindByDescriptionContainsIgnoreCase() {

        var campeonatos = service.findByDescriptionContainsIgnoreCase("Mundi");
        assertTrue(campeonatos.size() == 2);

        campeonatos = service.findByDescriptionContainsIgnoreCase("MUNDI");
        assertTrue(campeonatos.size() == 2);

        campeonatos = service.findByDescriptionContainsIgnoreCase("MuNdI");
        assertTrue(campeonatos.size() == 2);

        campeonatos = service.findByDescriptionContainsIgnoreCase("F1");
        assertTrue(campeonatos.isEmpty());

    }

    @Test
    @DisplayName("Testes buscar campeonatos pela descrição ignorando case sensitive")
    @Sql({"classpath:/sqls/campeonato.sql"})
    void tesFindByDescriptionContainsIgnoreCaseAndAnoEquals() {

        var campeonatos = service.findByescriptionContainsIgnoreCaseAndAnoEquals("Mundi", 2022);
        assertTrue(campeonatos.size() == 1);

        campeonatos = service.findByescriptionContainsIgnoreCaseAndAnoEquals("MUNDI",2023);
        assertTrue(campeonatos.size() == 1);

        campeonatos = service.findByescriptionContainsIgnoreCaseAndAnoEquals("MuNdI", 2022);
        assertTrue(campeonatos.size() == 1);

        campeonatos = service.findByescriptionContainsIgnoreCaseAndAnoEquals("Mundial", 2025);
        assertTrue(campeonatos.isEmpty());

        campeonatos = service.findByescriptionContainsIgnoreCaseAndAnoEquals("F1", 2023);
        assertTrue(campeonatos.isEmpty());

    }




}
