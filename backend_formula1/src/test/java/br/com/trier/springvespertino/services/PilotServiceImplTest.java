package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.repositories.PilotRepository;
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
public class PilotServiceImplTest extends BaseTest {

    @Autowired
    private PilotService service;

    @Test
    @DisplayName("Teste buscar piloto por ID")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql"})
    void testFindById() {
        var piloto = service.findById(3);
        assertNotNull(piloto);
        assertEquals(3, piloto.getId());
        assertEquals("Leonardo", piloto.getName());
    }

    @Test
    @DisplayName("Teste buscar piloto por ID inexistente")
    @Sql({"classpath:/sqls/pais.sql", "classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql"})
    void testFindByIdNonExists() {

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(10));
        assertEquals("Piloto 10 não existe", exception.getMessage());
    }

    @Test
    @DisplayName("Teste salvar piloto")
    @Sql({"classpath:/sqls/pais.sql", "classpath:/sqls/equipe.sql"})
    void testInsert() {
        var piloto = new Pilot(null, "Ayrton Senna", new Country(3, "Brasil"), new Team(3, "Ferrari"));
        service.insert(piloto);
        piloto = service.findById(1);
        assertEquals(1, piloto.getId());
        assertEquals("Ayrton Senna", piloto.getName());
    }

    @Test
    @DisplayName("Testes listar todos os pilotos cadastrados")
    @Sql({"classpath:/sqls/pais.sql", "classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql"})
    void testListAll() {
        List<Pilot> pilots = service.listAll();
        assertTrue(!pilots.isEmpty() && pilots.size() == 2);
    }

    @Test
    @DisplayName("Teste listar todos sem possuir piloto cadastrado")
    void testListAllIsEmpty() {

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.listAll());
        assertEquals("Nenhum piloto cadastrado", exception.getMessage());;

    }

    @Test
    @DisplayName("Teste alterar piloto")
    @Sql({"classpath:/sqls/pais.sql", "classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql"})
    void testUpdate() {
        var piloto = service.findById(3);
        String nomeAntesAlterar = piloto.getTeam().getName();

        var pilotoAlterado = new Pilot(3, "Leonardo", new Country(3, "Brasil"), new Team(4, "Red Bull"));
        pilotoAlterado =  service.update(pilotoAlterado);
        assertEquals("Red Bull", pilotoAlterado.getTeam().getName());
        assertNotEquals(nomeAntesAlterar, pilotoAlterado.getTeam().getName());
    }

    @Test
    @DisplayName("Teste alterar piloto inexistente")
    void testUpdateNonExists() {

        PilotRepository repositoryMock = mock(PilotRepository.class);
        when(repositoryMock.findById(1)).thenReturn(null);

        var piloto = new Pilot(1, "Ayrton Senna", new Country(3, "Brasil"), new Team(4, "Red Bull"));
        var exception = assertThrows(
                ObjectNotFound.class, () -> service.update(piloto));
        assertEquals("Piloto 1 não existe", exception.getMessage());
        verify(repositoryMock, times(0)).save(piloto);
    }

    @Test
    @DisplayName("Teste remover piloto")
    @Sql({"classpath:/sqls/pais.sql", "classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql"})
    void testDelete() {
        service.delete(3);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(3));
        assertEquals("Piloto 3 não existe", exception.getMessage());

    }

    @Test
    @DisplayName("Testes buscar piloto pela descrição ignorando case sensitive")
    @Sql({"classpath:/sqls/pais.sql", "classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql"})
    void testFindByNameStartsWithIgnoreCase() {

        var pilotos = service.findByNameStartsWithIgnoreCase("Leo");
        assertTrue(pilotos.size() == 1);

        pilotos = service.findByNameStartsWithIgnoreCase("LEO");
        assertTrue(pilotos.size() == 1);

        pilotos = service.findByNameStartsWithIgnoreCase("cLAV");
        assertTrue(pilotos.size() == 1);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByNameStartsWithIgnoreCase("Ayrton"));
        assertEquals("Nenhum piloto com esse nome", exception.getMessage());

    }

    @Test
    @DisplayName("Testes buscar piloto pelo país")
    @Sql({"classpath:/sqls/pais.sql", "classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql"})
    void testFindByCountry() {

        var pilotos = service.findByCountry(new Country(3, "Brasil"));
        assertTrue(pilotos.size() == 1);

         pilotos = service.findByCountry(new Country(4, "Japão"));
        assertTrue(pilotos.size() == 1);


        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByCountry(new Country(1,"Chile")));
        assertEquals("Nenhum piloto nesse país", exception.getMessage());

    }

    @Test
    @DisplayName("Testes buscar piloto pela equipe")
    @Sql({"classpath:/sqls/pais.sql", "classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql"})
    void testFindByTeam() {

        var pilotos = service.findByTeam(new Team(3, "Ferrari"));
        assertTrue(pilotos.size() == 1);

        pilotos = service.findByTeam(new Team(4, "Red Bull"));
        assertTrue(pilotos.size() == 1);


        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByTeam(new Team(1,"Mclaren")));
        assertEquals("Nenhum piloto nesse time", exception.getMessage());

    }




}
