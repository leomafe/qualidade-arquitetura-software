package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.models.*;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class PilotRaceServiceImplTest extends BaseTest {


    @Autowired
    private PilotRaceService service;

    @Test
    @DisplayName("Teste buscar piloto_corrida por ID")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testFindById() {
        var pilotoCorrida = service.findById(3);
        assertNotNull(pilotoCorrida);
        assertEquals(3, pilotoCorrida.getId());
        assertEquals("Leonardo", pilotoCorrida.getPilot().getName());
    }

    @Test
    @DisplayName("Teste buscar piloto_corrida por ID inexistente")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testFindByIdNonExists() {

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(10));
        assertEquals("ID 10 inválido!", exception.getMessage());
    }

    @Test
    @DisplayName("Teste salvar piloto_corrida")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testInsert() {
        var pilotoCorrida = new PilotRace(null, 3,
                new Pilot(3, "Leonardo", new Country(3,"Brasil"), new Team(3, "Ferrari")),
                new Race(3, ZonedDateTime.now(),new Speedway(3,"Pista Curta",10, new Country(3, "Brasil")),new Championship(3, "Mundial", 2023)));
        service.insert(pilotoCorrida);
        pilotoCorrida = service.findById(1);
        assertEquals(1, pilotoCorrida.getId());
        assertEquals("Leonardo", pilotoCorrida.getPilot().getName());
    }

    @Test
    @DisplayName("Teste salvar piloto_corrida com valor da posicao nulo e zero ")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testInsertException() {
        var pilotoCorridaColocacaoNula = new PilotRace(null, null,
                new Pilot(3, "Leonardo", new Country(3,"Brasil"), new Team(3, "Ferrari")),
                new Race(3, ZonedDateTime.now(),new Speedway(3,"Pista Curta",10, new Country(3, "Brasil")),new Championship(3, "Mundial", 2023)));


        var exception = assertThrows(
                IntegrityViolation.class, () -> service.insert(pilotoCorridaColocacaoNula));
        assertEquals("Colocacao null!", exception.getMessage());


        var pilotoCorridaColocacaoZero = new PilotRace(null, 0,
                new Pilot(3, "Leonardo", new Country(3,"Brasil"), new Team(3, "Ferrari")),
                new Race(3, ZonedDateTime.now(),new Speedway(3,"Pista Curta",10, new Country(3, "Brasil")),new Championship(3, "Mundial", 2023)));


        exception = assertThrows(
                IntegrityViolation.class, () -> service.insert(pilotoCorridaColocacaoZero));
        assertEquals("Colocacao zero!", exception.getMessage());


    }

    @Test
    @DisplayName("Testes listar todos os pilotos_corridas cadastrado")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testListAll() {
        List<PilotRace> pilotRaces = service.listAll();
        assertTrue(!pilotRaces.isEmpty() && pilotRaces.size() == 2);
    }

    @Test
    @DisplayName("Teste listar todos sem possuir pilotos_corridas cadastrado")
    void testListAllIsEmpty() {

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.listAll());
        assertEquals("Nenhum PilotoCorrida cadastrado!", exception.getMessage());;

    }

    @Test
    @DisplayName("Teste alterar piloto_corrida")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testUpdate() {
        var pilotoCorrida = service.findById(3);
        Integer posicaoAntesAlterar = pilotoCorrida.getPlacement();

        var pilotoCorridaAlterado =  new PilotRace(3, 3,
                new Pilot(3, "Leonardo", new Country(3,"Brasil"), new Team(3, "Ferrari")),
                new Race(3, ZonedDateTime.now(),new Speedway(3,"Pista Curta",10, new Country(3, "Brasil")),new Championship(3, "Mundial", 2023)));
        pilotoCorridaAlterado =  service.update(pilotoCorridaAlterado);
        assertEquals(3, pilotoCorridaAlterado.getPlacement());
        assertNotEquals(posicaoAntesAlterar, pilotoCorridaAlterado.getPlacement());
    }

    @Test
    @DisplayName("Teste remover piloto")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testDelete() {
        service.delete(3);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findById(3));
        assertEquals("ID 3 inválido!", exception.getMessage());

    }

    @Test
    @DisplayName("Testes buscar piloto_corrida pela posicao")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testFindByPlacement() {

        var pilotosCorrida = service.findByPlacement(1);
        assertTrue(pilotosCorrida.size() == 1);

        pilotosCorrida = service.findByPlacement(2);
        assertTrue(pilotosCorrida.size() == 1);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByPlacement(3));
        assertEquals("Nenhum PilotoCorrida nesta posição!", exception.getMessage());

    }


    @Test
    @DisplayName("Testes buscar piloto_corrida pelo piloto")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testFindByPilot() {

        var pilotosCorrida = service.findByPilot(new Pilot(3, "Leonardo", new Country(3,"Brasil"), new Team(3, "Ferrari")));
        assertTrue(pilotosCorrida.size() == 1);

        pilotosCorrida = service.findByPilot(new Pilot(4, "Clavison", new Country(4,"Japão"), new Team(4, "Red Bull")));
        assertTrue(pilotosCorrida.size() == 1);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByPilot(new Pilot(1, "Ayrton Senna", new Country(4,"Japão"), new Team(4, "Red Bull"))));
        assertEquals("Nenhum PilotoCorrida com esse piloto!", exception.getMessage());

    }

    @Test
    @DisplayName("Testes buscar piloto_corrida pela corrida")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testfindByRaceOrderByPlacementAsc() {

        var pilotosCorrida = service.findByRaceOrderByPlacementAsc(new Race(3, ZonedDateTime.now(), new Speedway(), new Championship()));
        assertTrue(pilotosCorrida.size() == 1);

        pilotosCorrida = service.findByRaceOrderByPlacementAsc(new Race(4, ZonedDateTime.now(), new Speedway(), new Championship()));
        assertTrue(pilotosCorrida.size() == 1);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByRaceOrderByPlacementAsc(new Race(1, ZonedDateTime.now(), new Speedway(), new Championship())));
        assertEquals("Nenhum PilotoCorrida nesta corrida!", exception.getMessage());

    }

    @Test
    @DisplayName("Testes buscar piloto_corrida entre posições da corrida")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testFindByPlacementBetweenAndRace() {

        var pilotosCorrida = service.findByPlacementBetweenAndRace(1,3,new Race(3, ZonedDateTime.now(), new Speedway(), new Championship()));
        assertTrue(pilotosCorrida.size() == 1);

        pilotosCorrida = service.findByPlacementBetweenAndRace(1,3,new Race(4, ZonedDateTime.now(), new Speedway(), new Championship()));
        assertTrue(pilotosCorrida.size() == 1);

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByPlacementBetweenAndRace(4,5,new Race(4, ZonedDateTime.now(), new Speedway(), new Championship())));
        assertEquals("Nenhum PilotoCorrida com esses parâmetros de busca!", exception.getMessage());

    }

    @Test
    @DisplayName("Testes buscar piloto_corrida entre posições da corrida")
    @Sql({"classpath:/sqls/pais.sql","classpath:/sqls/equipe.sql", "classpath:/sqls/piloto.sql","classpath:/sqls/pista.sql","classpath:/sqls/campeonato.sql", "classpath:/sqls/corrida.sql","classpath:/sqls/piloto_corrida.sql"})
    void testFindByPilotAndRace() {

        var piloto = new Pilot(3, "Leonardo", new Country(3,"Brasil"), new Team(3, "Ferrari"));
        var corrida = new Race(3, ZonedDateTime.now(), new Speedway(), new Championship());
        var pilotoCorrida = service.findByPilotAndRace(piloto, corrida);
        assertNotNull(pilotoCorrida);

        piloto = new Pilot(4, "Clavison", new Country(4,"Japão"), new Team(4, "Red Bull"));
        corrida = new Race(4, ZonedDateTime.now(), new Speedway(), new Championship());

        pilotoCorrida = service.findByPilotAndRace(piloto, corrida);
        assertNotNull(pilotoCorrida);

        corrida = new Race(1, ZonedDateTime.now(), new Speedway(), new Championship());

        var exception = assertThrows(
                ObjectNotFound.class, () -> service.findByPilotAndRace(new Pilot(4, "Clavison", new Country(4,"Japão"), new Team(4, "Red Bull")), new Race(1, ZonedDateTime.now(), new Speedway(), new Championship())));
        assertEquals("Nenhum PilotoCorrida com esses parâmetros de busca!", exception.getMessage());

    }



}
