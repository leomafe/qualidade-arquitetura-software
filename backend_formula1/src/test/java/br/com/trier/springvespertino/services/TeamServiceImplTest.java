package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.repositories.TeamRepository;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
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
public class TeamServiceImplTest extends BaseTest {

    @Autowired
    private TeamService service;

    @Test
    @DisplayName("Teste salvar equipe")
    void testSalvar() {
        var equipe = new Team(null,"Mclaren");
        service.salvar(equipe);
        equipe = service.findById(1);
        assertEquals(1, equipe.getId());
        assertEquals("Mclaren", equipe.getName());
    }

    @Test
    @DisplayName("Teste salvar equipe já cadastrada")
    @Sql({"classpath:/sqls/equipe.sql"})
    void testSalvarEquipeCadastrada() {
        var equipe = new Team(4,"Ferrari");
        var exception = assertThrows(
                IntegrityViolation.class, () -> service.salvar(equipe));
        assertEquals("Nome já existente: Ferrari", exception.getMessage());

    }

    @Test
    @DisplayName("Teste listar todos")
    @Sql({"classpath:/sqls/equipe.sql"})
    void testListAll() {
        List<Team> lista = service.listAll();
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("Teste listar todos sem possuir equipe cadastrada")
    void testListAllEmpty() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> service.listAll());
        assertEquals("Não existe equipes cadastradas", exception.getMessage());
    }

    @Test
    @DisplayName("Teste buscar equipe por ID")
    @Sql({"classpath:/sqls/equipe.sql"})
    void testFindById() {
        var equipe = service.findById(3);
        assertNotNull(equipe);
        assertEquals(3, equipe.getId());
        assertEquals("Ferrari", equipe.getName());
    }

    @Test
    @DisplayName("Teste buscar equipe por ID inexistente")
    @Sql({"classpath:/sqls/equipe.sql"})
    void testFindByIdNonExists() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(10));
        assertEquals("Equipe 10 não encontrada", exception.getMessage());
    }


    @Test
    @DisplayName("Teste alterar equipe")
    @Sql({"classpath:/sqls/equipe.sql"})
    void testUpdate() {
        var equipe = service.findById(3);
        String nomeAntesAlterar = equipe.getName();

        var equipeAlterada =  service.update(new Team(3,"Mclaren"));
        assertEquals("Mclaren", equipeAlterada.getName());
        assertNotEquals(nomeAntesAlterar, equipeAlterada.getName());
    }

    @Test
    @DisplayName("Teste remover equipe")
    @Sql({"classpath:/sqls/equipe.sql"})
    void testDelete() {
        service.delete(3);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(3));
        assertEquals("Equipe 3 não encontrada", exception.getMessage());

    }

    @Test
    @DisplayName("Teste remover equipe inexistente")
    void testDeleteNonExists() {

        TeamRepository repositoryMock = mock(TeamRepository.class);
        when(repositoryMock.findById(1)).thenReturn(null);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.delete(1));
        assertEquals("Equipe 1 não encontrada", exception.getMessage());
        verify(repositoryMock, times(0)).delete(null);
    }

    @Test
    @DisplayName("Testes buscar equipe pela descrição ignorando case sensitive")
    @Sql({"classpath:/sqls/equipe.sql"})
    void testFindByNomeEqualsIgnoreCase() {

        var equipes = service.findByNameIgnoreCase("Ferrari");
        assertTrue(equipes.size() == 1);

        equipes = service.findByNameIgnoreCase("FERRARI");
        assertTrue(equipes.size() == 1);

        equipes = service.findByNameIgnoreCase("FeRRaRi");
        assertTrue(equipes.size() == 1);


    }

    @Test
    @DisplayName("Testes buscar equipe pela descrição ignorando case sensitive (quando não encontrar)")
    void testFindByNomeEqualsIgnoreCaseEmpty() {

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByNameIgnoreCase("Ferrari"));
        assertEquals("Equipe Ferrari não encontrada", exception.getMessage());

    }

    @Test
    @DisplayName("Testes buscar equipe pela parte de sua descrição")
    @Sql({"classpath:/sqls/equipe.sql"})
    void testFindByNameContains() {

        var equipes = service.findByNameContains("Fe");
        assertTrue(equipes.size() == 1);

        equipes = service.findByNameContains("Fer");
        assertTrue(equipes.size() == 1);

        equipes = service.findByNameContains("Ferra");
        assertTrue(equipes.size() == 1);


    }

    @Test
    @DisplayName("Testes buscar equipe pela parte de sua descrição (quando não encontrar)")
    void testFindByNameContainsEmpty() {

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByNameContains("Mc"));
        assertEquals("Nome Mc não encontrado em nenhuma equipe", exception.getMessage());

    }



}
